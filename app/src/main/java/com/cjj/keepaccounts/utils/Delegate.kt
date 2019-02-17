package com.cjj.keepaccounts.utils

import android.app.Activity
import android.os.Parcelable
import kotlin.reflect.KProperty

/**
 * Created by CJJ on 2019/1/17 18:52.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class Delegate<T : Parcelable?>(val key: String) {
    private  var p:T?=null
    private var isGet = false
    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Activity, property: KProperty<*>): T {
        if (!isGet) {
            isGet = true
            p = thisRef.intent.getParcelableExtra<T>(key)
        }
        return p as T
    }



    operator fun setValue(thisRef: Activity, property: KProperty<*>, value: T) {
        p=value
    }
}