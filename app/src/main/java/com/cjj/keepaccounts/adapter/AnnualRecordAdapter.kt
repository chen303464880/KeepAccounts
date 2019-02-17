package com.cjj.keepaccounts.adapter

import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.base.viewholder.ClickViewHolder
import com.cjj.keepaccounts.bean.AnnalRecordMonthBean
import com.cjj.keepaccounts.enum.ListViewType
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.AnnalRecordMonthView
import org.jetbrains.anko.backgroundResource

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/26 16:13.
 */
class AnnualRecordAdapter : BaseRecyclerViewAdapter<RecyclerView.ViewHolder, AnnalRecordMonthBean>() {
    class ViewHolder(itemView: AnnalRecordMonthView) : BaseViewHolder(itemView), ClickViewHolder {
        val monthView = itemView
    }

    class NodeViewHolder(itemView: TextView) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView
    }

    override fun getItemViewType(position: Int): Int = if (getItem(position).isNode) ListViewType.TYPE_INTERVAL.type else ListViewType.TYPE_CONTENT.type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ListViewType.TYPE_INTERVAL.type) {
            val textView = TextView(parent.context)
            textView.setPadding(0, Utils.dip2px(25F), 0, Utils.dip2px(8F))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getDimension(R.dimen.text_size27).toFloat())
            textView.backgroundResource = R.drawable.shape_bottom_e7_line
            NodeViewHolder(textView)
        } else {
            val view = AnnalRecordMonthView(parent.context)
            view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
            ViewHolder(view)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val monthBean = getItem(position)
        when (holder) {
            is NodeViewHolder -> {
                holder.textView.text = Utils.context.getString(R.string.xx_year, monthBean.year)
            }
            is ViewHolder -> {
                holder.monthView.month = monthBean
            }
        }

    }
}