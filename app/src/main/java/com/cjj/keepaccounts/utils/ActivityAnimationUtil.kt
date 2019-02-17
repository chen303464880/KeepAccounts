package com.cjj.keepaccounts.utils

import android.animation.Animator
import android.app.Activity
import android.view.View
import android.view.ViewAnimationUtils

/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/4 15:21.
 */
object ActivityAnimationUtil {
    fun circularClose(activity: Activity, view: View) {
        val decorView = activity.window.decorView
        val startRadius = Utils.computeDiagonal(decorView.width, decorView.height, view.pivotX.toInt(), view.pivotY.toInt())
        val intAry = IntArray(2)
        view.getLocationOnScreen(intAry)
        val centerX = intAry[0] + view.pivotX.toInt()
        val centerY = intAry[1] + view.pivotX.toInt()
        val animator = ViewAnimationUtils.createCircularReveal(decorView, centerX, centerY, startRadius.toFloat(), 0f)
        animator.duration = 500
        animator.start()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                decorView.visibility = View.INVISIBLE
                activity.finish()
                activity.overridePendingTransition(0, 0)
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })

//        fun getViewPoint(view: View): Point {
//            val intAry = IntArray(2)
//            view.getLocationOnScreen(intAry)
//            return Point(intAry[0], intAry[1])
//        }
    }
}