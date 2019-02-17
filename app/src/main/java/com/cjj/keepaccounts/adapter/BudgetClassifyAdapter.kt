package com.cjj.keepaccounts.adapter

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.base.viewholder.ClickViewHolder
import com.cjj.keepaccounts.bean.SameCityMainType
import com.cjj.keepaccounts.manager.LogoManager
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.drawableTop
import com.cjj.keepaccounts.view.ripple
import org.jetbrains.anko.textColorResource
import org.jetbrains.anko.textSizeDimen
import org.jetbrains.anko.topPadding

/**
 * @author CJJ
 * Created by CJJ on 2018/7/5 14:46.
 */
class BudgetClassifyAdapter : BaseRecyclerViewAdapter<BudgetClassifyAdapter.ViewHolder, SameCityMainType>() {

    private var checkPosition = 0
    private val dia = Utils.dip2px(33)

    private var changeListener: ((item: SameCityMainType) -> Unit)? = null

    fun setOnChangeListener(listener: (item: SameCityMainType) -> Unit) {
        changeListener = listener
    }

    class ViewHolder(itemView: TextView) : BaseViewHolder(itemView), ClickViewHolder {
        val textView = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = TextView(parent.context)
        view.gravity = Gravity.CENTER_HORIZONTAL
        view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Utils.dip2px(90))
        view.textColorResource = R.color.text_color_707070
        view.topPadding = Utils.dip2px(20)
        view.textSizeDimen = R.dimen.text_size12
        view.compoundDrawablePadding = Utils.dip2px(10)
        view.ripple()
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mainType = getItem(position)
        if (checkPosition == position) {
            holder.textView.background = Utils.getDrawable(R.drawable.ripple_bg_click_white)
        } else {
            holder.textView.ripple()
        }
        holder.textView.drawableTop = Utils.getDrawable(LogoManager.getTypeLogo(mainType.typeIconId), dia)
        holder.textView.text = mainType.typeName
        holder.textView.setOnClickListener {
            refreshChecked(position)
        }
    }

    fun refreshChecked(position: Int) {
        if (position >= itemCount) {
            return
        }
        if (checkPosition != position) {
            val tempCheckPosition = checkPosition
            checkPosition = position
            notifyItemChanged(position)
            if (tempCheckPosition >= 0) {
                notifyItemChanged(tempCheckPosition)
            }
            changeListener?.invoke(getItem(position))
        }
    }
}