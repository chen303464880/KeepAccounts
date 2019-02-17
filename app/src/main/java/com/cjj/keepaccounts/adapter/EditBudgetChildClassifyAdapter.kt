package com.cjj.keepaccounts.adapter

import android.support.v7.widget.AppCompatImageView
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.base.viewholder.ClickViewHolder
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.manager.LogoManager
import com.cjj.keepaccounts.utils.MoneyUtils
import com.cjj.keepaccounts.utils.Utils
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColorResource

/**
 * @author CJJ
 * Created by CJJ on 2018/7/4 15:48.
 */
class EditBudgetChildClassifyAdapter : HeaderAndFooterAdapter<EditBudgetChildClassifyAdapter.ViewHolder, RecordType>() {
    class ViewHolder(itemView: View) : BaseViewHolder(itemView), ClickViewHolder {
        @BindView(R.id.iv_logo)
        lateinit var ivLogo: AppCompatImageView
        @BindView(R.id.tv_type)
        lateinit var tvType: TextView
        @BindView(R.id.pb_budget)
        lateinit var pbBudget: ProgressBar
        @BindView(R.id.tv_budget)
        lateinit var tvBudget: TextView
        @BindView(R.id.tv_surplus)
        lateinit var tvSurplus: TextView
        @BindView(R.id.tv_surplus_desc)
        lateinit var tvSurplusDesc: TextView
    }

    override fun onCreateContentViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(Utils.inflateRecyclerViewItem(parent, R.layout.list_view_edit_budget_child_classify))
    }

    override fun onBindContentViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.ivLogo.backgroundResource = LogoManager.getTypeLogo(item.imgSrcId)
        holder.tvType.text = item.typeDesc
        if (item.budgetAmount != 0F) {
            holder.tvBudget.text = Utils.context.getString(R.string.month_budget_, item.budgetAmount)
        } else {
            holder.tvBudget.text = Utils.context.getString(R.string.click_setting_month_budget)
        }
        if (item.budgetAmount == 0F) {
            holder.pbBudget.progress = 0
            holder.tvSurplus.visibility = View.GONE
            holder.tvSurplusDesc.visibility = View.GONE
        } else {
            holder.tvSurplus.visibility = View.VISIBLE
            holder.tvSurplusDesc.visibility = View.VISIBLE
            if (holder.pbBudget.width == 0) {//view未绘制完成
                holder.pbBudget.doOnPreDraw {
                    holder.pbBudget.max = it.width
                    setProcess(item, holder)
                }
            } else {
                setProcess(item, holder)
            }
        }
    }

    private fun setProcess(item: RecordType, holder: ViewHolder) {
        var progress = (item.moneyExpend.toFloat() / item.budgetAmount * holder.pbBudget.width).toInt()
        if (progress != 0 && progress < holder.pbBudget.height) {
            progress = holder.pbBudget.height
        }
        holder.pbBudget.progress = progress
        val surplus = item.budgetAmount - item.moneyExpend
        holder.tvSurplus.text = MoneyUtils.formatMoney(Math.abs(surplus))
        if (surplus >= 0.0) {
            holder.pbBudget.progressDrawable = Utils.getDrawable(R.drawable.shape_progress_bar_budget_drawable)
            holder.tvSurplus.textColorResource = R.color.text_color_655f5f
            holder.tvSurplusDesc.text = Utils.getString(R.string.overspend)
        } else {
            holder.pbBudget.progressDrawable = Utils.getDrawable(R.drawable.shape_progress_bar_red_budget_drawable)
            holder.tvSurplus.textColorResource = R.color.text_color_ff6b6b
            holder.tvSurplusDesc.text = Utils.getString(R.string.surplus_)
        }
    }
}