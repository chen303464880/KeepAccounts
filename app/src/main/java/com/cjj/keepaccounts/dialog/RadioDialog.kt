package com.cjj.keepaccounts.dialog

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.cjj.keepaccounts.base.BaseDialog
import com.cjj.keepaccounts.view.CycleWheelView


/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/12 12:02.
 */
class RadioDialog(context: Context) : BaseDialog(context) {
    /**
     * 滑动选择器
     */
    private lateinit var wheelView: CycleWheelView
    /**
     * 点击确定之后的回调
     */
    private var listener: ((Int) -> Unit)? = null
    /**
     * 数据填充列表
     */
    var data: List<String>
        get() = wheelView.labels
        set(value) {
            wheelView.labels = value
        }

    var selection: Int
        get() = wheelView.selection
        set(value) {
            wheelView.selection = value
        }

    fun setOnItemChangeListener(listener: (position: Int) -> Unit) {
        this.listener = listener
    }

    override fun init() {
        setPositiveListener{
            listener?.invoke(wheelView.selection)
        }
    }

    override fun setContentView(): View {
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        wheelView = CycleWheelView(context)
        wheelView.layoutParams = layoutParams
        try {
            wheelView.setWheelSize(5)
        } catch (e: CycleWheelView.CycleWheelViewException) {
            e.printStackTrace()
        }

        return wheelView
    }
}