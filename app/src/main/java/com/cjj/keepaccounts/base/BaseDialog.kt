package com.cjj.keepaccounts.base

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.cjj.keepaccounts.R


/**
 * @author chenjunjie
 * Created by CJJ on 2018/2/6 15:46.
 */
abstract class BaseDialog(context: Context) : AlertDialog.Builder(context) {


    private lateinit var tvTitle: TextView
    private lateinit var flContent: FrameLayout
    private lateinit var tvPositive: TextView
    private lateinit var tvNegative: TextView
    private lateinit var llBtn: LinearLayout
    protected var alertDialog: AlertDialog? = null

    init {
        baseInit()
        this.init()
    }

    private fun baseInit() {
        val view = View.inflate(context, R.layout.dialog_base_view, null)
        tvTitle = view.findViewById(R.id.tv_title)
        tvPositive = view.findViewById(R.id.tv_positive)
        tvNegative = view.findViewById(R.id.tv_negative)
        flContent = view.findViewById(R.id.fl_content)
        llBtn = view.findViewById(R.id.ll_btn)
        flContent.addView(setContentView())
        this.setView(view)

    }

    abstract fun init()

    abstract fun setContentView(): View

    fun setPositiveButton(text: CharSequence, listener: ((View) -> Unit)?): BaseDialog {
        tvPositive.text = text
        setPositiveListener(listener)
        return this
    }

    fun setPositiveListener(listener: ((View) -> Unit)?): BaseDialog {
        llBtn.visibility = View.VISIBLE
        tvPositive.visibility = View.VISIBLE
        tvPositive.setOnClickListener {
            dismiss()
            listener?.invoke(it)
        }
        return this
    }

    fun setNegativeButton(text: CharSequence, listener: ((View) -> Unit)?): BaseDialog {
        tvNegative.text = text
        setNegativeListener(listener)
        return this
    }

    fun setNegativeListener(listener: ((View) -> Unit)?): BaseDialog {
        llBtn.visibility = View.VISIBLE
        tvNegative.visibility = View.VISIBLE
        tvNegative.setOnClickListener {
            dismiss()
            listener?.invoke(it)
        }
        return this
    }

    /**
     * 设置标题
     *
     * @param title 标题
     * @return 返回自已，用于链式调用
     */
    fun setTitle(title: String): BaseDialog {
        tvTitle.visibility = View.VISIBLE
        tvTitle.text = title
        return this
    }

    /**
     * 显示对话框
     *
     * @return AlertDialog对象，用于关闭对话框
     */
    override fun show(): AlertDialog {
        if (null == alertDialog) {
            alertDialog = this.create()
            val window = alertDialog!!.window
            window?.setBackgroundDrawable(null)
        }

        //设置dialog的背景
        //        Window window = alertDialog.getWindow();
        //        if (null != window) {
        //            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //        }
        alertDialog?.show()
        return alertDialog!!
    }

    protected fun dismiss() {
        alertDialog?.dismiss()
    }


}