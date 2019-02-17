package com.cjj.keepaccounts.utils

import android.os.Parcelable
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IModel
import com.cjj.keepaccounts.base.IView
import kotlin.reflect.KProperty

/**
 * Created by CJJ on 2019/1/17 18:52.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class PresenterDelegate<T : Parcelable?>(val view: IView,val key: String) {
    private var p: T? = null
    private var isGet = false
    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: BasePresenter<out IView, out IModel>, property: KProperty<*>): T {
        if (!isGet) {
            isGet = true
            p = view.mIntent.getParcelableExtra<T>(key)
        }
        return p as T
    }

    operator fun setValue(thisRef: BasePresenter<out IView, out IModel>, property: KProperty<*>, value: T) {
        p = value
    }
}