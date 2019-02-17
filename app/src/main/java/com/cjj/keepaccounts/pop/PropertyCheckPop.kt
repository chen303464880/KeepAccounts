package com.cjj.keepaccounts.pop

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.PropertyAdapter
import com.cjj.keepaccounts.adapter.PropertyCheckAdapter
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.utils.RecyclerSpace
import com.cjj.keepaccounts.utils.Utils


/**
 * @author chenjunjie
 * Created by CJJ on 2017/11/27 16:19.
 */
class PropertyCheckPop(context: Context, adapter: PropertyAdapter) {
    private val popupWindow: PopupWindow
    private val window: Window
    private var checkadapter: PropertyCheckAdapter? = null

    //recyclerView的分割线
    private val itemDecoration = RecyclerSpace(1, Utils.getColor(R.color.divider_color_e7))


    val view: View = Utils.inflate(R.layout.view_property_check_recycle_view, context)

    init {
        popupWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                true)
        popupWindow.isOutsideTouchable = true

        val recyclerView = view.findViewById<View>(R.id.lv_property_check) as RecyclerView
        val tv1 = view.findViewById<View>(R.id.tv1) as TextView
        tv1.text = Utils.context.getString(R.string.property_check, if (adapter.possess) Utils.context.getString(R.string.property) else Utils.getString(R.string.liabilities))
        checkadapter = PropertyCheckAdapter(adapter)
        recyclerView.adapter = checkadapter
        recyclerView.layoutManager = LinearLayoutManager(Utils.context)
        recyclerView.addItemDecoration(itemDecoration)


        window = ActivityTool.currentActivity().window
        view.doOnPreDraw {
            val dip2px = Utils.getDimension(R.dimen.property_check_height)
            if (view.measuredHeight > dip2px) {
                view.layoutParams.height = dip2px
                view.requestLayout()
            }
        }
        popupWindow.setOnDismissListener {
            val attributes = window.attributes
            attributes.alpha = 1.0f
            window.attributes = attributes
        }
    }

    fun notifyDataSetChanged() {
        checkadapter?.notifyDataSetChanged()
    }

    fun showAsDropDown(anchor: View) {
        val attributes = window.attributes
        attributes.alpha = 0.6f
        window.attributes = attributes
        popupWindow.showAsDropDown(anchor)
        view.doOnPreDraw {
            val dip2px = Utils.getDimension(R.dimen.property_check_height)
            if (view.measuredHeight > dip2px) {
                view.layoutParams.height = dip2px
                view.requestLayout()
            }
        }
    }
}