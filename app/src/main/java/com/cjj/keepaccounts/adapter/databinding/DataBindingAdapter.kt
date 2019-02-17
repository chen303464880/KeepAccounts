package com.cjj.keepaccounts.adapter.databinding

import android.databinding.BindingAdapter
import android.widget.TextView
import com.cjj.keepaccounts.manager.LogoManager
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.MemberDrawable
import com.cjj.keepaccounts.view.drawableStart

/**
 * @author CJJ
 * Created by CJJ on 2018/11/12 14:35.
 */
object DataBindingAdapter {

    @BindingAdapter(value = ["longTime", "format"], requireAll = false)
    @JvmStatic
    fun TextView.textViewFormatTime(longTime: Long?, format: String?) {
        if (longTime != null) {
            text = if (format.isNullOrEmpty()) {
                TimeUtils.longTurnTime(longTime)
            } else {
                TimeUtils.longTurnTime(longTime, format)
            }
        }

    }

}

@BindingAdapter(value = ["logoImgId", "logoImgSize", "memberDesc", "memberColor"], requireAll = false)
fun TextView.typeLogo(logoImg: Int?, logoImgSize: Int? = null, memberDesc: String? = null, memberColor: Int? = null) {
    if (logoImg != null && logoImg != 0) {
        drawableStart = if (logoImgSize != null) {
            Utils.getDrawable(LogoManager.getTypeLogo(logoImg), logoImgSize)
        } else {
            Utils.getDrawable(LogoManager.getTypeLogo(logoImg))
        }
    } else if (memberDesc != null && memberDesc.isNotEmpty() && memberColor != null && logoImgSize != null) {
        val drawable = MemberDrawable(memberDesc[0].toString(), memberColor)
        drawable.setBounds(0, 0, logoImgSize, logoImgSize)
        drawableStart = drawable
    }
}