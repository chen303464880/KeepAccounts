package com.cjj.keepaccounts.adapter

import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.statement.SettingShowClassifyActivity
import com.cjj.keepaccounts.adapter.databinding.typeLogo
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.base.viewholder.ClickViewHolder
import com.cjj.keepaccounts.bean.ContrastItemBean
import com.cjj.keepaccounts.enum.ListViewType
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.ContrastView
import org.jetbrains.anko.backgroundResource

/**
 * Created by CJJ on 2018/5/17 21:13.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class ContrastAdapter : BaseRecyclerViewAdapter<RecyclerView.ViewHolder, ContrastItemBean>() {

    private val logoDia = Utils.dip2px(26)
    private val colorSpan = ForegroundColorSpan(Utils.getColor(R.color.text_color_a39f9f))
    private val sizeSpan = AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size10))

    class ViewHolder(itemView: View) : BaseViewHolder(itemView), ClickViewHolder {
        @BindView(R.id.tv_desc)
        lateinit var tvDesc: TextView
        @BindView(R.id.tv_count)
        lateinit var tvCount: TextView
        @BindView(R.id.tv_money)
        lateinit var tvMoney: TextView
        @BindView(R.id.tv_average)
        lateinit var tvAverage: TextView
        @BindView(R.id.contrast_view)
        lateinit var contrastView: ContrastView
    }

    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        return if (position != super.getItemCount()) ListViewType.TYPE_CONTENT.type else ListViewType.TYPE_FOOTER.type
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ListViewType.TYPE_CONTENT.type) {
            ViewHolder(Utils.inflateRecyclerViewItem(parent, R.layout.list_item_contrast_content))
        } else {
            FooterViewHolder(Utils.inflateRecyclerViewItem(parent, R.layout.list_item_contrast_footer))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position)

                val count = SpannableStringBuilder(Utils.context.getString(R.string.month_expend_count, item.monthCount))
                count.setSpan(colorSpan, count.length - 6, count.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                count.setSpan(sizeSpan, count.length - 6, count.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                val money = SpannableStringBuilder(Utils.context.getString(R.string.month_expend_money, item.totalMoney))
                money.setSpan(colorSpan, money.length - 6, money.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                money.setSpan(sizeSpan, money.length - 6, money.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                val average = SpannableStringBuilder(Utils.context.getString(R.string.month_average_money, item.average))
                average.setSpan(colorSpan, average.length - 4, average.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                average.setSpan(sizeSpan, average.length - 4, average.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                holder.apply {
                    tvDesc.text = item.desc
                    tvDesc.typeLogo(item.imgId, logoDia)
                    tvCount.text = count
                    tvMoney.text = money
                    tvMoney.setTextColor(item.color)
                    tvAverage.text = average

                    contrastView.list = item.monthInfo
                    contrastView.setBackgroundColor(item.color)
                    contrastView.selectMonth = TimeUtils.month


                    if (position != super.getItemCount() - 1) {
                        itemView.backgroundResource = R.drawable.shape_bottom_e7_line
                    } else {
                        itemView.background = null
                    }
                }
            }
            is FooterViewHolder -> {
                holder.itemView.setOnClickListener {
                    ActivityTool.skipActivity<SettingShowClassifyActivity>()
                }
            }
        }

    }
}