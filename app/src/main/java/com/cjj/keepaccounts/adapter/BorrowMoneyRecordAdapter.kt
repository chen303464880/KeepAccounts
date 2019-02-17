package com.cjj.keepaccounts.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseDeleteAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.base.viewholder.DataBindingViewHolder
import com.cjj.keepaccounts.bean.Credit
import com.cjj.keepaccounts.databinding.ListItemTextIntervalBinding
import com.cjj.keepaccounts.enum.ListViewType
import com.cjj.keepaccounts.utils.MoneyUtils
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.drawableEnd
import com.cjj.keepaccounts.view.drawableStart

/**
 * @author chenjunjie
 * Created by CJJ on 2017/12/22 11:48.
 */

class BorrowMoneyRecordAdapter : BaseDeleteAdapter<BaseViewHolder, Credit>() {
    private val size = Utils.dip2px(30F)

    private val startDrawable = arrayOf(Utils.getDrawable(R.mipmap.icon_shouru_type_jieru, size), Utils.getDrawable(R.mipmap.icon_zhichu_type_jiechu, size))
    private val endDrawable = arrayOf(Utils.getDrawable(R.mipmap.ic_a19e9d_more), Utils.getDrawable(R.mipmap.ic_655f5f_more))

    class IntervalViewHolder(dataBinding: ListItemTextIntervalBinding) : DataBindingViewHolder<ListItemTextIntervalBinding>(dataBinding)

    class ContentViewHolder(itemView: View) : DeleteViewHolder(itemView) {
        @BindView(R.id.tv_name)
        lateinit var tvName: TextView
        @BindView(R.id.tv_money)
        lateinit var tvMoney: TextView
        @BindView(R.id.fl_content)
        lateinit var flContent: FrameLayout
    }

    override fun getItemViewType(position: Int): Int = if (getItem(position).isNode) ListViewType.TYPE_INTERVAL.type else ListViewType.TYPE_CONTENT.type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == ListViewType.TYPE_INTERVAL.type) {
//            val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_text_interval)


            IntervalViewHolder(Utils.inflateDataBindingItem(parent, R.layout.list_item_text_interval))
        } else {
            val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_borrow_money)
            ContentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        when (holder) {
            is IntervalViewHolder -> {
                holder.dataBinding.time = item.mTime
            }
            is ContentViewHolder -> {
                //判断是否还清
                val isRefund = item.repayMoney == item.money

                holder.tvMoney.text = MoneyUtils.formatMoney(item.money)
                //根据是否还清,来显示文字的颜色
                if (isRefund) {
                    holder.tvMoney.setTextColor(Utils.getColor(R.color.text_color_a19e9d))
                    holder.tvMoney.drawableEnd = endDrawable[0]
                } else {
                    holder.tvMoney.setTextColor(Utils.getColor(R.color.text_color_655f5f))
                    holder.tvMoney.drawableEnd = endDrawable[1]
                }

                var desc = ""
                if (item.settlementTime != 0L) {
                    desc = TimeUtils.longTurnTime(item.settlementTime * 1000L, "yyyy年MM月dd日到期")
                }
                if (!item.content.isNullOrEmpty()) {
                    desc += " " + item.content
                }
                if (desc.isEmpty()) {
                    holder.tvName.text = item.dcUName
                } else {
                    val spannableString = SpannableString("${item.dcUName}\n$desc")
                    spannableString.setSpan(ForegroundColorSpan(Utils.getColor(R.color.text_color_9e9b9b)), item.dcUName.length + 1, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    spannableString.setSpan(AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12)), item.dcUName.length + 1, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    holder.tvName.text = spannableString
                }
                //设置借出的图标
                holder.tvName.drawableStart = startDrawable[if (isRefund) 0 else 1]
            }
        }
    }
}