package com.cjj.keepaccounts.fragment.statement

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.set
import butterknife.OnClick
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.bill.ClassifyDetailsActivity
import com.cjj.keepaccounts.adapter.ContrastAdapter
import com.cjj.keepaccounts.base.BaseFragment
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.*
import com.cjj.keepaccounts.bean.event.SyncOverEvent
import com.cjj.keepaccounts.dao.AppConfigTool
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.dao.RecordTypeTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.EventBusUtils
import com.cjj.keepaccounts.utils.TimeMarkUtils
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.ioToMain
import kotlinx.android.synthetic.main.fragment_contrast.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.collections.forEachWithIndex
import rx.Observable
import rx.functions.Action1
import java.math.BigDecimal

/**
 * @author CJJ
 * Created by CJJ on 2017/11/13 13:55.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 * 显示对比的页面
 */
class ContrastFragment : BaseFragment<EmptyPresenter>() {
    private var startYear = TimeUtils.year
    private var startMonth = 0
    private var startDay = 1
    private var endYear = TimeUtils.year
    private var endMonth = 11
    private var endDay = TimeUtils.getMonthMax(endYear, endMonth + 1)

    private var startDate = startYear * 10000 + startMonth * 100 + startDay
    private var endDate = endYear * 10000 + endMonth * 100 + endDay

    private val adapter = ContrastAdapter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contrast, container, false)
    }

    override fun initView() {
        EventBusUtils.register(this, presenter)
        RecordTool.addOnDaoChangeListener(recordDaoChangeListener, presenter)
        AppConfigTool.addOnDaoChangeListener(appConfigDaoChangeListener, presenter)
        rv_contrast.layoutManager = LinearLayoutManager(context)
        rv_contrast.adapter = adapter
        tv_time_interval.text = endYear.toString()
        getData()
    }

    override fun initListener() {
        super.initListener()
        adapter.setOnItemClickListener { _, _, itemInfo ->
            val startDate = startYear * 10000 + startMonth * 100 + startDay
            val endDate = endYear * 10000 + endMonth * 100 + endDay
            val bean = StatementDetailBean(startDate, endDate, itemInfo.uuid, itemInfo.totalMoney, 0, itemInfo.desc, 0, itemInfo.color, itemInfo.imgId, 0.0)
            val activityInfo = ActivityInfoBean(getString(R.string.xx_classify_details, itemInfo.desc), getString(R.string.contrast), itemInfo.color)
            ActivityTool.skipActivity<ClassifyDetailsActivity>(bean, activityInfo = activityInfo)
        }
    }

    @OnClick(R.id.iv_left, R.id.iv_right)
    fun onClick(view: View) {
        when (view.id) {
            R.id.iv_left -> {
                endYear -= 1
                startYear -= 1
            }
            R.id.iv_right -> {
                if (startYear == TimeUtils.year) {
                    return
                } else {
                    endYear += 1
                    startYear += 1
                }
            }
            else -> {

            }
        }
        tv_time_interval.text = endYear.toString()
        startDate = startYear * 10000 + startMonth * 100 + startDay
        endDate = endYear * 10000 + endMonth * 100 + endDay
        getData()
    }

    private fun getData() {
        Observable.defer {
            val make = TimeMarkUtils.mark()
            val list = DaoManager.getAppConfigDao().queryBuilder()
                    .where(AppConfigDao.Properties.ConfVal.eq("1"))
                    .where(AppConfigDao.Properties.Uuid.ge("10000"))
                    .list()

            val infoList = list.map {
                getContrastItem(startDate, endDate, it.typeId)
            } as ArrayList

            infoList.sort()

            make.printTime("对比查询")
            Observable.just(infoList)
        }.ioToMain()
                .subscribe(onNext)

    }

    private fun getContrastItem(startDate: Int, endDate: Int, it: String): ContrastItemBean {
        val sql = "select count(*),sum(rate_money),month from record where is_deleted==0 and  the_date between ? and ? and typeId==? group by month order by month asc"
        val rawQuery = DaoManager.daoSession.database.rawQuery(sql, arrayOf(startDate.toString(), endDate.toString(), it))
        var maxMoney = 0.0
        var totalMoney = BigDecimal.ZERO
        var monthCount = 0


        val months = SparseArray<Double>()
        while (rawQuery.moveToNext()) {

            val count = rawQuery.getInt(0)
            val money = rawQuery.getDouble(1)
            val month = rawQuery.getInt(2)

            if (maxMoney < money) {
                maxMoney = money
            }
            monthCount += count
            totalMoney += BigDecimal(money)
            months[month + 1] = money
        }
        rawQuery.close()
        val average = if (monthCount == 0) 0.0 else (totalMoney / BigDecimal(monthCount)).toDouble()
        val recordType = RecordTypeTool.getRecordType(it.toLong())
        return ContrastItemBean(recordType.uuid, recordType.typeDesc, recordType.imgSrcId
                , recordType.color, maxMoney, totalMoney.toDouble(), monthCount, average, months, recordType.orderIndex)
    }

    val onNext = Action1<ArrayList<ContrastItemBean>> {
        adapter.setData(it)
    }

    private val recordDaoChangeListener = object : OnDaoChangeListener<Record> {
        override fun onInsertEntity(entity: Record) {
            if (entity.theDate in startDate..endDate) {
                getData()
            }
        }

        override fun onUpdateEntity(oldEntity: Record, newEntity: Record) {
            if (oldEntity.theDate in startDate..endDate || newEntity.theDate in startDate..endDate) {
                getData()
            }
        }

        override fun onDeleteEntity(entity: Record) {
            if (entity.theDate in startDate..endDate) {
                getData()
            }
        }
    }

    private val appConfigDaoChangeListener = object : OnDaoChangeListener<AppConfig> {
        override fun onInsertEntity(entity: AppConfig) {

        }

        override fun onUpdateEntity(oldEntity: AppConfig, newEntity: AppConfig) {

        }

        override fun onDeleteEntity(entity: AppConfig) {

        }

        override fun onRangeInsertEntity(entities: Iterable<AppConfig>) {
            entities.forEach {
                val contrastItem = getContrastItem(startDate, endDate, it.typeId)
                run {
                    adapter.data.forEachWithIndex { i, contrastItemBean ->
                        if (contrastItem.orderIndex < contrastItemBean.orderIndex) {
                            adapter.insertData(i, contrastItem)
                            return@run
                        }
                    }
                    adapter.insertData(adapter.data.size, contrastItem)
                }
            }
        }

        override fun onRangeUpdateEntity(oldEntities: Iterable<AppConfig>, newEntities: Iterable<AppConfig>) {
            super.onRangeUpdateEntity(oldEntities, newEntities)

            onRangeInsertEntity(newEntities.filter {
                it.confVal == "1"
            })

            onRangeDeleteEntity(newEntities.filter {
                it.confVal == "0"
            })
        }

        override fun onRangeDeleteEntity(entities: Iterable<AppConfig>) {
            val map = hashMapOf<Long, AppConfig>()
            entities.forEach {
                map[it.typeId.toLong()] = it
            }
            val data = adapter.data
            var size = data.size
            var index = 0
            for (i in 0 until size) {
                val it = data[index]
                if (map[it.uuid] != null) {
                    adapter.removeData(it)
                    --size
                } else {
                    ++index
                }
            }
        }
    }

    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun syncOver(@Suppress("UNUSED_PARAMETER") event: SyncOverEvent) {
        getData()
    }

}
