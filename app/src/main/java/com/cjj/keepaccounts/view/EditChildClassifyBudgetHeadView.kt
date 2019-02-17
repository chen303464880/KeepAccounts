package com.cjj.keepaccounts.view

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.bean.SameCityMainType
import com.cjj.keepaccounts.manager.LogoManager
import com.cjj.keepaccounts.utils.MoneyUtils
import com.cjj.keepaccounts.utils.Utils
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColorResource

/**
 * Created by CJJ on 2018/7/9 19:27.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class EditChildClassifyBudgetHeadView(val context: Context) {
    val headView: View = Utils.inflate(R.layout.list_view_edit_budget_child_classify, context)

    private val ivLogo: AppCompatImageView = headView.findViewById(R.id.iv_logo)

    private val tvType: TextView = headView.findViewById(R.id.tv_type)

    private val pbBudget: ProgressBar = headView.findViewById(R.id.pb_budget)

    private val tvBudget: TextView = headView.findViewById(R.id.tv_budget)

    private val tvSurplus: TextView = headView.findViewById(R.id.tv_surplus)

    private val tvSurplusDesc: TextView = headView.findViewById(R.id.tv_surplus_desc)

    private val ivMore: View = headView.findViewById(R.id.iv_more)


    init {
        headView.backgroundResource = R.color.AppThemeColor
        headView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Utils.dip2px(90))
        tvType.textColorResource = R.color.white
        pbBudget.progressDrawable = Utils.getDrawable(R.drawable.shape_progress_bar_group_budget_drawable)
        tvBudget.textColorResource = R.color.transparent_white
        tvSurplus.textColorResource = R.color.white
        tvSurplusDesc.textColorResource = R.color.transparent_white
        ivMore.visibility = View.INVISIBLE
    }

    fun setData(sameCityMainType: SameCityMainType) {
        ivLogo.backgroundResource = LogoManager.getTypeLogo(sameCityMainType.typeIconId)
        tvType.text = sameCityMainType.typeName
        if (sameCityMainType.budget.amount != 0.0) {
            tvBudget.text = Utils.context.getString(R.string.month_budget_, sameCityMainType.budget.amount)
        } else {
            tvBudget.text = Utils.context.getString(R.string.click_setting_month_budget)
        }
        if (sameCityMainType.recordTypes.isEmpty()) {
            pbBudget.progress = 0
            tvSurplus.visibility = View.GONE
            tvSurplusDesc.visibility = View.GONE
        } else if (sameCityMainType.budget.amount == 0.0) {
            pbBudget.progress = 0
            tvSurplus.visibility = View.GONE
            tvSurplusDesc.visibility = View.GONE
        } else {
            tvSurplus.visibility = View.VISIBLE
            tvSurplusDesc.visibility = View.VISIBLE
            if (pbBudget.width == 0) {//view未绘制完成
                pbBudget.doOnPreDraw {
                    pbBudget.max = it.width
                    setProcess(sameCityMainType)
                }
            } else {
                setProcess(sameCityMainType)
            }
        }

    }

    private fun setProcess(item: SameCityMainType) {
        var progress = (item.expend.toFloat() / item.budget.amount.toFloat() * pbBudget.width).toInt()
        if (progress != 0 && progress < pbBudget.height) {
            progress = pbBudget.height
        }
        if (progress >= pbBudget.width) {
            pbBudget.progressDrawable = Utils.getDrawable(R.drawable.shape_progress_bar_red_budget_drawable)
        } else {
            pbBudget.progressDrawable = Utils.getDrawable(R.drawable.shape_progress_bar_group_budget_drawable)
        }
        pbBudget.progress = progress
        val surplus = item.budget.amount - item.expend
        tvSurplus.text = MoneyUtils.formatMoney(Math.abs(surplus))
        tvSurplusDesc.text = Utils.getString(if (surplus > 0) R.string.surplus_ else R.string.overspend)
    }
}