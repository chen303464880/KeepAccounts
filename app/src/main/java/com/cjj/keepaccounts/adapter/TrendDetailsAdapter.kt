package com.cjj.keepaccounts.adapter


import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import androidx.core.text.inSpans
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.viewholder.ClickViewHolder
import com.cjj.keepaccounts.base.viewholder.DataBindingViewHolder
import com.cjj.keepaccounts.bean.TrendDetailsBean
import com.cjj.keepaccounts.databinding.ListItemTrendDetailsBinding
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.Utils

/**
 * @author CJJ
 * Created by CJJ on 2018/5/15 12:00.
 */
class TrendDetailsAdapter : HeaderAndFooterAdapter<TrendDetailsAdapter.ViewHolder, TrendDetailsBean>() {
    private val sizeSpan = AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size08))
    private val colorSpan = ForegroundColorSpan(Utils.getColor(R.color.text_color_a39f9f))

    class ViewHolder(dataBinding: ListItemTrendDetailsBinding) : DataBindingViewHolder<ListItemTrendDetailsBinding>(dataBinding), ClickViewHolder

    override fun onCreateContentViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(Utils.inflateDataBindingItem(parent, R.layout.list_item_trend_details))
    }

    override fun onBindContentViewHolder(holder: TrendDetailsAdapter.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        holder.dataBinding.trendDetails = item
        if (position != itemCount - 1 - getHeaderViewCount()) {
            val spanBuilder = SpannableStringBuilder(Utils.context.getString(R.string.xx_month, item.month + 1))
            spanBuilder.append("\n")
            spanBuilder.inSpans(colorSpan, sizeSpan) {
                val day = TimeUtils.getMonthMax(item.year, item.month)
                append(Utils.context.getString(R.string.date_interval, item.month + 1, 1, item.month + 1, day))
            }
            holder.dataBinding.tvDate.text = spanBuilder
        } else {
            holder.dataBinding.tvDate.text = Utils.getString(R.string.total)
        }

    }
}