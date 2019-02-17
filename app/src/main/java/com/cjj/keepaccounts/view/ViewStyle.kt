package com.cjj.keepaccounts.view

import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.utils.Utils
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColorResource

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/19 11:33.
 */
object ViewStyle {

    val item_layout_style = { layout: FrameLayout ->
        layout.backgroundResource = R.drawable.ripple_bg_click_white
        layout.setPadding(Utils.getDimension(R.dimen.start), 0, Utils.getDimension(R.dimen.end), 0)
        layout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.getDimension(R.dimen.add_account_item_height))
    }

    val item_key_style = { v: TextView ->
        v.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getDimension(R.dimen.text_size14).toFloat())
        v.gravity = Gravity.START or Gravity.CENTER_VERTICAL
    }

    val item_value_style = { v: TextView ->
        v.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getDimension(R.dimen.text_size14).toFloat())
        v.gravity = Gravity.END or Gravity.CENTER_VERTICAL
        v.setCompoundDrawables(null, null, Utils.getDrawable(R.mipmap.youjiantou_baobiao), null)
        v.compoundDrawablePadding = Utils.dip2px(10F)
        v.textColorResource = R.color.text_color_a19e9d
        v.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getDimension(R.dimen.text_size14).toFloat())
    }
}