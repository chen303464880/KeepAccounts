package com.cjj.keepaccounts.view

import android.content.Context
import android.support.v4.view.NestedScrollingParent
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.OverScroller
import androidx.core.view.forEach
import org.jetbrains.anko.dip

/**
 * @author CJJ
 * Created by CJJ on 2018/6/13 14:18.
 */
class SpringBackLayout : FrameLayout, NestedScrollingParent {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private lateinit var scrollView: View

    interface OnSpringBackListener {
        fun onTopSpringBack()
        fun onBottomSpringBack()
    }

    private var overListener: OnSpringBackListener? = null
    private var startListener: OnSpringBackListener? = null

    fun setOnStartSpringBackListener(startListener: OnSpringBackListener) {
        this.startListener = startListener
    }

    fun setOnOverSpringBackListener(overListener: OnSpringBackListener) {
        this.overListener = overListener
    }

    var friction = 4

    private var mScroller: OverScroller = OverScroller(context)

    override fun onFinishInflate() {
        super.onFinishInflate()
        this.forEach {
            if (it is RecyclerView) {
                scrollView = it
                return
            }
        }
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return true
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        super.onNestedPreScroll(target, dx, dy, consumed)
        if ((dy > 0 && !scrollView.canScrollVertically(1))//手指下滑且RecyclerView到顶
                || (dy < 0 && !scrollView.canScrollVertically(-1))//手指上滑且RecyclerView到底
                || (dy > 0F && scrollY < 0F)//手指下滑且view上部
                || (dy < 0F && scrollY > 0F)//手指上滑且view底部
        ) {
//            Log.i("TAG", "onNestedPreScroll -> dy:$dy\tscrollY:$scrollY")
            if ((dy > 0 && scrollY >= 0) || (dy < 0 && scrollY <= 0)) {
//                Log.i("TAG", "onNestedPreScroll -> 减速")
                scrollBy(0, dy / friction)
            } else {
//                Log.i("TAG", "onNestedPreScroll -> 常速")
                scrollBy(0, dy)
            }
            consumed[1] = dy
        }
    }

    override fun onStopNestedScroll(child: View) {
        super.onStopNestedScroll(child)
        if (scrollY != 0) {
            isSpringBack = true
            mScroller.springBack(scrollX, scrollY, 0, 0, 0, 0)
            if (Math.abs(scrollY) > dip(15)) {
                isTop = scrollY < 0
                if (isTop) {
                    startListener?.onTopSpringBack()
                } else {
                    startListener?.onBottomSpringBack()
                }
            }
            postInvalidateOnAnimation()
        }
    }

    var l: ((view: View, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) -> Unit)? = null

    fun setOnScrollChangeListener(l: (view: View, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) -> Unit) {
        this.l = l
    }

    override fun scrollTo(x: Int, y: Int) {
        val oldScrollY = scrollY
        val oldScrollX = scrollX
        super.scrollTo(x, y)
        l?.invoke(this, scrollX, scrollY, oldScrollX, oldScrollY)
    }

    //    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
////        if (scrollY != 0) {
////            return
////        }
////        Log.i("TAG", "上拉${scrollView.canScrollVertically(1)}")
////        Log.i("TAG", "下拉${scrollView.canScrollVertically(-1)}")
//        if (ViewCompat.isNestedScrollingEnabled(scrollView)) {
//            super.requestDisallowInterceptTouchEvent(disallowIntercept)
//        }
//    }
//
    private var isSpringBack = false
    //
//    private var downY = 0F
//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        Log.i("TAG", "onInterceptTouchEvent")
////        if (ev.x + scrollX in scrollView.left..scrollView.right && ev.y + scrollY in scrollView.top..scrollView.bottom) {
//        when (ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                mActivePointerId = ev.getPointerId(0)
//                downY = ev.y
//                if (!mScroller.isFinished) {
//                    mScroller.abortAnimation()
//                }
//            }
//            MotionEvent.ACTION_MOVE -> {
//                if (scrollY != 0) {
//                    return true
//                }
//                if (downY > ev.y) {//上拉
//                    return !scrollView.canScrollVertically(1)
//                } else if (downY < ev.y) {//下拉
//                    return !scrollView.canScrollVertically(-1)
//                }
//            }
//            MotionEvent.ACTION_UP -> {
//
//            }
//        }
////        }
//
//        return super.onInterceptTouchEvent(ev)
//    }
//
//
//    private var mActivePointerId = -1
    private var isTop = true
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//            }
//            MotionEvent.ACTION_MOVE -> {
//                val activePointerIndex = event.findPointerIndex(mActivePointerId)
//                if (activePointerIndex != -1) {
//                    Log.i("TAG", "SpringBack onTouchEvent MOVE")
//                    val dy = Math.round(downY - event.getY(activePointerIndex))
//                    if ((dy > 0 && scrollY > 0) || (dy < 0 && scrollY < 0)) {
//                        scrollBy(0, dy / 4)
//                    } else {
//                        scrollBy(0, dy)
//                    }
//                    downY = event.y
//                }
//            }
//            MotionEvent.ACTION_POINTER_UP -> {
//                mActivePointerId = event.getPointerId(event.actionIndex)
//                downY = event.getY(event.findPointerIndex(mActivePointerId))
//                Log.i("TAG", "POINTER_UP\ttempY:$downY")
//            }
//            MotionEvent.ACTION_POINTER_DOWN -> {
//                mActivePointerId = event.getPointerId(event.actionIndex)
//                downY = event.getY(event.findPointerIndex(mActivePointerId))
//                Log.i("TAG", "POINTER_DOWN\ttempY:$downY")
//            }
//            MotionEvent.ACTION_UP -> {
////                pointerIndex = event.findPointerIndex(mActivePointerId)
////                if (pointerIndex < 0) {
////                    return true
////                }
//                if (scrollY != 0) {
//                    isSpringBack = true
//                    isTop = scrollY < 0
//                    mScroller.springBack(scrollX, scrollY, 0, 0, 0, 0)
//                    if (isTop) {
//                        startListener?.onTopSpringBack()
//                    } else {
//                        startListener?.onBottomSpringBack()
//                    }
//                    postInvalidateOnAnimation()
//                }
//            }
//        }
//
//
//        return true
//    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidateOnAnimation()
        } else if (isSpringBack) {
            isSpringBack = false
            if (isTop) {
                overListener?.onTopSpringBack()
            } else {
                overListener?.onBottomSpringBack()
            }
        }
        super.computeScroll()
    }
}