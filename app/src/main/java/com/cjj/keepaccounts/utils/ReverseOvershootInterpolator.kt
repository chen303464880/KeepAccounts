package com.cjj.keepaccounts.utils

import android.view.animation.OvershootInterpolator

/**
 * @author CJJ
 * Created by CJJ on 2018/5/31 16:33.
 */
class ReverseOvershootInterpolator(private val mTension: Float = 2F) : OvershootInterpolator() {
    override fun getInterpolation(input: Float): Float {
        val t = -input
        return t * t * ((mTension + 1) * t + mTension) + 1.0f
    }
}