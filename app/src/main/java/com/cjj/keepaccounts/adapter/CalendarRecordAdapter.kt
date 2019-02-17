package com.cjj.keepaccounts.adapter

import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.inSpans
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.bean.CalendarRecordBean
import com.cjj.keepaccounts.utils.MoneyUtils
import com.cjj.keepaccounts.utils.Utils
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundDrawable

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/21 14:04.
 */
class CalendarRecordAdapter : BaseRecyclerViewAdapter<CalendarRecordAdapter.ViewHolder, CalendarRecordBean>() {
    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    private var checkPosition: Int = -1

    private var listener: (year: Int, month: Int, day: Int) -> Unit = { _, _, _ -> Unit }

    fun setOnItemCheckedListener(listener: (year: Int, month: Int, day: Int) -> Unit) {
        this.listener = listener
    }

    override fun setData(data: List<CalendarRecordBean>) {
        super.setData(data)
        checkPosition = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = TextView(parent.context)
        view.gravity = Gravity.END or Gravity.TOP
        view.setPadding(0, Utils.dip2px(6F), Utils.dip2px(6F), 0)
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getDimension(R.dimen.text_size16).toFloat())
        view.setLineSpacing(0F, 1.15F)
        view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Utils.widthPixels / 7)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val bean = getItem(position)
        holder.textView.setOnClickListener(null)
        if (bean.isLastMonth) {
            holder.textView.text = ""
            holder.textView.backgroundColor = Utils.getColor(R.color.transparent)
        } else {
            if (bean.isFuture) {
                holder.textView.text = bean.day.toString()
                holder.textView.setTextColor(Utils.getColor(R.color.text_color_a19e9d))
                holder.textView.backgroundColor = Utils.getColor(R.color.transparent)
            } else {
                holder.textView.text = bean.day.toString()
                holder.textView.setTextColor(Utils.getColor(if (bean.isChecked) R.color.AppThemeColor else R.color.text_color_655f5f))
                if (bean.income == 0.0 && bean.expend == 0.0) {
                    holder.textView.backgroundColor = Utils.getColor(R.color.transparent)
                } else {
                    holder.textView.backgroundColor = Utils.getColor(if (bean.isOverproof) R.color.beyond_budget_color else R.color.beyond_not_budget_color)

                    val strSpan = SpannableStringBuilder(bean.day.toString())
                    val incomeColor = Utils.getColor(if (bean.income > 0.0) R.color.beyond_not_budget_text_color else R.color.text_color_81929b)
                    strSpan.inSpans(ForegroundColorSpan(incomeColor), AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size08))) {
                        append("\n")
                        append(MoneyUtils.formatMoney(bean.income))
                    }

                    val expendColor = Utils.getColor(if (bean.expend > 0.0) R.color.beyond_budget_text_color else R.color.text_color_81929b)
                    strSpan.inSpans(ForegroundColorSpan(expendColor), AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size08))) {
                        append("\n")
                        append(MoneyUtils.formatMoney(bean.expend))
                    }
                    holder.textView.text = strSpan
                }
                if (bean.isToday) {
                    holder.textView.backgroundDrawable = Utils.getDrawable(R.drawable.shape_calendar_record_today_bg)
                }
                if (bean.isChecked) {
                    holder.textView.backgroundDrawable = Utils.getDrawable(R.drawable.shape_calendar_record_checked_bg)
                }
                holder.textView.setOnClickListener {
                    refreshChecked(position)
                }
            }
        }
    }

    fun refreshChecked(position: Int) {
        if (position >= itemCount) {
            return
        }
        if (checkPosition <= 0 || checkPosition != position) {
            val calendarRecordBean = getItem(position)
            calendarRecordBean.isChecked = true
            notifyItemChanged(position)
            if (checkPosition >= 0) {
                getItem(checkPosition).isChecked = false
                notifyItemChanged(checkPosition)
            }
            checkPosition = position
            listener.invoke(calendarRecordBean.year, calendarRecordBean.month, calendarRecordBean.day)
        }
    }
}