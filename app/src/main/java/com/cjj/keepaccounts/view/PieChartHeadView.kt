package com.cjj.keepaccounts.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.utils.Utils

/**
 * Created by CJJ on 2018/5/20 18:58.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class PieChartHeadView(context: Context) {
    val headView: View = Utils.inflate(R.layout.head_view_pie_chart, context)

    private val pieChart: PieChartView = headView.findViewById(R.id.pie_chart)
    private val tvMonth: TextView = headView.findViewById(R.id.tv_month)

    init {
        headView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Utils.dip2px(226))
        tvMonth.setOnClickListener {
            listener?.invoke()
        }
    }

    var pieChartInfo: ArrayList<PieChartView.PieChartItem>
        set(value) {
            pieChart.pieChartInfo = value
        }
        get() {
            return pieChart.pieChartInfo
        }

    var money: CharSequence
        set(value) {
            tvMonth.text = value
        }
        get() {
            return tvMonth.text
        }

    private var listener: (() -> Unit)? = null
    fun setOnChangeListener(listener: () -> Unit) {
        this.listener = listener
    }

}