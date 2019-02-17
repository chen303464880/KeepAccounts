package com.cjj.keepaccounts.activity.statement

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.core.view.get
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.bean.DateIntervalBean
import com.cjj.keepaccounts.mvp.activity.statement.selectdateintervalactivity.CSelectDateInterval
import com.cjj.keepaccounts.mvp.activity.statement.selectdateintervalactivity.PSelectDateInterval
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.Utils
import kotlinx.android.synthetic.main.activity_select_date_interval.*
import org.greenrobot.eventbus.EventBus

/**
 * @author CJJ
 * Created by CJJ on 2018/5/28 15:29.
 */
class SelectDateIntervalActivity : WhiteActivity<PSelectDateInterval>(), CSelectDateInterval.View {

    private val firstDayWeek = TimeUtils.firstDayOfWeek()
    private val lastDayWeek = TimeUtils.lastDayOfWeek()
    private val firstDayMonth = TimeUtils.firstDayOfMonth()
    private val lastDayMonth = TimeUtils.lastDayOfMonth()
    private val firstDayYear = TimeUtils.firstDayOfYear()
    private val lastDayYear = TimeUtils.lastDayOfYear()

    private val info by extra<Bundle>()


    override fun getContentView(): View {
        return Utils.inflate(R.layout.activity_select_date_interval, this)
    }


    override fun initView() {
        setActivityBackText(getString(R.string.statement))
        setActivityTitleText(getString(R.string.select_date))
        setNextText(getString(R.string.ok))
        showTitleLine()
        val startDate = info.getInt("startDate")
        val endDate = info.getInt("endDate")
        dp_start_date.init(TimeUtils.getTheDateYear(startDate)
                , TimeUtils.getTheDateMonth(startDate)
                , TimeUtils.getTheDateDay(startDate)
                , listener)
        dp_end_date.init(TimeUtils.getTheDateYear(endDate)
                , TimeUtils.getTheDateMonth(endDate)
                , TimeUtils.getTheDateDay(endDate)
                , listener)
        examineDate()
    }

    override fun initListener() {
        super.initListener()
        rg_date_interval.setOnCheckedChangeListener { _, checkedId ->
            val index = rg_date_interval.indexOfChild(rg_date_interval.findViewById(checkedId))
            when (index) {
                0 -> {
                    updateDate(firstDayWeek, lastDayWeek)
                }
                1 -> {
                    updateDate(firstDayMonth, lastDayMonth)
                }
                2 -> {
                    updateDate(firstDayYear, lastDayYear)
                }
                else -> {
                }
            }
        }

    }

    private fun examineDate() {
        val startDate = TimeUtils.getTheDate(dp_start_date.year, dp_start_date.month, dp_start_date.dayOfMonth)
        val endDate = TimeUtils.getTheDate(dp_end_date.year, dp_end_date.month, dp_end_date.dayOfMonth)
        if (startDate == firstDayWeek && endDate == lastDayWeek) {
            rg_date_interval.check(rg_date_interval[0].id)
        } else if (startDate == firstDayMonth && endDate == lastDayMonth) {
            rg_date_interval.check(rg_date_interval[1].id)
        } else if (startDate == firstDayYear && endDate == lastDayYear) {
            rg_date_interval.check(rg_date_interval[2].id)
        } else {
            rg_date_interval.clearCheck()
        }
    }

    private fun updateDate(startDate: Int, endDate: Int) {
        dp_start_date.updateDate(TimeUtils.getTheDateYear(startDate)
                , TimeUtils.getTheDateMonth(startDate)
                , TimeUtils.getTheDateDay(startDate))
        dp_end_date.updateDate(TimeUtils.getTheDateYear(endDate)
                , TimeUtils.getTheDateMonth(endDate)
                , TimeUtils.getTheDateDay(endDate))
    }

    val listener = DatePicker.OnDateChangedListener { _, _, _, _ -> examineDate() }


    override fun onNext() {
        super.onNext()
        val bean = DateIntervalBean(dp_start_date.year, dp_start_date.month, dp_start_date.dayOfMonth
                , dp_end_date.year, dp_end_date.month, dp_end_date.dayOfMonth)
        EventBus.getDefault().post(bean)
        finish()
    }
}