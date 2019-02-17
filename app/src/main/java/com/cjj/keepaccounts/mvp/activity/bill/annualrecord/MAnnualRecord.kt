package com.cjj.keepaccounts.mvp.activity.bill.annualrecord

import com.cjj.keepaccounts.bean.AnnalRecordDayBean
import com.cjj.keepaccounts.bean.AnnalRecordMonthBean
import com.cjj.keepaccounts.utils.TimeUtils
import java.util.*
import javax.inject.Inject

/**
 * Created by CJJ on 2019/1/30 18:05.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class MAnnualRecord @Inject constructor() : CAnnualRecord.Model {
    override fun getMonth(): ArrayList<AnnalRecordMonthBean> {
        val startYear = TimeUtils.year
        val endYear = 2012
        val months = ArrayList<AnnalRecordMonthBean>()
        val calendar = Calendar.getInstance(Locale.CHINA)

        for (year in startYear downTo endYear) {
            val startMonth = 1
            val endMonth = 12

            months.add(AnnalRecordMonthBean(year, 0, true, false, null))
            for (month in startMonth..endMonth) {

                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month - 1)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                val days = arrayListOf<AnnalRecordDayBean>()
                val monthBean = AnnalRecordMonthBean(year, month, false, year >= TimeUtils.year && month > TimeUtils.month, days)


                val monthDayNum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                val week = calendar.get(Calendar.DAY_OF_WEEK)
                val startDay = 2 - week


                for (day in startDay..monthDayNum) {
                    val dayBean = AnnalRecordDayBean(year, month, day, day < 1, 0, false, false)
                    if (year == TimeUtils.year && month == TimeUtils.month && day == TimeUtils.day) {
                        dayBean.isToday = true
                    }
                    if (year >= TimeUtils.year && ((month >= TimeUtils.month && day > TimeUtils.day) || month > TimeUtils.month)) {
                        dayBean.isFuture = true
                    }
                    days.add(dayBean)
                }
                months.add(monthBean)
            }

        }

        return months
    }
}