package com.cjj.keepaccounts.adapter

import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.base.viewholder.ClickViewHolder
import com.cjj.keepaccounts.bean.BankInfoBean
import com.cjj.keepaccounts.enum.ListViewType
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.drawableEnd
import com.cjj.keepaccounts.view.drawableStart
import com.cjj.keepaccounts.view.ripple
import org.jetbrains.anko.textColorResource

/**
 * Created by CJJ on 2018/4/10 21:46.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class SelectBackAdapter : BaseRecyclerViewAdapter<BaseViewHolder, BankInfoBean>() {


    class IntervalViewHolder(itemView: View) : BaseViewHolder(itemView) {
        @BindView(R.id.tv_interval)
        lateinit var interval: TextView
    }

    class ContentViewHolder(itemView: TextView) : BaseViewHolder(itemView), ClickViewHolder {
        val textView = itemView
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isNode) {
            ListViewType.TYPE_INTERVAL.type
        } else {
            ListViewType.TYPE_CONTENT.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
            if (viewType == ListViewType.TYPE_INTERVAL.type) {
                val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_text_interval)
                SelectBackAdapter.IntervalViewHolder(view)
            } else {
                val view = TextView(parent.context)
                view.apply {
                    compoundDrawablePadding = Utils.dip2px(16F)
                    setPadding(Utils.getDimension(R.dimen.start), 0, Utils.dip2px(20F), 0)
                    layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Utils.getDimension(R.dimen.select_bank_item_height))
                    gravity = Gravity.CENTER_VERTICAL
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getDimension(R.dimen.text_size15).toFloat())
                    textColorResource = R.color.text_color_655f5f
                    ripple()
                    drawableEnd = Utils.getDrawable(R.mipmap.youjiantou_baobiao)
                }
                SelectBackAdapter.ContentViewHolder(view)
            }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        when (holder) {
            is ContentViewHolder -> {
                holder.textView.drawableStart = Utils.getDrawable(item.logoRes, Utils.dip2px(30))
                holder.textView.text = item.backName
            }
            is IntervalViewHolder -> {
                holder.interval.text = item.nodeName.toString()
            }
            else -> {
            }
        }
    }
}