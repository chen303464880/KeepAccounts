package com.cjj.keepaccounts.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.utils.Utils

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/6 16:30.
 */
class BorrowMoneyDetailsHeadView(context: Context) {
    val headView: View = Utils.inflate(R.layout.head_view_borrow_money_details, context)
    private val flHead: FrameLayout = headView.findViewById(R.id.fl_head)
    private val tvDebt: TextView = headView.findViewById(R.id.tv_debt)
    private val tvInterest: TextView = headView.findViewById(R.id.tv_interest)
    private val tvAllDebt: TextView = headView.findViewById(R.id.tv_all_debt)
    private val tvNameKey: TextView = headView.findViewById(R.id.tv_name_key)
    private val tvName: TextView = headView.findViewById(R.id.tv_name)
    private val tvTimeKey: TextView = headView.findViewById(R.id.tv_time_key)
    private val tvTime: TextView = headView.findViewById(R.id.tv_time)
    private val tvAccount: TextView = headView.findViewById(R.id.tv_account)
    private val tvAccountKey: TextView = headView.findViewById(R.id.tv_account_key)
    private val tvContent: TextView = headView.findViewById(R.id.tv_content)
    private val tvContentKey: TextView = headView.findViewById(R.id.tv_content_key)
    val tvDetailsKong: TextView = headView.findViewById(R.id.tv_details_kong)


    var backgroundColor = 0
        set(value) {
            flHead.setBackgroundColor(value)
        }

    var debt: CharSequence
        get() = tvDebt.text
        set(value) {
            tvDebt.text = value
        }

    var interest: CharSequence
        get() = tvInterest.text
        set (value) {
            tvInterest.text = value
        }

    var allDebt: CharSequence
        get() = tvAllDebt.text
        set (value) {
            tvAllDebt.text = value
        }

    var nameKey: CharSequence
        get() = tvNameKey.text
        set (value) {
            tvNameKey.text = value
        }

    var name: CharSequence
        get() = tvName.text
        set (value) {
            tvName.text = value
        }

    var time: CharSequence
        get() = tvTime.text
        set (value) {
            tvTime.text = value
        }

    var timeKey: CharSequence
        get() = tvTimeKey.text
        set (value) {
            tvTimeKey.text = value
        }

    var account: CharSequence
        get() = tvAccount.text
        set (value) {
            tvAccount.text = value
        }

    var accountKey: CharSequence
        get() = tvAccountKey.text
        set (value) {
            tvAccountKey.text = value
        }

    var content: CharSequence
        get() = tvContent.text
        set (value) {
            tvContent.text = value
        }

    var contentKey: CharSequence
        get() = tvContentKey.text
        set (value) {
            tvContentKey.text = value
        }
}