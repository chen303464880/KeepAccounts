package com.cjj.keepaccounts.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager

/**
 * @author CJJ
 * Created by CJJ on 2017/11/10 14:00.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 */
@SuppressLint("Registered")
open class TransparencyActivity<P : BasePresenter<out IView, out IModel>> : BaseSlideCloseActivity<P>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        setStatusBarColor(Color.TRANSPARENT)
    }
}