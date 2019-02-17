package com.cjj.keepaccounts.mvp.activity.bill.annualrecord

import com.cjj.keepaccounts.activity.bill.AnnualRecordActivity
import com.cjj.keepaccounts.bean.AnnalRecordMonthBean
import com.cjj.keepaccounts.bean.RecordDao
import com.cjj.keepaccounts.dao.ListBookTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.LogUtils
import com.cjj.keepaccounts.utils.ioToMain
import rx.Emitter
import rx.Observable
import rx.Observer
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Created by CJJ on 2019/1/30 18:05.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class PAnnualRecord @Inject constructor(view: AnnualRecordActivity, model: MAnnualRecord) : CAnnualRecord.Presenter(view, model) {
    override fun presenter() {
        super.presenter()
        val months = model.getMonth()
        view.setAdapterData(months)
        //查询是否超预算
        val startTime = System.currentTimeMillis()
        Observable.create<Pair<Int, AnnalRecordMonthBean>>({ em ->
            val monthBudget = ListBookTool.getBudget().listBudget
            for ((index, value) in months.withIndex()) {
                if (value.isNode) {
                    continue
                }
                val list = DaoManager.getRecordDao().queryBuilder()
                        .where(RecordDao.Properties.Year.eq(value.year))
                        .where(RecordDao.Properties.Month.eq(value.month - 1))
                        .where(RecordDao.Properties.IsDeleted.eq(0))
                        .where(RecordDao.Properties.TypeId.ge(0))
                        .orderDesc(RecordDao.Properties.TheDate)
                        .orderDesc(RecordDao.Properties.Day)
                        .orderDesc(RecordDao.Properties.TheHour)
                        .orderDesc(RecordDao.Properties.CreateTime)
                        .list()
                if (list.isEmpty()) {
                    continue
                }
                val groupBy = list.groupBy { it.day }
                val days = value.days!!
                days.forEach {
                    val dayRecords = groupBy[it.day] ?: return@forEach
                    var expend = BigDecimal(0.0)
                    val budget = monthBudget / dayRecords.last().day
                    dayRecords.forEach { record ->
                        //判断收入与支出
                        val type = record.recordType
                        if (type.isIncoming == 0) {
                            expend += BigDecimal(Math.abs(record.rateMoney))
                        }
                    }
                    if (expend.toDouble() > 0.0) {
                        it.state = 1
                    }
                    if (expend.toFloat() > budget) {
                        it.state = 2
                    }
                }
                em.onNext(index to value)

            }
            em.onCompleted()
        }, Emitter.BackpressureMode.BUFFER)
                .ioToMain()
                .subscribe(object : Observer<Pair<Int, AnnalRecordMonthBean>> {

                    override fun onCompleted() {
                        LogUtils.i("TAG", "查询数据库用时:${System.currentTimeMillis() - startTime}ms")
                    }

                    override fun onNext(t: Pair<Int, AnnalRecordMonthBean>) {
                        view.updateData(t.first, t.second)
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }


}