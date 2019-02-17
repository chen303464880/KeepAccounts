package com.cjj.keepaccounts.utils

import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author CJJ
 * Created by CJJ on 2017/11/13 17:32.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 */
object TimeUtils {


    private val calendar = Calendar.getInstance(Locale.CHINA)!!
    @JvmStatic
    var year: Int = calendar.get(Calendar.YEAR)
        get() = calendar.get(Calendar.YEAR)

    @JvmStatic
    var month: Int = calendar.get(Calendar.MONTH) + 1
        get() = calendar.get(Calendar.MONTH) + 1

    @JvmStatic
    var day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        get() = calendar.get(Calendar.DAY_OF_MONTH)

    @JvmStatic
    var hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        get() = calendar.get(Calendar.HOUR_OF_DAY)

    @JvmStatic
    val second: Long
        get() = System.currentTimeMillis() / 1000

    @JvmStatic
    val timeOfSecond: Long
        get() = second * 1000

    @JvmStatic
    fun getYear(time: Long): Int {
        calendar.time = Date(time)
        val year = calendar.get(Calendar.YEAR)
        calendar.time = Date()
        return year
    }

    @JvmStatic
    fun getMonth(time: Long): Int {
        calendar.time = Date(time)
        val month = calendar.get(Calendar.MONTH) + 1
        calendar.time = Date()
        return month
    }

    @JvmStatic
    fun getDay(time: Long): Int {
        calendar.time = Date(time)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.time = Date()
        return day
    }

    @JvmStatic
    fun getHour(time: Long): Int {
        calendar.time = Date(time)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        calendar.time = Date()
        return hour
    }

    @JvmStatic
    fun getDayOfMonth() = calendar.getActualMaximum(Calendar.DATE)

    @JvmStatic
    fun getMonthMax(year: Int, month: Int): Int {
        val instance = Calendar.getInstance()
        instance.set(Calendar.YEAR, year)
        instance.set(Calendar.MONTH, month)
        return instance.getActualMaximum(Calendar.DAY_OF_MONTH)
    }


    @JvmStatic
    fun firstDayOfYear(): Int {
        return getTheDate(year, 0, 1)
    }

    @JvmStatic
    fun lastDayOfYear(): Int {
        return getTheDate(year, 11, getMonthMax(year, 12))
    }

    @JvmStatic
    fun firstDayOfMonth(): Int {
        return getTheDate(year, month - 1, 1)
    }

    @JvmStatic
    fun lastDayOfMonth(): Int {
        val month = this.month - 1
        return getTheDate(year, month, getMonthMax(year, month))
    }

    @JvmStatic
    fun firstDayOfWeek(): Int {
        val instance = Calendar.getInstance()
        instance.set(Calendar.DAY_OF_WEEK, 1)
        return getTheDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH))
    }

    @JvmStatic
    fun lastDayOfWeek(): Int {
        val instance = Calendar.getInstance()
        instance.set(Calendar.DAY_OF_WEEK, 7)
        return getTheDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH))
    }

    @JvmStatic
    fun getTheDate(year: Int, month: Int, day: Int): Int {
        return year * 10000 + month * 100 + day
    }

    @JvmStatic
    fun getTheDateYear(theDate: Int): Int {
        return theDate / 10000
    }

    @JvmStatic
    fun getTheDateMonth(theDate: Int): Int {
        return (theDate % 10000) / 100
    }

    @JvmStatic
    fun getTheDateDay(theDate: Int): Int {
        return theDate % 100
    }

    /**
     * 字符串时间转long
     *
     * @param strTime 时间
     * @param format  时间格式
     * @return LongTime
     */
    @JvmStatic
    fun timeTurnLong(strTime: String, format: String): Long {
        val sdf = SimpleDateFormat(format, Locale.CHINA)
        var time: Long = 0
        try {
            val date = sdf.parse(strTime)
            time = date.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return time

    }


    /**
     * long类型时间转字符串
     *
     * @param longTime long时间
     * @return StrTime
     */
    @JvmStatic
    fun longTurnTime(longTime: Long): String {
        val timeZone = "GMT+8"
        val format = "yyyy-MM-dd HH:mm:ss"
        return longTurnTime(longTime, format, timeZone)
    }

    /**
     * long类型时间转字符串
     *
     * @param longTime long时间
     * @param format   时间格式
     * @return StrTime
     */
    @JvmStatic
    fun longTurnTime(longTime: Long, format: String): String {

        val timeZone = "GMT+8"
        return longTurnTime(longTime, format, timeZone)
    }

    /**
     * long类型时间转字符串
     *
     * @param longTime long时间
     * @param format   时间格式
     * @param timeZone 时区
     * @return StrTime
     */
    @JvmStatic
    fun longTurnTime(longTime: Long, format: String, timeZone: String): String {
        var timeZone1 = timeZone
        if (TextUtils.isEmpty(timeZone1)) {
            timeZone1 = "GMT+8"
        }
        val sf = SimpleDateFormat(format, Locale.CHINA)
        sf.timeZone = TimeZone.getTimeZone(timeZone1)
        val date = Date(longTime)
        return sf.format(date)
    }

    /**
     * 比较两个字符串类型的时间的大小
     *
     * @param timeStr1 第一个时间
     * @param timeStr2 第二个时间
     * @return 第一个时间在第二个时间之后，return true
     */
    @JvmStatic
    fun timeAfterTime(timeStr1: String, timeStr2: String): Boolean {
        val format = "yyyy-MM-dd HH:mm:ss"
        val sf = SimpleDateFormat(format, Locale.CHINA)
        return try {
            val time1 = sf.parse(timeStr1)
            val time2 = sf.parse(timeStr2)
            time1.after(time2)
        } catch (e: ParseException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 判断两个时间是否是同一天
     */
    @JvmStatic
    fun isTimeSameDate(time1: Long, time2: Long): Boolean {
        if (time1 == time2) {
            return true
        }
        val instance = Calendar.getInstance()
        instance.time = Date(time1)
        instance.set(Calendar.HOUR_OF_DAY, 0)
        val zero = instance.timeInMillis
        instance.set(Calendar.HOUR_OF_DAY, 24)
        return time2 in zero..instance.timeInMillis
    }


    @JvmStatic
    fun getDayLongTime(year: Int, month: Int, day: Int): Long {
        val tempM = calendar.timeInMillis
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val timeInMillis = calendar.timeInMillis
        calendar.timeInMillis = tempM
        return timeInMillis
    }

    @JvmStatic
    fun getStartTime(year: Int, month: Int, day: Int): Long {
        val tempM = calendar.timeInMillis
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val timeInMillis = calendar.timeInMillis
        calendar.timeInMillis = tempM
        return timeInMillis
    }

    @JvmStatic
    fun getEndTime(year: Int, month: Int, day: Int): Long {
        val tempM = calendar.timeInMillis
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val timeInMillis = calendar.timeInMillis
        calendar.timeInMillis = tempM
        return timeInMillis
    }


}