package com.cjj.keepaccounts.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.LongSparseArray
import androidx.core.util.forEach
import androidx.core.util.set
import com.cjj.keepaccounts.base.BaseEntity
import com.cjj.keepaccounts.bean.*
import com.cjj.keepaccounts.bean.adapter.*
import com.cjj.keepaccounts.bean.event.SyncErrorEvent
import com.cjj.keepaccounts.bean.event.SyncOverEvent
import com.cjj.keepaccounts.bean.event.SyncProgressEvent
import com.cjj.keepaccounts.bean.event.SyncStartEvent
import com.cjj.keepaccounts.bean.http.SyncBase
import com.cjj.keepaccounts.dao.GlobalConfigTool
import com.cjj.keepaccounts.dao.GlobalUserTool
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.dao.RecordTypeTool
import com.cjj.keepaccounts.http.OkGoHttpUtils
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.LogUtils
import com.cjj.keepaccounts.utils.TimeMarkUtils
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.ToastUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import org.greenrobot.eventbus.EventBus
import org.greenrobot.greendao.AbstractDao
import org.jetbrains.anko.collections.forEachWithIndex

var isSync: Boolean = false

class SyncService : IntentService("SyncService") {

    private var progressFloat: Float = 0F
    private var progressInt: Int = 0
        set(value) {
            field = value
            EventBus.getDefault().post(SyncProgressEvent(value))
        }

    /**
     * 向服务器查询的数据的tag数组
     */
    private val mTags = arrayOf(
            "get-accounts-v2",
            "get-recordList-v3",
            "get-recordType-v2",
            "get-user-dc",
            "get-recordMember",
            "get-recordtotag-2",
            "get-record-with-creditid",
//                        "get-notice",
//                        "get-noticeFlowMessage",
            "get-app-config",
//                        "get-saving",
//                        "get-investment",
            "get-budget"
//                        "get-exchangeRate"
    )

    private val size = mTags.size

    private val mGson: Gson by lazy {
        GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Account::class.java, AccountTypeAdapter())
                .registerTypeAdapter(ListBook::class.java, ListBookTypeAdapter())
                .registerTypeAdapter(Credit::class.java, CreditTypeAdapter())
                .registerTypeAdapter(RecordType::class.java, RecordTypeTypeAdapter())
                .registerTypeAdapter(Record::class.java, RecordTypeAdapter())
                .registerTypeAdapter(RecordTag::class.java, RecordTagTypeAdapter())
                .registerTypeAdapter(RecordToTag::class.java, RecordToTagTypeAdapter())
                .registerTypeAdapter(AppConfig::class.java, AppConfigTypeAdapter())
                .registerTypeAdapter(Budget::class.java, BudgetTypeAdapter())
                .create()
    }


    override fun onHandleIntent(intent: Intent?) {
        EventBus.getDefault().post(SyncStartEvent())
        //开启数据事务
        val database = DaoManager.daoSession.database
        database.beginTransaction()
        try {
            //记录时间
            val mark = TimeMarkUtils.mark()
            //存储要更新的表的数据的集合
            val updates = arrayListOf<List<SyncBase<out BaseEntity>>>()

            mTags.forEachIndexed { index, s ->
                //计算进度
                progressFloat = index.toFloat() / size.toFloat()
                progressInt = (progressFloat * 100).toInt()
                //发起请求
                val response = OkGoHttpUtils.post<Void>("")
                        .params("access_token", GlobalConfigTool.getAccessToken())
                        .params("values", "{\"userId\":${GlobalUserTool.globalUser.userId}}")
                        .params("sqltag", s)
                        .params("is_new_user", true)
                        .execute()
                //获取数据
                if (response.code() == 200) {
                    val body = response.body()
                    if (body != null) {
                        //解析基类数据
                        val resultBase = mGson.fromJson(body.charStream(), ResultBase::class.java)
                        if (resultBase.isSuccess) {
                            //处理数据并得到要更新的服务器上的数据
                            val processingData = processingData(s, resultBase.data)
                            //如果数据不为空则添加到集合中
                            if (processingData.isNotEmpty()) {
                                updates.add(processingData)
                            }
                        }
                    }
                }
            }
            //更新数据
            for (update in updates) {
                updateNetworkData(update)
            }
            //数据处理完成，commit
            database.setTransactionSuccessful()
            //置空RecordTool中存储的每月消费数据
            RecordTool.monthRecordRefresh()
            //清除RecordType缓存
            RecordTypeTool.recrodTypeRefresh()
            //发出数据更新完成的消息
            EventBus.getDefault().post(SyncOverEvent(true))
            mark.printTime("同步完成")
        } catch (e: Exception) {
            EventBus.getDefault().post(SyncErrorEvent(e))
            LogUtils.exception(e)
        } finally {
            database.endTransaction()
            isSync = false
        }
    }

    private fun processingData(sqlTag: String, data: JsonElement): List<SyncBase<out BaseEntity>> {
        return when (sqlTag) {
            "get-accounts-v2" -> {
                val accounts: MutableList<Account> = mGson.fromJson(data, object : TypeToken<MutableList<Account>>() {}.type)
                comparisonData(DaoManager.getAccountDao(), "account", accounts)
            }
            "get-recordList-v3" -> {
                val listBooks: MutableList<ListBook> = mGson.fromJson(data, object : TypeToken<MutableList<ListBook>>() {}.type)
                comparisonData(DaoManager.getListBookDao(), "record_list", listBooks)
            }
            "get-user-dc" -> {
                val credits: MutableList<Credit> = mGson.fromJson(data, object : TypeToken<MutableList<Credit>>() {}.type)
                comparisonData(DaoManager.getCreditDao(), "debit_credit", credits)
            }
            "get-recordType-v2" -> {
                val recordTypes: MutableList<RecordType> = mGson.fromJson(data, object : TypeToken<MutableList<RecordType>>() {}.type)
                comparisonData(DaoManager.getRecordTypeDao(), "record_type", recordTypes)

            }
            "get-record-with-creditid" -> {
                val records: MutableList<Record> = mGson.fromJson(data, object : TypeToken<MutableList<Record>>() {}.type)
                comparisonData(DaoManager.getRecordDao(), "record", records)
            }
//            "get-notice" -> {
//                val notices: MutableList<NoticeBean> = mGson.fromJson(data, object : TypeToken<MutableList<NoticeBean>>() {}.type)
//
//            }
            "get-recordMember" -> {
                val temps: MutableList<RecordTag> = mGson.fromJson(data, object : TypeToken<MutableList<RecordTag>>() {}.type)
                comparisonData(DaoManager.getRecordTagDao(), "record_member", temps)
            }
            "get-recordtotag-2" -> {
                val recordToTags: MutableList<RecordToTag> = mGson.fromJson(data, object : TypeToken<MutableList<RecordToTag>>() {}.type)
                comparisonData(DaoManager.getRecordToTagDao(), "record_has_member", recordToTags)
            }
            "get-app-config" -> {
                val appConfigs: MutableList<AppConfig> = mGson.fromJson(data, object : TypeToken<MutableList<AppConfig>>() {}.type)
                comparisonData(DaoManager.getAppConfigDao(), "app_conf2", appConfigs)
            }
//            "get-investment" -> {
//            }
            "get-budget" -> {
                val budgets: MutableList<Budget> = mGson.fromJson(data, object : TypeToken<MutableList<Budget>>() {}.type)
                comparisonData(DaoManager.getBudgetDao(), "budget", budgets)
            }
//            "get-exchangeRate" -> {
//            }
            else -> {
                arrayListOf<SyncBase<BaseEntity>>()
            }
        }
    }

    private fun <T : BaseEntity> comparisonData(dao: AbstractDao<T, Long>, table: String, entities: List<T>): List<SyncBase<T>> {
        //解析数据
        val mark = TimeMarkUtils.mark()
        //创建要更新到服务器上的数据列表
        val networkUpdates = arrayListOf<SyncBase<T>>()
        //本地要升级的数据列表
        val diskUpdates = arrayListOf<T>()

        val queryBuilder = dao.queryBuilder()
        //服务器上的数据列表转换至SparseArray便于查询
        val entitySparse = LongSparseArray<T>()
        for (entity in entities) {
            entitySparse[entity.uuid] = entity
        }
        //查询本地该表所有数据
        val list = queryBuilder.build().forCurrentThread().list()
        //遍历本地数据与服务器数据对比
        list.forEachWithIndex { index, it ->
            //用本地数据的uuid取出对应的服务器数据
            val entity = entitySparse[it.uuid]

            if (entity == null) {//服务器中该数据为空,将该数据放入集合稍后上传数据
                networkUpdates.add(SyncBase("insert", table, it))
            } else {//服务器中该数据不为空
                //对比修改时间,服务器上数据存储的秒,将本地的毫秒转为秒
                val networkTime = entity.mTime / 1000
                val diskTime = it.mTime / 1000
                //服务器中存在该数据,删除SparseArray的对应的数据，剩下的数据则是本地不存在的数据
                entitySparse.remove(entity.uuid)
                if (networkTime != diskTime) {//服务器中的数据与本地数据修改时间不同
                    if (networkTime > diskTime) {//服务器数据较新
                        //存入本地更新的数据列表
                        diskUpdates.add(entity)
                    } else {//本地数据较新
                        //存入服务器更新的数据列表
                        networkUpdates.add(SyncBase("update", table, it))
                    }
                }
            }
            //计算当前进度
            val temp = ((progressFloat + index.toFloat() / list.size.toFloat() * (1F / size.toFloat())) * 100).toInt()
            if (temp != progressInt) {
                progressInt = temp
            }
        }
        //SparseArray中剩下的数据是本地没有的，插入本地数据库
        entitySparse.forEach { _, value ->
            dao.insert(value)
        }
        //更新本地的数据
        diskUpdates.forEach {
            dao.update(it)
        }
        //打印对比的时间
        mark.printTime("$table 对比用时")
        //清除该表所查出的数据的缓存
        dao.detachAll()
        //返回要服务器需要更新的数据
        return networkUpdates
    }

    private fun updateNetworkData(list: List<SyncBase<out BaseEntity>>) {

        if (list.isNotEmpty()) {
            val httpUtils = OkGoHttpUtils.post<Void>("")
                    .params("access_token", GlobalConfigTool.getAccessToken())
                    .params("is_new_user", false)
                    .params("client_time", TimeUtils.second)

            val response = httpUtils.params("json", mGson.toJson(list))
                    .execute()
            if (response.code() == 200) {
                val body = response.body()
                if (body != null) {
                    val resultBase = mGson.fromJson(body.charStream(), ResultBase::class.java)
                    LogUtils.i("${list.first().table}${if (resultBase.isSuccess) "更新成功" else "更新失败,msg:${resultBase.message}"}")
                }
            }
        }
    }


    companion object {
        @JvmStatic
        fun startSyncService(context: Context) {
            if (isSync) {
                ToastUtils.shortToast("正在同步")
                return
            }
            isSync = true
            val intent = Intent(context, SyncService::class.java)
            context.startService(intent)
        }

    }
}
