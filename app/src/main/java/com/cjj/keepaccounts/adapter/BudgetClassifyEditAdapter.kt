package com.cjj.keepaccounts.adapter

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.manager.LogoManager
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.drawableTop
import org.jetbrains.anko.dip

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/1 16:35.
 */
class BudgetClassifyEditAdapter : BaseRecyclerViewAdapter<BudgetClassifyEditAdapter.ViewHolder, RecordType>() {

    //图标的直径
    private val diameter = Utils.dip2px(34F)

    class ViewHolder(itemView: TextView) : BaseViewHolder(itemView) {
        val textView: TextView = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = TextView(parent.context)
        textView.apply {
            setTextColor(Utils.getColor(R.color.text_color_655f5f))
            gravity = Gravity.CENTER_HORIZONTAL
            background = Utils.getDrawable(R.drawable.ripple_bg_click_transparency)
            compoundDrawablePadding = Utils.dip2px(8F)
            layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
            setPadding(0, dip(10), 0, 0)
        }

        return BudgetClassifyEditAdapter.ViewHolder(textView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val recordType = getItem(position)
        holder.textView.text = recordType.typeDesc
        holder.textView.drawableTop = Utils.getDrawable(LogoManager.getTypeLogo(recordType.imgSrcId), diameter)
    }
}