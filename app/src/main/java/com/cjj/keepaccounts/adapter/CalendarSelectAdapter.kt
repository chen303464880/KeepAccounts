package com.cjj.keepaccounts.adapter

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.bean.CalendarDayBean
import com.cjj.keepaccounts.enum.ListViewType
import com.cjj.keepaccounts.utils.Utils
import org.jetbrains.anko.backgroundColorResource

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/22 16:31.
 */
class CalendarSelectAdapter : BaseRecyclerViewAdapter<BaseViewHolder, CalendarDayBean>() {
    private var checkedPosition = -1

    private val f0f0f0 = Utils.getColor(R.color.bg_color_f0f0f0)
    private val d9d8d8 = Utils.getColor(R.color.text_color_d9d8d8)
    private val e3e2e2 = Utils.getColor(R.color.text_color_e3e2e2)
    private val c8c5c5 = Utils.getColor(R.color.text_color_c8c5c5)
    private val white = Utils.getColor(R.color.white)

    private var listener: ((dayBean: CalendarDayBean, view: View) -> Unit)? = null
    fun setCalendarChangeListener(listener: (dayBean: CalendarDayBean, view: View) -> Unit) {
        this.listener = listener
    }


    class ViewHolder(val textView: TextView) : BaseViewHolder(textView)
    class NodeViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
    }

    override fun getItemViewType(position: Int): Int =
            if (getItem(position).isNode) {
                ListViewType.TYPE_INTERVAL.type
            } else {
                ListViewType.TYPE_CONTENT.type
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
            if (viewType == ListViewType.TYPE_INTERVAL.type) {
                val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_calendar_select_week)

                NodeViewHolder(view)
            } else {
                val textView = TextView(parent.context)
                textView.apply {
                    layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Utils.widthPixels / 7)
                    gravity = Gravity.CENTER
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getDimension(R.dimen.text_size14).toFloat())
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
                ViewHolder(textView)
            }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val dayBean = getItem(position)
        when (holder) {
            is CalendarSelectAdapter.ViewHolder -> {
                val textView = holder.textView
                textView.apply {
                    setOnClickListener(null)
                    text = dayBean.day.toString()
                    if (dayBean.isLastMonth || dayBean.isNextMonth) {
                        setBackgroundColor(f0f0f0)
                        setTextColor(d9d8d8)
                    } else {
                        if (dayBean.isFuture) {
                            setBackgroundColor(white)
                            setTextColor(e3e2e2)
                        } else {
                            backgroundColorResource = R.color.selector_calendar_day_text_color
                            setTextColor(c8c5c5)
                            setOnClickListener { setSelected(holder.adapterPosition, it) }
                        }
                    }
                    if (dayBean.isChecked) {
                        checkedPosition = holder.adapterPosition
                        backgroundColorResource = R.color.AppThemeColor
                        setTextColor(white)
                    }
                }
            }
            is NodeViewHolder -> {
                holder.tvTime.text = Utils.context.getString(R.string.yyyy_MM, dayBean.year, dayBean.month)
            }
        }
    }

    private fun setSelected(position: Int, view: View) {
        if (position != checkedPosition) {
            if (checkedPosition != -1) {
                getItem(checkedPosition).isChecked = false
                notifyItemChanged(checkedPosition)
            }
            getItem(position).isChecked = true
            notifyItemChanged(position)
        }
        listener?.invoke(getItem(position), view)

    }

}