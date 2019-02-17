package com.cjj.keepaccounts.mvp.activity.statement.settingshowclassify

import com.cjj.keepaccounts.bean.AppConfig
import com.cjj.keepaccounts.bean.AppConfigDao
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.bean.RecordTypeDao
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.TimeMarkUtils
import com.cjj.keepaccounts.utils.ioToMain
import org.jetbrains.anko.collections.forEachWithIndex
import rx.Observable
import javax.inject.Inject

/**
 * @author CJJ
 * Created by CJJ on 2018/8/9 15:00.
 */
class MSettingShowClassify @Inject constructor() : CSettingShowClassify.Model {

    override val checkTypeMap: HashMap<Long, AppConfig> = hashMapOf()


    override fun getData(): Observable<Pair<HashMap<Long, AppConfig>, List<RecordType>>> {
        return Observable.defer {
            val make = TimeMarkUtils.mark()
            val list = DaoManager.getAppConfigDao().queryBuilder()
                    .where(AppConfigDao.Properties.ConfVal.eq("1"))
                    .where(AppConfigDao.Properties.Uuid.ge("10000"))
                    .list()
            val uuidMap = hashMapOf<Long, AppConfig>()
            list.forEach {
                val uuid = it.confKey.substringAfterLast("=")
                uuidMap[uuid.toLong()] = it
            }
            val recordTypes = DaoManager.getRecordTypeDao().queryBuilder()
                    .where(RecordTypeDao.Properties.ListId.eq(1))
                    .where(RecordTypeDao.Properties.IsIncoming.eq(0))
                    .where(RecordTypeDao.Properties.IsDeleted.eq(0))
                    .where(RecordTypeDao.Properties.ImgSrcId.ge(0))
                    .orderAsc(RecordTypeDao.Properties.OrderIndex)
                    .list()

            recordTypes.forEachWithIndex { _, it ->
                if (uuidMap[it.showId] != null) {
                    it.isCheck = true
                }
            }
            make.printTime("查询展示分类")
            Observable.just(Pair(uuidMap, recordTypes))
        }.ioToMain()
    }
}