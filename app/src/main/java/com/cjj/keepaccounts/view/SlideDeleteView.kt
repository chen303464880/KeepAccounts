package com.cjj.keepaccounts.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.utils.Utils
import java.lang.IllegalStateException

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/28 10:02.
 */
class SlideDeleteView : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private lateinit var contentView: View
    private val deleteView: TextView = TextView(context)
    private var mRange: Int = 0
    private var mOpenState = false
    private var mSlideOffset = 0F

    private var status = Status.Close

    enum class Status {
        Close, Open, Draging
    }

    interface SlideListener {

        // 要去开启
        fun onStartOpen(mSlideDeleteView: SlideDeleteView)

        // 要去关闭
        fun onStartClose(mSlideDeleteView: SlideDeleteView)

        fun onOpen(mSlideDeleteView: SlideDeleteView)
        fun onClose(mSlideDeleteView: SlideDeleteView)
        fun onDraging(mSlideDeleteView: SlideDeleteView, mSlideOffset: Float)

    }

    private var listener: SlideListener? = null
    fun setOnSlideListener(listener: SlideListener) {
        this.listener = listener
    }

    private var deleteListener: (SlideDeleteView) -> Unit = {}

    fun setOnDeleteListener(listener: (SlideDeleteView) -> Unit) {
        this.deleteListener = listener
    }

    fun setOnContentClickListener(l: OnClickListener?) {
        contentView.setOnClickListener(l)
    }

    private val mDragger: ViewDragHelper = ViewDragHelper.create(this, 1F, Callback())

    init {
        deleteView.text = Utils.getString(R.string.delete)
        val layoutParams = LayoutParams(Utils.dip2px(100F), LayoutParams.MATCH_PARENT)
        layoutParams.gravity = Gravity.END
        deleteView.layoutParams = layoutParams
        deleteView.setTextColor(Color.WHITE)
        deleteView.setBackgroundColor(Color.RED)
        deleteView.gravity = Gravity.CENTER
        deleteView.setOnClickListener {
            close(true)
            deleteListener.invoke(this)
        }
        addView(deleteView, layoutParams)

    }

    inner class Callback : ViewDragHelper.Callback() {
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
            mSlideOffset = left.toFloat() / Math.abs(mRange.toFloat())
            dispatchSwipeEvent()
            mOpenState = mSlideOffset != 0F
            if (changedView === deleteView) {
                deleteView.layout(measuredWidth - Math.abs(mRange), 0, measuredWidth, measuredHeight)
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            when {
                xvel == 0F && contentView.left < mRange / 2 -> open()
                xvel < 0 -> open()
                else -> close()
            }
        }


    }

    fun isOpen(): Boolean {
        return mOpenState
    }


    private fun open() {
        open(true)
    }

    private fun close() {
        close(true)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun open(isAnimation: Boolean) {
        if (isAnimation) {
            if (mDragger.smoothSlideViewTo(contentView, mRange, 0)) {
                ViewCompat.postInvalidateOnAnimation(this)
            }
        } else {
            contentView.layout(mRange, 0, measuredWidth, measuredHeight)
        }
    }

    fun close(isAnimation: Boolean) {
        if (isAnimation) {
            if (mDragger.smoothSlideViewTo(contentView, 0, 0)) {
                ViewCompat.postInvalidateOnAnimation(this)
            }
        } else {
            contentView.layout(0, 0, measuredWidth, measuredHeight)
        }
    }

    fun dispatchSwipeEvent() {

        if (listener != null) {
            listener?.onDraging(this, mSlideOffset)
        }

        // 记录上一次的状态
        val preStatus = status
        // 更新当前状态
        status = updateStatus()
        if (preStatus !== status && listener != null) {
            if (status === Status.Close) {
                listener?.onClose(this)
            } else if (status === Status.Open) {
                listener?.onOpen(this)
            } else if (status === Status.Draging) {
                if (preStatus === Status.Close) {
                    listener?.onStartOpen(this)
                } else if (preStatus === Status.Open) {
                    listener?.onStartClose(this)
                }
            }
        }
    }

    private fun updateStatus(): Status {

        val left = contentView.left
        if (left == 0) {
            return Status.Close
        } else if (left == mRange) {
            return Status.Open
        }
        return Status.Draging
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mDragger.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun fixLeft(left: Int): Int {
        if (left > 0) {
            return 0
        } else if (left < mRange) {
            return mRange
        }
        return left
    }


    private var dX = 0F
    private var dY = 0F
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = ev.rawX
                dY = ev.rawY
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val x = ev.rawX - dX
                val y = ev.rawY - dY
                Log.i("TAG", "x:$x\tisOpen():${isOpen()}")
                val angle = Utils.computingAngle(0F, 0F, Math.abs(x), Math.abs(y))
                if ((x >= 0 && angle < 45F && isOpen()) || (angle < 20F && x < 0 && !isOpen())) {
                    Log.i("TAG", "angle:$angle 拦截")
                    parent.requestDisallowInterceptTouchEvent(true)
                } else {
                    Log.i("TAG", "angle:$angle 不拦截")
                    parent.requestDisallowInterceptTouchEvent(false)
                    return false
                }
                dX = ev.rawX
                dY = ev.rawY

            }
            MotionEvent.ACTION_UP -> {
            }
        }

        return mDragger.shouldInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mDragger.processTouchEvent(event)
        return true
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount < 2) {
            throw IllegalStateException()
        }
        contentView = getChildAt(1)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRange = -getChildAt(0).measuredWidth
    }


}