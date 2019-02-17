package com.cjj.keepaccounts.adapter

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.annotation.ColorInt
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.base.viewholder.ClickViewHolder
import com.cjj.keepaccounts.bean.SelectColorBean
import com.cjj.keepaccounts.utils.Utils

/**
 * @author chenjunjie
 * Created by CJJ on 2017/12/14 16:31.
 */
class SelectColorAdapter : BaseRecyclerViewAdapter<SelectColorAdapter.ViewHolder, SelectColorBean>() {

    private var checkPosition = 0

    class ViewHolder(itemView: View) : BaseViewHolder(itemView), ClickViewHolder {
        @BindView(R.id.iv_select_color)
        lateinit var ivSelectColor: ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_select_color)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.ivSelectColor.background = getAccountColor(item.color)
        if (item.check) {
            checkPosition = holder.adapterPosition
            holder.ivSelectColor.setImageResource(R.mipmap.icon_account_color_gou)
        } else {
            holder.ivSelectColor.setImageResource(android.R.color.transparent)
        }

        holder.ivSelectColor.setOnClickListener {
            itemClickListener?.invoke(holder, holder.adapterPosition, item)
            refreshCheck(position)
        }
    }

    private fun refreshCheck(position: Int) {
        if (position != checkPosition) {
            getItem(checkPosition).check = false
            getItem(position).check = true
            notifyItemChanged(checkPosition)
            notifyItemChanged(position)
        }
    }


    private fun getAccountColor(@ColorInt colorInt: Int): Drawable {
        val drawable = GradientDrawable()
        drawable.setColor(colorInt)
        drawable.cornerRadius = Utils.dip2px(2F).toFloat()
        val length = Utils.dip2px(20F)
        drawable.setSize(length, length)
        return drawable
    }
}