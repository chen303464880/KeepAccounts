package com.cjj.keepaccounts.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

/**
 * @author CJJ
 * Created by CJJ on 2018/5/29 16:17.
 */
open class BaseView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun dip2px(dip: Float): Int {
        return Math.round(dip * context.resources.displayMetrics.density)
    }

    fun dip2px(dip: Int): Int {
        return Math.round(dip * context.resources.displayMetrics.density)
    }

    fun getColor(@ColorRes colorId: Int): Int {
        return ContextCompat.getColor(context, colorId)
    }

    fun getDrawable(@DrawableRes drawableId: Int): Drawable {
        val drawable = ContextCompat.getDrawable(context, drawableId)!!
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        return drawable
    }

    fun getDimension(@DimenRes dimenId: Int): Int {
        return Math.round(context.resources.getDimension(dimenId))
    }

}