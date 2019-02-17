package com.cjj.keepaccounts.adapter

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.databinding.typeLogo
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.dao.RecordTypeTool
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.drawableEnd
import com.cjj.keepaccounts.view.ripple
import org.jetbrains.anko.horizontalPadding
import org.jetbrains.anko.textColorResource
import org.jetbrains.anko.textSizeDimen

/**
 * @author CJJ
 * Created by CJJ on 2018/7/5 14:46.
 */
class BudgetChildClassifyAdapter : BaseRecyclerViewAdapter<BudgetChildClassifyAdapter.ViewHolder, RecordType>() {

    var budgetTypeId = 0L
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private val dia = Utils.dip2px(25)

    class ViewHolder(itemView: TextView) : BaseViewHolder(itemView) {
        val textView = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = TextView(parent.context)
        view.gravity = Gravity.CENTER_VERTICAL
        view.textColorResource = R.color.text_color_655f5f
        view.horizontalPadding = Utils.getDimension(R.dimen.start)
        view.compoundDrawablePadding = Utils.dip2px(10)
        view.textSizeDimen = R.dimen.text_size14
        view.ripple()
        view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Utils.dip2px(44))
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recordType = getItem(position)
        holder.textView.typeLogo(recordType.imgSrcId, dia)
        holder.textView.drawableEnd = Utils.getDrawable(if (recordType.budgetType == budgetTypeId) R.mipmap.ic_check_true else R.mipmap.ic_check_false)
        holder.textView.text = recordType.typeDesc
        holder.textView.setOnClickListener {
            val newRecordType = recordType.clone()
            newRecordType.budgetType = if (recordType.budgetType == budgetTypeId) 0 else budgetTypeId
            RecordTypeTool.update(recordType, newRecordType, true)
        }
    }
}