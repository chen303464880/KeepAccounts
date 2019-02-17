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
import com.cjj.keepaccounts.adapter.databinding.typeLogo
import com.cjj.keepaccounts.base.BaseDeleteAdapter
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.utils.toMoney


/**
 * @author chenjunjie
 * Created by CJJ on 2018/1/26 15:23.
 */
class BorrowMoneyDetailsAdapter : HeaderAndFooterAdapter<BorrowMoneyDetailsAdapter.ViewHolder, Record>() {

    class ViewHolder(itemView: View) : BaseDeleteAdapter.DeleteViewHolder(itemView) {
        @BindView(R.id.fl_content)
        lateinit var flContent: FrameLayout
        @BindView(R.id.tv_name)
        lateinit var tvName: TextView
        @BindView(R.id.tv_money)
        lateinit var tvMoney: TextView
    }


    override fun onCreateContentViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_borrow_money)
        return ViewHolder(view)
    }

    override fun onBindContentViewHolder(holder: BorrowMoneyDetailsAdapter.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)

        val recordType = item.recordType
        val str = "${recordType.typeDesc}\n${TimeUtils.longTurnTime(item.rTime, Utils.getString(R.string.yyyy_MM_dd_format))}"
        val spannableString = SpannableString(str)
        val start = str.indexOf("\n")
        spannableString.setSpan(ForegroundColorSpan(Utils.getColor(R.color.text_color_9e9b9b)), start, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12)), start, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        holder.tvName.text = spannableString
        holder.tvName.typeLogo(recordType.imgSrcId, Utils.dip2px(30F))
        holder.tvMoney.text = item.rateMoney.toMoney()

    }

}