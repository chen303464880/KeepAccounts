package com.cjj.keepaccounts.view

import android.content.Context
import android.widget.CheckBox
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.utils.Utils

/**
 * @author CJJ
 * Created by CJJ on 2018/5/25 17:20.
 */
class LineChartHeadView(context: Context) {
    val headView = Utils.inflate(R.layout.head_view_line_chart, context)
    private val lineChartView: LineChartView = headView.findViewById(R.id.lcv)
    private val cbIncome: CheckBox = headView.findViewById(R.id.cb_income)
    private val cbExpend: CheckBox = headView.findViewById(R.id.cb_expend)
    private val cbSurplus: CheckBox = headView.findViewById(R.id.cb_surplus)

    init {

        cbIncome.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                if (!cbExpend.isChecked && !cbSurplus.isChecked) {
                    cbIncome.isChecked = true
                }
            }
            setLineChartData()
        }
        cbExpend.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                if (!cbIncome.isChecked && !cbSurplus.isChecked) {
                    cbExpend.isChecked = true
                }
            }
            setLineChartData()
        }
        cbSurplus.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                if (!cbIncome.isChecked && !cbExpend.isChecked) {
                    cbSurplus.isChecked = true
                }
            }
            setLineChartData()
        }

    }

    var minMoney: Double
        set(value) {
            lineChartView.minMoney = value
        }
        get() {
            return lineChartView.minMoney
        }

    var maxMoney: Double
        set(value) {
            lineChartView.maxMoney = value
        }
        get() {
            return lineChartView.maxMoney
        }

    var dataMap = hashMapOf<String, LineChartBean>()
        set(value) {
            field = value
            setLineChartData()
        }

    private fun setLineChartData() {
        val data = ArrayList<LineChartBean>()
        if (cbIncome.isChecked) {
            val income = dataMap["income"]
            if (income != null) {
                data.add(income)
            }
        }
        if (cbExpend.isChecked) {
            val expend = dataMap["expend"]
            if (expend != null) {
                data.add(expend)
            }
        }
        if (cbSurplus.isChecked) {
            val surplus = dataMap["surplus"]
            if (surplus != null) {
                data.add(surplus)
            }
        }
        lineChartView.data = data

    }


}