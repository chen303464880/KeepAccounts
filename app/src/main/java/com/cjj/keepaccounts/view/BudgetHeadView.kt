package com.cjj.keepaccounts.view

import android.content.Context
import android.widget.ProgressBar
import android.widget.TextView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.bean.ListBook
import com.cjj.keepaccounts.dao.ListBookTool
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.utils.toMoney

/**
 * @author CJJ
 * Created by CJJ on 2018/7/4 15:39.
 */
class BudgetHeadView(val context: Context) {
    val headView = Utils.inflate(R.layout.head_view_budget, context)
    private val tvResidualBudget: TextView = headView.findViewById(R.id.tv_residual_budget)
    private val tvBudgetDesc: TextView = headView.findViewById(R.id.tv_budget_desc)
    private val pbBudget: ProgressBar = headView.findViewById(R.id.pb_budget)
    private val tvTotalBudget: TextView = headView.findViewById(R.id.tv_total_budget)

    fun setData(listBook: ListBook) {
        val monthBudget = listBook.listBudget




        when (listBook.listBudgetShow) {
            "month" -> {
                val expend = RecordTool.getMonthRecord(TimeUtils.year, TimeUtils.month - 1).expend
                tvResidualBudget.text = (monthBudget - expend).toMoney()
                tvTotalBudget.text = monthBudget.toString()
                pbBudget.progress = ((1 - expend.toFloat() / monthBudget) * 100F).toInt()
                tvBudgetDesc.text = context.getString(R.string.surplus_month_budget)
            }
            "week" -> {
                val week = ListBookTool.getWeekBudget(TimeUtils.year, TimeUtils.month - 1, TimeUtils.day)
                val weekBudget = week.first
                val weekExpend = week.second
                tvResidualBudget.text = (weekBudget - weekExpend).toMoney()
                tvTotalBudget.text = weekBudget.toMoney()
                pbBudget.progress = ((1 - weekExpend / weekBudget) * 100.0).toInt()
                tvBudgetDesc.text = context.getString(R.string.surplus_week_budget)
            }
            "day" -> {
                val day = ListBookTool.getDayBudget(TimeUtils.year, TimeUtils.month - 1, TimeUtils.day)
                val dayBudget = day.first
                val dayExpend = day.second

                tvResidualBudget.text = (dayBudget - dayExpend).toMoney()
                tvTotalBudget.text = dayBudget.toMoney()
                pbBudget.progress = ((1 - dayExpend / dayBudget) * 100.0).toInt()
                tvBudgetDesc.text = context.getString(R.string.surplus_day_budget)
            }
            else -> {
                monthBudget.toString()
            }
        }
    }
}