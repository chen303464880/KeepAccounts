package com.cjj.keepaccounts.view

import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.utils.Utils

/**
 * @author CJJ
 * Created by CJJ on 2018/5/24 16:02.
 */
class MemberDrawable(private val char: String, @ColorInt private val color: Int) : Drawable() {
    private val mPaint = Paint()
    private val mTextPaint = Paint()

    init {
        mPaint.isAntiAlias = true
        mPaint.color = color

        mTextPaint.isAntiAlias = true
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.color = Color.WHITE
        mTextPaint.textSize = Utils.getDimension(R.dimen.text_size15).toFloat()
    }

    override fun draw(canvas: Canvas) {
        val height = bounds.bottom - bounds.top
        val width = bounds.right - bounds.left
        val cx = (width / 2).toFloat()
        val cy = (height / 2).toFloat()

        val dia = Math.min(bounds.right - bounds.left, bounds.bottom - bounds.top)
        val radius = dia / 2

        canvas.drawCircle(cx, cy, radius.toFloat(), mPaint)


        val fontMetrics = mTextPaint.fontMetrics
        val y = (fontMetrics.bottom - fontMetrics.top) / 2
        val baseLineY = cy + y - Utils.dip2px(4)
        canvas.drawText(char, cx, baseLineY, mTextPaint)

    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
        mTextPaint.alpha = alpha
        invalidateSelf()
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
        invalidateSelf()
    }
}