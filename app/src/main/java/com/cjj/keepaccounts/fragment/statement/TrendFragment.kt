package com.cjj.keepaccounts.fragment.statement

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.TrendDetailsAdapter
import com.cjj.keepaccounts.base.BaseFragment
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.MonthRecordInfo
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.bean.TrendDetailsBean
import com.cjj.keepaccounts.bean.event.SyncOverEvent
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.utils.*
import com.cjj.keepaccounts.view.LineChartBean
import com.cjj.keepaccounts.view.LineChartHeadView
import kotlinx.android.synthetic.main.fragment_trend.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import rx.functions.Action1
import java.math.BigDecimal

/**
 * @author CJJ
 * Created by CJJ on 2017/11/13 13:55.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 * 显示每月消费趋势的页面
 */
class TrendFragment : BaseFragment<EmptyPresenter>() {

    private var year = TimeUtils.year

    private lateinit var adapter: TrendDetailsAdapter
    private lateinit var headView: LineChartHeadView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trend, container, false)
    }


    override fun initView() {
        EventBusUtils.register(this, presenter)
        RecordTool.addOnDaoChangeListener(daoChangeListener, presenter)
        tv_time_interval.text = year.toString()
        rv_trend.layoutManager = LinearLayoutManager(activity!!)
        adapter = TrendDetailsAdapter()
        headView = LineChartHeadView(context!!)
        adapter.addHeaderView(headView.headView)
        rv_trend.adapter = adapter
        rv_trend.addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e7)))
        getData()
    }


    @OnClick(R.id.iv_left, R.id.iv_right)
    fun onClick(view: View) {
        when (view.id) {
            R.id.iv_left -> {
                year -= 1
            }
            R.id.iv_right -> {
                if (year == TimeUtils.year) {
                    return
                } else {
                    year += 1
                }
            }
            else -> {

            }
        }
        tv_time_interval.text = year.toString()
        getData()
    }

    private fun getData() {

        Observable.defer {
            val make = TimeMarkUtils.mark()
            val list = ArrayList<TrendDetailsBean>()
            val start = if (year == TimeUtils.year) TimeUtils.month - 1 else 11
            var totalIncome = BigDecimal.ZERO
            var totalExpend = BigDecimal.ZERO
            for (i in start downTo 0) {
                val monthRecord = RecordTool.getMonthRecord(year, i)
                val incomeDecimal = BigDecimal(monthRecord.income)
                val expendDecimal = BigDecimal(monthRecord.expend)
                totalIncome += BigDecimal(monthRecord.income)
                totalExpend += BigDecimal(monthRecord.expend)
                val surplus = (incomeDecimal - expendDecimal).toDouble()
                list.add(TrendDetailsBean(year, i, monthRecord, surplus))
            }
            val surplus = (totalIncome - totalExpend).toDouble()
            list.add(TrendDetailsBean(year, -1, MonthRecordInfo(totalIncome.toDouble(), totalExpend.toDouble()), surplus))
            make.printTime("趋势查询")
            Observable.just(list)
        }.ioToMain()
                .bindToLifecycle(presenter)
                .subscribe(onNext)
    }

    val onNext = Action1<ArrayList<TrendDetailsBean>> {
        adapter.setData(it)
        adapter.notifyDataSetChanged()

        val incomes = arrayListOf<Double>()
        val expends = arrayListOf<Double>()
        val surplus = arrayListOf<Double>()
        var min = 0.0
        var max = 0.0
        for (i in it.size - 2 downTo 0) {
            val trendDetailsBean = it[i]
            val income = trendDetailsBean.monthRecordInfo.income
            val expend = trendDetailsBean.monthRecordInfo.expend
            val surplu = income - expend
            if (income < min) {
                min = income
            }
            if (expend < min) {
                min = expend
            }
            if (surplu < min) {
                min = surplu
            }
            if (income > max) {
                max = income
            }
            if (expend > max) {
                max = expend
            }
            if (surplu > max) {
                max = surplu
            }
            incomes.add(income)
            expends.add(expend)
            surplus.add(surplu)
        }
        val data = HashMap<String, LineChartBean>()
        data["income"] = (LineChartBean(Utils.getColor(R.color.bg_color_5cc5ac), incomes))
        data["expend"] = (LineChartBean(Utils.getColor(R.color.AppThemeColor), expends))
        data["surplus"] = (LineChartBean(Utils.getColor(R.color.text_color_ff6b6b), surplus))
        headView.minMoney = min
        headView.maxMoney = max
        headView.dataMap = data
    }

    private val daoChangeListener = object : OnDaoChangeListener<Record> {
        override fun onInsertEntity(entity: Record) {
            if (entity.year == year && entity.recordType.isIncoming == 0) {
                getData()
            }
        }

        override fun onUpdateEntity(oldEntity: Record, newEntity: Record) {
            if ((oldEntity.year == year && oldEntity.recordType.isIncoming == 0)
                    || (newEntity.year == year && oldEntity.recordType.isIncoming == 0)) {
                getData()
            }
        }

        override fun onDeleteEntity(entity: Record) {
            if (entity.year == year && entity.recordType.isIncoming == 0) {
                getData()
            }
        }
    }

    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun syncOver(@Suppress("UNUSED_PARAMETER") event: SyncOverEvent) {
        getData()
    }

}