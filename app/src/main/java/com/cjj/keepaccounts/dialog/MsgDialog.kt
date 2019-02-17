package com.cjj.keepaccounts.dialog

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseDialog
import com.cjj.keepaccounts.utils.Utils
import org.jetbrains.anko.horizontalPadding
import org.jetbrains.anko.textColorResource
import org.jetbrains.anko.verticalPadding

/**
 * Created by CJJ on 2018/5/10 19:43.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class MsgDialog(context: Context) : BaseDialog(context) {
    private lateinit var textView: TextView
    var message: CharSequence
        set(value) {
            textView.text = value
        }
        get() {
            return textView.text
        }

    override fun init() {

    }

    override fun setContentView(): View {
        textView = TextView(context)
        textView.apply {
            textColorResource = R.color.text_color_655f5f
            setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getDimension(R.dimen.text_size15).toFloat())
            gravity = Gravity.CENTER
            horizontalPadding = Utils.getDimension(R.dimen.start)
            verticalPadding = Utils.dip2px(25)
            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        return textView
    }

    override fun show(): AlertDialog {
        val alertDialog = super.show()
        val window = alertDialog.window
        if (window != null) {
            val lp = window.attributes
            if (lp != null) {
                lp.width = (com.cjj.keepaccounts.utils.Utils.widthPixels * 0.85).toInt() //设置宽度
                window.attributes = lp
            }
        }
        return alertDialog

    }
}