package com.cjj.keepaccounts.base

import android.content.Intent
import android.os.Parcelable
import com.cjj.keepaccounts.utils.Delegate

/**
 * @author CJJ
 * Created by CJJ on 2018/8/9 17:15.
 */
interface IView {
    fun <T : Parcelable?> extra(key: String = "info"): Delegate<T>
    val mIntent: Intent
}