package com.cjj.keepaccounts.adapter

import android.support.v7.widget.AppCompatImageView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.base.viewholder.ClickViewHolder
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.manager.LogoManager
import com.cjj.keepaccounts.utils.Utils
import org.jetbrains.anko.textColorResource

/**
 * @author CJJ
 * Created by CJJ on 2018/7/2 10:36.
 */
class SearchRecordAdapter : HeaderAndFooterAdapter<SearchRecordAdapter.ViewHolder, Record>() {
    class ViewHolder(itemView: View) : BaseViewHolder(itemView), ClickViewHolder {
        @BindView(R.id.iv_logo)
        lateinit var ivLogo: AppCompatImageView
        @BindView(R.id.tv_desc)
        lateinit var tvDesc: TextView
        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView
        @BindView(R.id.tv_money)
        lateinit var tvMoney: TextView
    }


    override fun onCreateContentViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_search_record)
        return ViewHolder(view)
    }

    override fun onBindContentViewHolder(holder: ViewHolder, position: Int) {
        val record = getItem(position)
        holder.ivLogo.setImageResource(LogoManager.getTypeLogo(record.recordType.imgSrcId))
        val desc = "${record.recordType.typeDesc}  ${record.content}"
        holder.tvDesc.text = desc
        holder.tvTime.text = Utils.context.getString(R.string.yyyy_MM_dd, record.year, record.month + 1, record.day)
        val isIncoming = record.recordType.isIncoming
        if (isIncoming == 1 || (isIncoming == -1 && record.rateMoney > 0)) {
            holder.tvMoney.textColorResource = R.color.AppThemeColor
            val str = "+" + Utils.context.getString(R.string.format_money, record.rateMoney)
            holder.tvMoney.text = str
        } else {
            holder.tvMoney.textColorResource = R.color.text_color_655f5f
            val str = "-" + Utils.context.getString(R.string.format_money, Math.abs(record.rateMoney))
            holder.tvMoney.text = str
        }
    }


}