package com.cjj.keepaccounts.view

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.cjj.keepaccounts.utils.Utils

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/28 10:02.
 */
class ActivitySlideCloseLayout : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    interface SlideListener {

        fun onPanelSlide(slideOffset: Float)

        fun onPanelOpened()

        fun onPanelClosed()
    }

    private var listener: SlideListener? = null
    fun setOnSlideListener(listener: SlideListener) {
        this.listener = listener
    }


    private var _contentView: View? = null
    private val contentView: View
        get() {
            if (_contentView == null) {
                _contentView = getChildAt(1)
            }
            return _contentView!!
        }
    private var _deleteView: View? = null
    private val deleteView: View
        get() {
            if (_deleteView == null) {
                _deleteView = getChildAt(0)
            }
            return _deleteView!!
        }
    private var mRange: Int = 0
    private var mOpenState = false
    private var mSlideOffset = 0F

    private val mDragger: ViewDragHelper = ViewDragHelper.create(this, 1F, DragHelperCallback())

    private fun open() {
        if (mDragger.smoothSlideViewTo(contentView, -mRange, 0)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun close() {
        if (mDragger.smoothSlideViewTo(contentView, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mDragger.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun fixLeft(left: Int): Int {
        if (left < 0) {
            return 0
        } else if (left < mRange) {
            return mRange
        }
        return left
    }

    inner class DragHelperCallback : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean = true
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            var l = left
            if (child === contentView) {
                l = fixLeft(left)
            }

            return l
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return if (child === contentView) child.width else 0
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return if (child === contentView) child.height else 0
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            dispatchOnPanelSlide(left.toFloat() / Math.abs(mRange.toFloat()))
            if (changedView === deleteView) {
                deleteView.layout(measuredWidth - Math.abs(mRange), 0, measuredWidth, measuredHeight)
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            when {
                xvel == 0F && contentView.left > -mRange / 2 -> open()
                xvel > 0 && contentView.left > -mRange / 5 -> open()
                else -> close()
            }
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            if (mDragger.viewDragState == ViewDragHelper.STATE_IDLE) {
                mOpenState = if (mSlideOffset == 1f) {
                    dispatchOnPanelClosed()
                    false
                } else {
                    dispatchOnPanelOpened()
                    true
                }
            }
        }
    }

    private fun dispatchOnPanelSlide(slideOffset: Float) {
        mSlideOffset = slideOffset
        listener?.onPanelSlide(slideOffset)
    }

    private fun dispatchOnPanelClosed() {
        listener?.onPanelClosed()
    }

    private fun dispatchOnPanelOpened() {
        listener?.onPanelOpened()
    }

    private var dx = 0F
    private var dy = 0F
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var b = false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                dx = ev.rawX
                dy = ev.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val x = ev.rawX - dx
                val y = ev.rawY - dy

                b = x > 0 && Utils.computingAngle(0F, 0F, x, y) in -15F..15F
                dx = ev.rawX
                dy = ev.rawY
            }
            MotionEvent.ACTION_UP -> {

            }
        }
        mDragger.shouldInterceptTouchEvent(ev)
        return b
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.x > contentView.left) {
            mDragger.processTouchEvent(event)
        }
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRange = -measuredWidth
    }


}