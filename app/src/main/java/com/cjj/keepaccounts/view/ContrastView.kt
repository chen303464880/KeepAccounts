package com.cjj.keepaccounts.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.OverScroller
import androidx.core.util.set
import com.cjj.keepaccounts.base.BaseView
import com.cjj.keepaccounts.utils.LogUtils
import org.jetbrains.anko.dip

/**
 * Created by CJJ on 2018/5/20 14:01.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class ContrastView : BaseView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var viewWidth = 0
    private var viewHeight = 0
    private var viewTop = 0
    private var viewLeft = 0

    private var contentHeight = 0F

    var textSize = dip2px(10)
    private var color = Color.WHITE
    private var monthAlpha = 240
    private var progressAlpha = 240
    private var moneyAlpha = 180
    private var cornerRadius = dip2px(6).toFloat()

    private var itemHeight = 0F
    private var itemDistance = 0F


    private var mScroller: OverScroller = OverScroller(context)

    var list = SparseArray<Double>()
        set(value) {
            maxMoney = 0.0
            for (i in 1..12) {
                val money = value[i]
                if (money == null) {
                    value[i] = 0.0
                } else {
                    if (money > maxMoney) {
                        maxMoney = money
                    }
                }
            }
            field = value
            invalidate()
        }
    var maxMoney = 0.0
    private val monthPaint: Paint
    private val progressPaint: Paint
    private val moneyPaint: Paint

    private val maxProgressWidth: Float

    private var mTouchSlop: Int = 0
    private var mMinimumVelocity: Int = 0
    private var mMaximumVelocity: Int = 0

    private var mOverscrollDistance: Int = 0
    private var mOverflingDistance: Int = 0

    var selectMonth: Int = 12
        set(value) {
            field = value
            if (field < 3) {
                field = 3
            }
            val i = 12 - field
            scrollY = Math.round(i * (itemDistance + itemHeight))
        }


    init {

        val configuration = ViewConfiguration.get(context)
        mTouchSlop = configuration.scaledTouchSlop
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity
        mOverscrollDistance = configuration.scaledOverscrollDistance
        mOverflingDistance = configuration.scaledOverflingDistance


        itemDistance = dip(10).toFloat()

        maxProgressWidth = 650.0F / 3.0F * dip(1)

        monthPaint = Paint()
        monthPaint.textAlign = Paint.Align.RIGHT
        monthPaint.isAntiAlias = true
        monthPaint.color = color
        monthPaint.alpha = monthAlpha
        monthPaint.textSize = dip2px(11).toFloat()

        val fontMetrics = monthPaint.fontMetrics
        itemHeight = (fontMetrics.descent - fontMetrics.ascent)

        progressPaint = Paint()
        progressPaint.isAntiAlias = true
        progressPaint.strokeWidth = dip2px(8).toFloat()
        progressPaint.color = color
        progressPaint.alpha = progressAlpha
        progressPaint.strokeCap = Paint.Cap.ROUND

        moneyPaint = Paint()
        monthPaint.isAntiAlias = true
        moneyPaint.color = color
        moneyPaint.alpha = moneyAlpha
        moneyPaint.textSize = context.resources.displayMetrics.density * 11

        for (i in 1..12) {
            val money = list[i]
            if (money == null) {
                list[i] = 0.0
            } else {
                if (money > maxMoney) {
                    maxMoney = money
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var drawY = itemDistance / 2 + viewTop
        for (i in 12 downTo 1) {
            val money = list[i]
            val y = drawY + itemHeight / 2

            val monthStartX = viewLeft.toFloat() + dip(26)
            canvas.drawText(i.toString() + "月", monthStartX, y + monthPaint.textSize / 2, monthPaint)


            val startX = monthStartX + itemDistance
            val endX = if (maxMoney == 0.0) {
                startX
            } else {
                (startX + money / maxMoney * maxProgressWidth).toFloat()
            }
            if (startX == endX) {
                canvas.drawCircle(startX, y + dip(1), dip(4).toFloat(), progressPaint)
            } else {
                canvas.drawLine(startX, y + dip(1), endX, y + dip(1), progressPaint)
            }



            canvas.drawText(String.format("%.2f", money), endX + dip2px(13).toFloat(), y + moneyPaint.textSize / 2, moneyPaint)


            drawY += itemDistance + itemHeight
        }
        contentHeight = drawY


    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (scrollY == 0 || scrollY >= Math.round(contentHeight - height)) {
            parent.requestDisallowInterceptTouchEvent(false)
        } else {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.dispatchTouchEvent(event)
    }


    private var tempY = 0.0F
    private var mVelocityTracker: VelocityTracker? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                tempY = event.y
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain()
                } else {
                    mVelocityTracker!!.clear()
                }
                mVelocityTracker!!.addMovement(event)

                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val toY = scrollY + (tempY - event.y)
                if (toY < 0F) {
                    return false
                }
                if (toY + height > contentHeight) {
                    return false
                }
                scrollTo(0, Math.round(toY))
                tempY = event.y
                mVelocityTracker!!.addMovement(event)
                mVelocityTracker!!.computeCurrentVelocity(1000)
                val yVelocity = mVelocityTracker!!.yVelocity
                LogUtils.i("yVelocity:$yVelocity")
            }
            MotionEvent.ACTION_UP -> {
                val velocityTracker = mVelocityTracker!!
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                val initialVelocity = velocityTracker.yVelocity.toInt()
                LogUtils.i("Velocity:$initialVelocity mMinimumVelocity$mMinimumVelocity")
                if (Math.abs(initialVelocity) > mMinimumVelocity) {
                    flingWithNestedDispatch(initialVelocity)
                }
            }
        }
        return true
    }

    private fun flingWithNestedDispatch(velocityY: Int) {
        val canFling = scrollY > 0 && scrollY < contentHeight - height
        if (canFling) {
            fling(velocityY)
        }

    }

    private fun fling(velocityY: Int) {
        mScroller.fling(scrollX, scrollY, 0, -velocityY, 0, 0, 0, contentHeight.toInt() - height)
        Log.i("TAG", "scrollY$scrollY finalY${mScroller.finalY}")
        postInvalidateOnAnimation()

    }

    private fun getScrollRange(): Int = Math.max(0, viewHeight)

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidateOnAnimation()
        }
        super.computeScroll()
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        setLocation(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setLocation(left, top, right, bottom)
    }

    private fun setLocation(left: Int, top: Int, right: Int, bottom: Int) {
        viewWidth = measuredWidth - left - right
        viewHeight = minimumHeight - top - bottom
        viewTop = top
        viewLeft = left
        invalidate()
    }

    override fun setBackgroundColor(color: Int) {
        val drawable = GradientDrawable()
        drawable.setColor(color)
        drawable.cornerRadius = cornerRadius
        super.setBackground(drawable)
    }


}