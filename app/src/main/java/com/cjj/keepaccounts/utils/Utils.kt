package com.cjj.keepaccounts.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import com.cjj.keepaccounts.BuildConfig
import com.cjj.keepaccounts.application.MyApplication
import org.jetbrains.anko.layoutInflater
import rx.Subscription


/**
 * Created by CJJ on 2017/11/10 10:22.
 * Copyright © 2015-2017 CJJ All rights reserved.
 */
@SuppressWarnings("all")
object Utils {
    val context: Context
    @JvmStatic
    val resources: Resources
    private var SCALE: Float

    @JvmStatic
    val widthPixels: Int
        get() = resources.displayMetrics.widthPixels
    @JvmStatic
    val heightPixels: Int
        get() = resources.displayMetrics.heightPixels

    init {
        context = MyApplication.CONTEXT
        resources = context.resources
        SCALE = resources.displayMetrics.density
    }

    @JvmStatic
    fun getDrawable(@DrawableRes drawableId: Int): Drawable {
        val drawable = ContextCompat.getDrawable(context, drawableId)!!
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        return drawable
    }

    @JvmStatic
    fun getDrawable(@DrawableRes drawableId: Int, size: Int): Drawable {
        val drawable = ContextCompat.getDrawable(context, drawableId)!!
        drawable.setBounds(0, 0, size, size)
        return drawable
    }

    @JvmStatic
    fun getDrawable(@DrawableRes drawableId: Int, width: Int, height: Int): Drawable {
        val drawable = ContextCompat.getDrawable(context, drawableId)!!
        drawable.setBounds(0, 0, width, height)
        return drawable
    }

    @JvmStatic
    fun getColor(@ColorRes colorId: Int): Int {
        return ContextCompat.getColor(context, colorId)
    }

    @JvmStatic
    fun getDimension(@DimenRes dimenId: Int): Int {
        return Math.round(resources.getDimension(dimenId))
    }

    @JvmStatic
    fun getString(@StringRes stringId: Int): String {
        return resources.getString(stringId)
    }

    @JvmStatic
    fun getString(@StringRes stringId: Int, vararg formatArgs: String): String {
        return resources.getString(stringId, formatArgs)
    }

    @JvmStatic
    fun dip2px(dip: Float): Int {
        return Math.round(dip * SCALE)
    }

    @JvmStatic
    fun dip2px(dip: Int): Int {
        return Math.round(dip * SCALE)
    }

    @JvmStatic
    fun px2dip(px: Int): Int {
        return Math.round(px / SCALE)
    }

    @JvmStatic
    fun inflate(@LayoutRes layoutId: Int, context: Context): View {
        return View.inflate(context, layoutId, null)
    }

    @JvmStatic
    fun <T : ViewDataBinding> inflateDataBinding(@LayoutRes layoutId: Int, context: Context): T {
        return DataBindingUtil.inflate(context.layoutInflater, layoutId, null, false)
    }

    @JvmStatic
    fun inflateRecyclerViewItem(viewGroup: ViewGroup, @LayoutRes layoutId: Int): View {
        return LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)
    }

    @JvmStatic
    fun <T : ViewDataBinding> inflateDataBindingItem(viewGroup: ViewGroup, @LayoutRes layoutId: Int): T {
        return DataBindingUtil.inflate(viewGroup.context.layoutInflater, layoutId, viewGroup, false)
    }

    @JvmStatic
    fun evaluate(fraction: Float, startValue: Float, endValue: Float): Float {
        return startValue + fraction * (endValue - startValue)
    }

    /**
     * 输入两个点的坐标,确定两个点的角度
     *
     * @return 点2到点1与x轴组成的夹角
     */
    @JvmStatic
    fun computingAngle(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val a = x2 - x1
        val b = y2 - y1
        return Math.toDegrees(Math.atan2(b.toDouble(), a.toDouble())).toFloat()
    }

    /**
     * 颜色变化过度
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    @JvmStatic
    fun evaluateColor(fraction: Float, startValue: Any, endValue: Any): Int {
        val startInt = startValue as Int
        val startA = (startInt shr 24 and 0xff) / 255.0f
        var startR = (startInt shr 16 and 0xff) / 255.0f
        var startG = (startInt shr 8 and 0xff) / 255.0f
        var startB = (startInt and 0xff) / 255.0f

        val endInt = endValue as Int
        val endA = (endInt shr 24 and 0xff) / 255.0f
        var endR = (endInt shr 16 and 0xff) / 255.0f
        var endG = (endInt shr 8 and 0xff) / 255.0f
        var endB = (endInt and 0xff) / 255.0f

        // convert from sRGB to linear
        startR = Math.pow(startR.toDouble(), 2.2).toFloat()
        startG = Math.pow(startG.toDouble(), 2.2).toFloat()
        startB = Math.pow(startB.toDouble(), 2.2).toFloat()

        endR = Math.pow(endR.toDouble(), 2.2).toFloat()
        endG = Math.pow(endG.toDouble(), 2.2).toFloat()
        endB = Math.pow(endB.toDouble(), 2.2).toFloat()

        // compute the interpolated color in linear space
        var a = startA + fraction * (endA - startA)
        var r = startR + fraction * (endR - startR)
        var g = startG + fraction * (endG - startG)
        var b = startB + fraction * (endB - startB)

        // convert back to sRGB in the [0..255] range
        a *= 255.0f
        r = Math.pow(r.toDouble(), 1.0 / 2.2).toFloat() * 255.0f
        g = Math.pow(g.toDouble(), 1.0 / 2.2).toFloat() * 255.0f
        b = Math.pow(b.toDouble(), 1.0 / 2.2).toFloat() * 255.0f

        return Math.round(a) shl 24 or (Math.round(r) shl 16) or (Math.round(g) shl 8) or Math.round(b)
    }

    @JvmStatic
    var title = 0

    @JvmStatic
    fun getTitleHeight(): Int {
        if (title == 0) {
            synchronized(title) {
                val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
                if (resourceId > 0 && title == 0) {
                    title = resources.getDimensionPixelSize(resourceId)
                }
            }
        }
        return title
    }

    /**
     * 标题栏高度
     */
    @JvmStatic
    fun getTitleHeight(activity: Activity): Int {
        return activity.window.findViewById<View>(Window.ID_ANDROID_CONTENT).top
    }

    /**
     * 计算对角线长度
     */
    @JvmStatic
    fun computeDiagonal(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        val i = Math.pow((x2 - x1).toDouble(), 2.0) + Math.pow((y2 - y1).toDouble(), 2.0)
        return Math.sqrt(i).toInt()
    }

    @JvmStatic
    fun abs(a: Double): Double {
        return Math.abs(a)
    }

    @JvmStatic
    fun abs(a: Float): Float {
        return Math.abs(a)
    }


    @JvmStatic
    fun getPhoneModel(): String {
        return Build.MODEL
    }

    @JvmStatic
    fun getAppVersionName(): String {
        return BuildConfig.VERSION_NAME
    }

    @JvmStatic
    fun getAppVersionCode(): Int {
        return BuildConfig.VERSION_CODE
    }

    @JvmStatic
    fun getAppDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    @JvmStatic
    fun getAndroidVersion(): String {
        return Build.VERSION.RELEASE
    }

    @JvmStatic
    fun getAndroidVersionCode(): Int {
        return Build.VERSION.SDK_INT
    }

    @JvmStatic
    fun unsubscribe(subscribe: Subscription?) {
        if (subscribe != null && subscribe.isUnsubscribed) {
            subscribe.unsubscribe()
        }
    }

    @JvmStatic
    fun fixInputMethodManagerLeak(destContext: Context) {

        val inputMethodManager = destContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val viewArray = arrayOf("mCurRootView", "mServedView", "mNextServedView", "mLastSrvView")

        for (view in viewArray) {
            try {
                val filed = inputMethodManager.javaClass.getDeclaredField(view)
                if (!filed.isAccessible) {
                    filed.isAccessible = true
                }
                val fileView = filed.get(inputMethodManager)
                if (fileView != null && fileView is View) {//被InputMethodManager持有引用的context是想要目标销毁的
                    if (fileView.context == destContext) {
                        filed.set(inputMethodManager, null) // 置空，破坏掉path to gc节点
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

}

inline fun <T> List<T>.interpolationSearch(key: Int, selector: (T) -> Int): Int {
    var low = 0
    var high = this.size - 1

    var mid: Int

    while (low < high) {
        val lowKey = selector(this[low])
        val highKey = selector(this[high])
        mid = low + (high - low) * (key - lowKey) / (highKey - lowKey)
        if (mid > high) {
            return -1
        }
        val temp = selector(this[mid])
        when {
            key < temp -> high = mid - 1
            key > temp -> low = mid + 1
            else -> return mid
        }
    }
    return -1
}

inline fun <T> List<T>.binarySearch(key: Long, selector: (T) -> Long): Int {

    var mid: Int
    var low = 0
    var high = size - 1

    while (low <= high) {
        mid = (low + high) / 2
        val midKey = selector(get(mid))
        when {
            midKey == key -> return mid
            midKey > key -> high = mid - 1
            else -> low = mid + 1
        }
    }
    return -1
}

inline fun <T> List<T>.binaryReversedSearch(key: Int, selector: (T) -> Int): Int {

    var mid: Int
    var low = 0
    var high = size - 1

    while (low <= high) {
        mid = (low + high) / 2
        val midKey = selector(get(mid))
        when {
            midKey == key -> return mid
            midKey < key -> high = mid - 1
            else -> low = mid + 1
        }
    }
    return -1
}


