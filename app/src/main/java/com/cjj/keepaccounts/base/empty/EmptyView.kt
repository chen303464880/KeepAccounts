package com.cjj.keepaccounts.base.empty

import android.content.Intent
import android.os.Parcelable
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.utils.Delegate
import javax.inject.Inject

/**
 * @author CJJ
 * Created by CJJ on 2018/9/27 16:14.
 */
class EmptyView @Inject constructor() : IView {
    override val mIntent: Intent
        get() = Intent()

    override fun <T : Parcelable?> extra(key: String): Delegate<T> {
        return Delegate("")
    }
}