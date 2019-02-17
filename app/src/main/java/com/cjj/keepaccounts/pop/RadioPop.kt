package com.cjj.keepaccounts.pop

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.core.view.get
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.drawableEnd
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColor


/**
 * @author chenjunjie
 * Created by CJJ on 2017/11/27 16:19.
 */
class RadioPop(context: Context, items: List<String>) {
    private var popupWindow: PopupWindow? = null
    private val view: View = Utils.inflate(R.layout.pop_riado_view, context)

    private var changeListener: ((position: Int) -> Unit)? = null
    private val rg: RadioGroup
    private val tvTitle: TextView
    private val viewArrow: View

    fun setOnChangeListener(listener: (Int) -> Unit) {
        changeListener = listener
    }


    init {
        popupWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                true)
        popupWindow?.isOutsideTouchable = true


        tvTitle = view.findViewById(R.id.tv1)
        rg = view.findViewById(R.id.rg_year)
        viewArrow = view.findViewById(R.id.view_arrow)
        val size = items.size
        for (i in 0 until size) {
            val radioButton = RadioButton(context)
            radioButton.buttonDrawable = null
            radioButton.backgroundResource = R.drawable.ripple_bg_click_transparency
            radioButton.setPadding(Utils.getDimension(R.dimen.start), 0, Utils.getDimension(R.dimen.end), 0)
            radioButton.drawableEnd = Utils.getDrawable(R.drawable.selector_rb_property_check_drawable)
            radioButton.textColor = Utils.getColor(R.color.text_color_655f5f)
            radioButton.text = items[i]
            radioButton.layoutParams = ViewGroup.LayoutParams(Utils.dip2px(235F), Utils.dip2px(45F))
            radioButton.setOnClickListener {
                popupWindow?.dismiss()
            }
            rg.addView(radioButton)
        }
        rg.check(rg.getChildAt(0).id)

        rg.setOnCheckedChangeListener { group, checkedId ->
            changeListener?.invoke(group.indexOfChild(group.findViewById(checkedId)))
        }

    }

    fun setTitle(title: CharSequence) {
        tvTitle.text = title
    }

    fun setSelection(position: Int) {
        rg.check(rg[position].id)
    }


    private var isArrowX = true

    fun showAsDropDown(anchor: View) {
//        val attributes = window.attributes
//        attributes.alpha = 0.6f
//        window.attributes = attributes
        popupWindow?.showAsDropDown(anchor)
        view.doOnPreDraw {
            if (isArrowX) {
                isArrowX = false
                val popAry = IntArray(2)
                view.getLocationOnScreen(popAry)
                val anchorAry = IntArray(2)
                anchor.getLocationOnScreen(anchorAry)
                var x = anchorAry[0] + anchor.measuredWidth / 2 - popAry[0] - viewArrow.x
                when {
                    viewArrow.x + x + viewArrow.measuredWidth > view.measuredWidth - Utils.dip2px(20F) -> x -= Utils.dip2px(20F)
                    viewArrow.x + x < Utils.dip2px(20F) -> x = Utils.dip2px(20F).toFloat()
                    x < 0F -> x = 0F
                }
                viewArrow.translationX = x
            }


            val dip2px = Utils.getDimension(R.dimen.property_check_height)
            if (view.measuredHeight > dip2px) {
                view.layoutParams.height = dip2px
                view.requestLayout()
            }
        }
    }

    fun dismiss() {
        popupWindow?.dismiss()
        popupWindow = null
    }
}