package com.cjj.keepaccounts.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import com.cjj.keepaccounts.base.BaseView

/**
 * Created by CJJ on 2018/5/20 18:28.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class PieChartView : BaseView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private var viewWidth = 0
    private var viewHeight = 0
    private var viewTop = 0
    private var viewLeft = 0

    private var diameter = 0

    private var ovalRectF = RectF()

    private var strokeWidth = dip2px(40).toFloat()
        set(value) {
            mPaint.strokeWidth = value
            field = value
        }
    var pieChartInfo = ArrayList<PieChartItem>()
        set(value) {
            field.clear()
            field.addAll(value)
            invalidate()
        }

    private val mPaint: Paint

    private var defaultColor: Int

    init {
        defaultColor = Color.GRAY

        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = strokeWidth
        mPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (pieChartInfo.isEmpty()) {
            mPaint.color = defaultColor
            canvas.drawArc(ovalRectF, 0F, 360F, true, mPaint)
        } else {
            var ratio = 0.0
            pieChartInfo.forEach {
                val startAngle = (ratio * 360F / 100F - 90F).toFloat()
                var sweepAngle = (it.ratio * 360F / 100F + 1).toFloat()
                if (sweepAngle > 360F) {
                    sweepAngle = 360F
                }
                mPaint.color = it.color
                canvas.drawArc(ovalRectF, startAngle, sweepAngle, false, mPaint)
                ratio += it.ratio
            }
        }

    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        setLocation(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setLocation(left, top, right, bottom)
    }


    override fun setForegroundGravity(gravity: Int) {


    }

    private fun setLocation(left: Int, top: Int, right: Int, bottom: Int) {
        viewWidth = measuredWidth - left - right
        viewHeight = measuredHeight - top - bottom
        diameter = Math.min(viewWidth, viewHeight)
        viewTop = top
        viewLeft = left

        val fl = strokeWidth / 2
        ovalRectF.set(viewLeft.toFloat() + fl, viewTop.toFloat() + fl, diameter.toFloat() + viewLeft.toFloat() - fl, diameter.toFloat() + viewTop.toFloat() - fl)

        invalidate()
    }

    data class PieChartItem(val ratio: Double, val color: Int)
}
