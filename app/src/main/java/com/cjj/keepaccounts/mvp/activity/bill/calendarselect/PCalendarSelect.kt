package com.cjj.keepaccounts.mvp.activity.bill.calendarselect

import android.util.Log
import com.cjj.keepaccounts.activity.bill.CalendarSelectActivity
import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.bean.CalendarDayBean
import com.cjj.keepaccounts.utils.LogUtils
import com.cjj.keepaccounts.utils.TimeUtils
import java.util.*
import javax.inject.Inject

/**
 * Created by CJJ on 2019/1/31 18:29.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class PCalendarSelect @Inject constructor(view: CalendarSelectActivity, model: BaseModel) : CCalendarSelect.Presenter(view, model) {

    private val calendarDayBean by extra<CalendarDayBean>()

    override fun presenter() {
        super.presenter()
        val startTime = System.currentTimeMillis()

        val startYear = 2012
        val endYear = TimeUtils.year
        val days = ArrayList<CalendarDayBean>(3000)
        val calendar = Calendar.getInstance(Locale.CHINA)

        var position = -1
        //保存上个月一共有多少天
        var lastMonthDayNum = 31
        for (year in startYear..endYear) {
            val startMonth = 1
            val endMonth = if (year == endYear) TimeUtils.month else 12
            for (month in startMonth..endMonth) {

                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month - 1)
                calendar.set(Calendar.DAY_OF_MONTH, 1)


                val monthDayNum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                val week = calendar.get(Calendar.DAY_OF_WEEK)
                val startDay = 2 - week
                val endDay = monthDayNum + (7 - (monthDayNum - startDay) % 7) - 1

                val node = CalendarDayBean(year, month, -1, false, false, false, false, true)
                days.add(node)

                for (day in startDay..endDay) {
                    var d = day
                    if (day < 1) {
                        d = lastMonthDayNum + day
                    }
                    if (day > monthDayNum) {
                        d = day - monthDayNum
                    }
                    val dayBean = CalendarDayBean(year, month, d, false, day < 1, day > monthDayNum, false, false)
                    if (year >= TimeUtils.year && month >= TimeUtils.month && day > TimeUtils.day) {
                        dayBean.isFuture = true
                    }
                    if (year == calendarDayBean.year && month == calendarDayBean.month && day == calendarDayBean.day) {
                        dayBean.isChecked = true
                        position = days.size
                    }
                    days.add(dayBean)
                }

                lastMonthDayNum = monthDayNum
            }
        }
        view.setAdapterData(days)
        view.setPosition(position)
        LogUtils.i("一共有${days.size}个对象")
        Log.i("TAG", "用时:${System.currentTimeMillis() - startTime}ms")
    }
}