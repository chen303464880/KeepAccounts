package com.cjj.keepaccounts.dialog

import android.content.Context
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.application.MyApplication
import com.cjj.keepaccounts.base.BaseDialog
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.view.CycleWheelView
import java.util.*
import kotlin.collections.ArrayList


/**
 * @author chenjunjie
 * Created by CJJ on 2018/2/6 16:14.
 */
class SelectDateDialog(context: Context) : BaseDialog(context) {
    /**
     * 年份的选择器
     */
    private lateinit var wheelViewYear: CycleWheelView
    /**
     * 月份的选择器
     */
    private lateinit var wheelViewMonth: CycleWheelView
    /**
     * 日的选择器
     */
    private lateinit var wheelViewDay: CycleWheelView

    /**
     * 1901年到当前年份的list
     */
    private var years: MutableList<String>? = null
    private val yearsUnlimited: ArrayList<String> by lazy(LazyThreadSafetyMode.NONE) {
        val list = ArrayList<String>()
        for (i in 1901..2050) {
            list.add(i.toString())
        }
        list
    }
    /**
     * 12个月的list
     */
    private lateinit var months: List<String>
    private val currentMonths: List<String> by lazy(LazyThreadSafetyMode.NONE) {
        val i1 = TimeUtils.month
        if (i1 < 12) {
            (1..i1).map { it.toString() }
        } else {
            months
        }
    }
    /**
     * 31天的list
     */
    private var day1: MutableList<String>? = null
    /**
     * 30天的list
     */
    private var day2: MutableList<String>? = null
    /**
     * 29天的list
     */
    private var day3: MutableList<String>? = null
    /**
     * 28天的list
     */
    private var day4: MutableList<String>? = null

    private val currentDays: List<String> by lazy(LazyThreadSafetyMode.NONE) {
        (1..TimeUtils.day).map { it.toString() }
    }

    private var listener: ((year: Int, month: Int, day: Int) -> Unit)? = null
    /**
     * 日历
     */
    private var calendar: Calendar? = null
    /**
     * 年份改变的监听器
     */
    private var yearListener: YearListener? = null
    /**
     * 月份改变的监听器
     */
    private var monthListener: MonthListener? = null

    private var year: Int = TimeUtils.year
    private var month: Int = TimeUtils.month
    private var day: Int = TimeUtils.day

    var unlimited = false
        set(value) {
            field = value
            if (field) {
                wheelViewYear.labels = yearsUnlimited
            } else {
                wheelViewYear.labels = years
            }
        }

    fun setOnDatePositiveListener(listener: (year: Int, month: Int, day: Int) -> Unit) {
        this.listener = listener
    }

    override fun init() {
        setPositiveListener {
            val year = Integer.parseInt(wheelViewYear.selectLabel)
            val month = Integer.parseInt(wheelViewMonth.selectLabel)
            val dayOfMonth = Integer.parseInt(wheelViewDay.selectLabel)
            listener?.invoke(year, month, dayOfMonth)
        }
    }

    override fun setContentView(): View {
        calendar = Calendar.getInstance(Locale.CHINA)
        years = ArrayList()


        yearListener = YearListener()
        monthListener = MonthListener()


        //初始化日期list
        for (i in 1901..calendar!!.get(Calendar.YEAR)) {
            years!!.add(i.toString())
        }
        months = (1..12).map { it.toString() }


        day1 = ArrayList()
        day2 = ArrayList()
        day3 = ArrayList()
        day4 = ArrayList()

        for (i in 1..31) {
            day1!!.add(i.toString())
            if (i <= 30) {
                day2!!.add(i.toString())
            }
            if (i <= 29) {
                day3!!.add(i.toString())
            }
            if (i <= 28) {
                day4!!.add(i.toString())
            }
        }


        //初始化布局
        val view = View.inflate(context, R.layout.dialog_set_birthday_view, null)
        wheelViewYear = view.findViewById(R.id.wheel_view_year)
        wheelViewMonth = view.findViewById(R.id.wheel_view_month)
        //        wheelViewMonth.setCycleEnable(true);
        wheelViewDay = view.findViewById(R.id.wheel_view_day)
        //        wheelViewDay.setCycleEnable(true);

        //设置初始数据
        if (unlimited) {
            wheelViewYear.labels = yearsUnlimited
        } else {
            wheelViewYear.labels = years
        }
        wheelViewMonth.labels = months
        wheelViewDay.labels = day1

        wheelViewYear.setOnWheelItemSelectedListener(yearListener)
        wheelViewMonth.setOnWheelItemSelectedListener(monthListener)
        wheelViewDay.setOnWheelItemSelectedListener { _, _ ->
            //修改title的值
            setTitle(String.format("%s年%s月%s日",
                    wheelViewYear.selectLabel,
                    wheelViewMonth.selectLabel,
                    wheelViewDay.selectLabel))
        }
        return view
    }

    fun setDate(time: Long) {
        setBirthday(TimeUtils.getYear(time), TimeUtils.getMonth(time), TimeUtils.getDay(time))
    }

    fun setBirthday(year: String, month: String, day: String) {
        setBirthday(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day))
    }

    fun setBirthday(year: Int, month: Int, day: Int) {

        //清除掉监听器
        wheelViewYear.setOnWheelItemSelectedListener(null)
        wheelViewMonth.setOnWheelItemSelectedListener(null)


        //设置年
        var yearIndex = year - 1901
        yearIndex = if (yearIndex <= wheelViewYear.count - 1) yearIndex else wheelViewYear.count - 1
        wheelViewYear.selection = yearIndex


        //设置月
        if (year.toString() == wheelViewYear.labels.last() && wheelViewMonth.count == 12) {//若为当前年份，则可能不满12个月，重新获取月的数据
            wheelViewMonth.labels = currentMonths
        }

        val monthInt = if (month <= wheelViewMonth.count - 1) month - 1 else wheelViewMonth.count - 1
//        wheelViewMonth!!.selection = monthInt


        //设置日
        val m = TimeUtils.month
        if (m - 1 == monthInt) {//若为当前月份，则可能不满28或29或30或31天，重新获取天的数据
            val list = (1..TimeUtils.day).map { it.toString() }
            wheelViewDay.labels = list
        }

        val dayInt = if (day <= wheelViewDay.count - 1) day - 1 else wheelViewDay.count - 1
        wheelViewDay.selection = dayInt

        //延迟500ms设置监听器
        MyApplication.HANDLER.postDelayed({
            wheelViewYear.setOnWheelItemSelectedListener(yearListener)
            wheelViewMonth.setOnWheelItemSelectedListener(monthListener)
            wheelViewMonth.selection = monthInt
        }, 100)
    }


    /**
     * 修改天的数据
     *
     * @param yearStr  年
     * @param monthStr 月
     */
    private fun setDayData(yearStr: String, monthStr: String) {
        val year = Integer.parseInt(yearStr)
        val month = Integer.parseInt(monthStr)
        val days = getDays(year, month)
        if (days !== wheelViewDay.labels) {
            wheelViewDay.labels = days
        }
    }

    /**
     * 获取天的数据
     * @param year 年
     * @param month  月
     * @return 根据年月的数据获取这个月有多少天
     */
    private fun getDays(year: Int, month: Int): List<String>? {
        return if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            day1
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            day2
        } else {
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                day3
            } else {
                day4
            }
        }
    }

    private inner class YearListener : CycleWheelView.WheelItemSelectedListener {
        override fun onItemSelected(position: Int, label: String) {
            if (position != wheelViewYear.labels.size - 1) {
                //不是当前的年份，并且没有十二个月的时候，换回12个月
                if (months !== wheelViewMonth.labels) {
                    wheelViewMonth.labels = months
                }
                setDayData(label, wheelViewMonth.selectLabel)
            } else {
                //如果是当前年份,需要重新计算当前的月份
                if (wheelViewMonth.count == 12) {//若为当前年份，则可能不满12个月，重新获取月的数据
                    wheelViewMonth.labels = currentMonths
                }
                if (wheelViewMonth.selectLabel == wheelViewMonth.labels.last() && wheelViewDay.labels.size > TimeUtils.day) {
                    val list = (1..TimeUtils.day).map { it.toString() }
                    val selection = wheelViewDay.selection
                    wheelViewDay.labels = list
                    if (selection < list.size) {
                        wheelViewDay.selection = selection
                    }
                }
            }
            //修改title的值
            setTitle(String.format("%s年%s月%s日",
                    wheelViewYear.selectLabel,
                    wheelViewMonth.selectLabel,
                    wheelViewDay.selectLabel))
        }
    }

    private inner class MonthListener : CycleWheelView.WheelItemSelectedListener {
        override fun onItemSelected(position: Int, label: String) {
            if (wheelViewMonth.labels.size == 12) {
                setDayData(label, wheelViewMonth.selectLabel)
            } else {
                if (position == wheelViewMonth.labels.size - 1) {
                    //如果item没有12个,则为当前月,需要重新计算当前月份的天数
                    wheelViewDay.labels = currentDays
                } else {
                    setDayData(label, wheelViewMonth.selectLabel)
                }
            }
            //修改title的值
            setTitle(String.format("%s年%s月%s日",
                    wheelViewYear.selectLabel,
                    wheelViewMonth.selectLabel,
                    wheelViewDay.selectLabel))
        }
    }

}