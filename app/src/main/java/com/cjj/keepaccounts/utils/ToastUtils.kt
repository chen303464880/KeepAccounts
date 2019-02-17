package com.cjj.keepaccounts.utils

import android.widget.Toast

/**
 * Created by CJJ on 2017/9/30 9:46.
 * Copyright © 2015-2017 CJJ All rights reserved.
 */
object ToastUtils {

    private object ToastInstance {
        internal val TOAST_LONG: Toast = Toast.makeText(Utils.context, "", Toast.LENGTH_LONG)
        internal val TOAST_SHORT: Toast = Toast.makeText(Utils.context, "", Toast.LENGTH_SHORT)
    }

    fun longToast(msg: String) {
        ToastInstance.TOAST_LONG.setText(msg)
        ToastInstance.TOAST_LONG.show()

    }

    fun shortToast(msg: String) {
        ToastInstance.TOAST_SHORT.setText(msg)
        ToastInstance.TOAST_SHORT.show()
    }
}