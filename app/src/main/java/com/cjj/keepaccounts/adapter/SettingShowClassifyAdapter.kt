package com.cjj.keepaccounts.adapter

import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.CheckBox
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.databinding.typeLogo
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.drawableEnd
import com.cjj.keepaccounts.view.ripple
import org.jetbrains.anko.horizontalPadding
import org.jetbrains.anko.textColorResource


/**
 * @author CJJ
 * Created by CJJ on 2018/5/21 10:44.
 */
class SettingShowClassifyAdapter : BaseRecyclerViewAdapter<SettingShowClassifyAdapter.ViewHolder, RecordType>() {
    private val dia = Utils.dip2px(30)

    class ViewHolder(itemView: CheckBox) : RecyclerView.ViewHolder(itemView) {
        val cbShow: CheckBox = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val checkBox = CheckBox(parent.context)
        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getDimension(R.dimen.text_size15).toFloat())
        checkBox.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Utils.dip2px(55))
        checkBox.textColorResource = R.color.text_color_655f5f
        checkBox.gravity = Gravity.CENTER_VERTICAL
        checkBox.horizontalPadding = Utils.getDimension(R.dimen.start)
        checkBox.compoundDrawablePadding = Utils.getDimension(R.dimen.start)
        checkBox.buttonDrawable = null
        checkBox.drawableEnd = Utils.getDrawable(R.drawable.selector_show_classify_bg, Utils.dip2px(24))
        checkBox.ripple()
        return ViewHolder(checkBox)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.cbShow.setOnCheckedChangeListener(null)
        holder.cbShow.typeLogo(item.imgSrcId, dia)
        holder.cbShow.text = item.typeDesc
        holder.cbShow.isChecked = item.isCheck
        holder.cbShow.setOnCheckedChangeListener { _, isChecked ->
            item.isCheck = isChecked
        }
    }
}