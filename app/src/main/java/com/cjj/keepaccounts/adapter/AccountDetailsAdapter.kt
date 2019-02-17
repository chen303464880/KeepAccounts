package com.cjj.keepaccounts.adapter

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.Pair
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseExpandableListAdapter
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.inSpans
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.databinding.typeLogo
import com.cjj.keepaccounts.bean.AccountMonthDetails
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.bean.RecordDao
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.dialog.MsgDialog
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.MoneyUtils
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.SlideDeleteView
import org.jetbrains.anko.textColorResource
import java.util.*


/**
 * @author chenjunjie
 * Created by CJJ on 2017/12/18 15:54.
 */
class AccountDetailsAdapter(private val possess: Boolean) : BaseExpandableListAdapter() {

    val openViews = arrayListOf<SlideDeleteView>()

    private val accountDetails = ArrayList<kotlin.Pair<AccountMonthDetails, ArrayList<Record>>>()

    fun setDatas(list: ArrayList<kotlin.Pair<AccountMonthDetails, ArrayList<Record>>>) {
        accountDetails.clear()
        accountDetails.addAll(list)
        notifyDataSetChanged()
    }

    //    private val a6a3a3Span = ForegroundColorSpan(Utils.getColor(R.color.text_color_a6a3a3))
    private val a19e9dSpan = ForegroundColorSpan(Utils.getColor(R.color.text_color_a19e9d))
    private val sizeSpan = AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size09))
    private val size15Span = AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size15))
    private val size11Span = AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size11))
    private val deleteDialog: MsgDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = MsgDialog(ActivityTool.currentActivity())
        dialog.setTitle(Utils.getString(R.string.delete_record_hint))
        dialog.setNegativeListener(null)
        dialog.message = Utils.getString(R.string.delete_record_affirm_hint)
        dialog
    }

    private var childClickListener: ((groupPosition: Int, childPosition: Int, item: Record) -> Unit)? = null

    fun setOnChildClickListener(listener: (groupPosition: Int, childPosition: Int, item: Record) -> Unit) {
        this.childClickListener = listener
    }


    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    override fun hasStableIds(): Boolean = true

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()
    override fun getGroupCount(): Int = accountDetails.size
    override fun getGroup(groupPosition: Int): AccountMonthDetails = accountDetails[groupPosition].first


    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: GroupViewHolder
        val view: View
        if (convertView == null) {
            view = Utils.inflate(R.layout.list_item_account_details_group, parent!!.context)
            viewHolder = GroupViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as GroupViewHolder
        }

        val group = getGroup(groupPosition)
        viewHolder.tvMonth.text = Utils.context.getString(R.string.xx_month, group.month + 1)
        val span = SpannableStringBuilder()
        span.bold {
            append(Utils.context.getString(R.string.xx_month, group.month + 1))
        }
        span.inSpans(sizeSpan, a19e9dSpan) {
            append("\n")
            val monthDayNum = TimeUtils.getMonthMax(group.year, group.month)
            append(Utils.context.getString(R.string.date_interval, group.month + 1, 1, group.month + 1, monthDayNum))
        }
        viewHolder.tvMonth.text = span
        if (possess) {
            val str = "${MoneyUtils.formatMoney(group.monthIncome)}\n${MoneyUtils.formatMoney(group.monthExpenditure)}"
            viewHolder.tvMoney.text = str
            viewHolder.tvMoney.translationX = -Utils.widthPixels.toFloat() / 3
        } else {
            viewHolder.tvMoney.translationX = 0F
            val moneySpan = SpannableStringBuilder()
            moneySpan.inSpans(size15Span) {
                append(MoneyUtils.formatMoney(-group.monthExpenditure))
            }
            moneySpan.append("\n")
            moneySpan.inSpans(size11Span) {
                append(Utils.getString(R.string.the_current_bill))
            }
            viewHolder.tvMoney.text = moneySpan
        }

        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()
    override fun getChildrenCount(groupPosition: Int): Int = accountDetails[groupPosition].second.size
    override fun getChild(groupPosition: Int, childPosition: Int): Record = accountDetails[groupPosition].second[childPosition]
    override fun getChildType(groupPosition: Int, childPosition: Int): Int =
            if (getChild(groupPosition, childPosition).isNode) 0 else 1

    override fun getChildTypeCount(): Int = 2


    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val type = getChildType(groupPosition, childPosition)
        if (type == 0) {
            val child = getChild(groupPosition, childPosition)
            val textView: TextView = if (convertView == null) {
                getDayView()
            } else {
                convertView as TextView
            }
            textView.text = Utils.context.getString(R.string.yyyy_MM_dd, child.year, child.month + 1, child.day)
            view = textView
        } else {
            val viewHolder: ChildViewHolder
            if (convertView == null) {
                view = Utils.inflate(R.layout.list_item_account_details_child, parent!!.context)
                viewHolder = ChildViewHolder(view)
                view.tag = viewHolder
            } else {
                view = convertView
                viewHolder = view.tag as ChildViewHolder
            }
            val child = getChild(groupPosition, childPosition)

            viewHolder.tvType.typeLogo(child.recordType.imgSrcId, Utils.dip2px(30F))


            val typeDesc = child.recordType.typeDesc
            val desc = if (child.typeId != -1L) {
                child.content
            } else {
                val first = DaoManager.getRecordDao().queryBuilder()
                        .where(RecordDao.Properties.ActionId.eq(child.actionId))
                        .where(RecordDao.Properties.AccountId.notEq(child.accountId))
                        .unique()
                first.__setDaoSession(DaoManager.daoSession)
                if (child.rateMoney > 0) {
                    "[${first.account.name}转入]"
                } else {
                    "[转出到${first.account.name}]"
                }

            }
            //有描述的时候,添加描述
            if (!desc.isNullOrEmpty()) {
                val spannableString = SpannableString("$typeDesc\n$desc")
                spannableString.setSpan(ForegroundColorSpan(Utils.getColor(R.color.text_color_9e9b9b)), typeDesc.length + 1, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableString.setSpan(AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12)), typeDesc.length + 1, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                viewHolder.tvType.text = spannableString
            } else {
                viewHolder.tvType.text = typeDesc
                viewHolder.tvType.gravity = Gravity.CENTER_VERTICAL
            }
            val isIncoming = child.recordType.isIncoming
            if (isIncoming == 1 || (isIncoming == -1 && child.rateMoney > 0)) {
                viewHolder.tvMoney.textColorResource = R.color.AppThemeColor
                val str = "+" + Utils.context.getString(R.string.format_money, child.rateMoney)
                viewHolder.tvMoney.text = str
            } else {
                viewHolder.tvMoney.textColorResource = R.color.text_color_655f5f
                val str = "-" + Utils.context.getString(R.string.format_money, Math.abs(child.rateMoney))
                viewHolder.tvMoney.text = str
            }

            viewHolder.deleteView.setOnSlideListener(object : SlideDeleteView.SlideListener {
                override fun onStartOpen(mSlideDeleteView: SlideDeleteView) {
                    close()
                }

                override fun onStartClose(mSlideDeleteView: SlideDeleteView) {

                }

                override fun onOpen(mSlideDeleteView: SlideDeleteView) {
                    openViews.add(mSlideDeleteView)
                }

                override fun onClose(mSlideDeleteView: SlideDeleteView) {
                    openViews.remove(mSlideDeleteView)
                }

                override fun onDraging(mSlideDeleteView: SlideDeleteView, mSlideOffset: Float) {

                }
            })
            viewHolder.deleteView.setOnDeleteListener {
                deleteDialog.setPositiveListener {
                    RecordTool.delete(child)
                }.show()
            }
            viewHolder.flContent.setOnClickListener {
                if (isOpen()) {
                    close()
                } else {
                    childClickListener?.invoke(groupPosition, childPosition, child)
                }
            }
        }
        return view
    }

    fun isOpen(): Boolean = openViews.size != 0

    fun close() {
        openViews.forEach {
            it.close(true)
        }
        openViews.clear()
    }


    private fun getDayView(): TextView {
        val textView = TextView(Utils.context)
        val layoutParams = AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Utils.dip2px(30F))
        textView.layoutParams = layoutParams
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getDimension(R.dimen.text_size12).toFloat())
        textView.setTextColor(Utils.getColor(R.color.text_color_9e9b9b))
        textView.setBackgroundColor(Utils.getColor(R.color.divider_color_f4f6f4))
        textView.setPadding(Utils.getDimension(R.dimen.start), 0, 0, 0)
        return textView
    }

    private class GroupViewHolder(itemView: View) {
        val tvMonth: TextView = itemView.findViewById(R.id.tv_month)
        val tvMoney: TextView = itemView.findViewById(R.id.tv_income)
    }

    private class ChildViewHolder(itemView: View) {
        val deleteView: SlideDeleteView = itemView.findViewById(R.id.delete_view)
        val tvType: TextView = itemView.findViewById(R.id.tv_type)
        val tvMoney: TextView = itemView.findViewById(R.id.tv_money)
        val flContent: FrameLayout = itemView.findViewById(R.id.fl_content)
    }


}