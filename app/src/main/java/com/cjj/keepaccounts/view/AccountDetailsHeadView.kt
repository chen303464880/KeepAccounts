package com.cjj.keepaccounts.view

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.text.inSpans
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.pop.RadioPop
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.utils.toMoney

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/6 15:43.
 */
class AccountDetailsHeadView(val context: Context, val possess: Boolean) {
    val headView: View = Utils.inflate(R.layout.head_view_account_details, context)
    private val rlContent: RelativeLayout
    private val tvBalance: TextView
    private val tvBalanceHint: TextView
    private val rlYear: RelativeLayout
    private val tvYear: TextView
    private val tvYearHint: TextView
    private val tvExpenditure: TextView
    private val tvIncome: TextView
    val tvDetailsKong: TextView

    private val spanSize = AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12))
    private val colorSize = ForegroundColorSpan(Utils.getColor(R.color.transparent_white))

    var type: Int = 0
    private var radioPop: RadioPop? = null

    private var listener: (year: Int) -> Unit = {}
    fun setOnChangeListener(listener: (year: Int) -> Unit) {
        this.listener = listener
    }

    fun setOnBalanceClickListener(listener: (view: View) -> Unit) {
        tvBalance.setOnClickListener(listener)
    }

    var backgroundColor: Int = 0
        set(value) {
            rlContent.setBackgroundColor(value)
        }

    init {
        tvBalance = headView.findViewById(R.id.tv_balance)
        rlContent = headView.findViewById(R.id.rl_content)
        tvBalanceHint = headView.findViewById(R.id.tv_balance_hint)
        rlYear = headView.findViewById(R.id.rl_year)
        tvYear = headView.findViewById(R.id.tv_year)
        tvYearHint = headView.findViewById(R.id.tv_year_hint)
        tvExpenditure = headView.findViewById(R.id.tv_expenditure)
        tvIncome = headView.findViewById(R.id.tv_income)
        tvDetailsKong = headView.findViewById(R.id.tv_details_kong)
    }

    fun setYears(years: List<Int>) {

        val list = years.map { Utils.context.getString(R.string.xx_year, it) }
        radioPop = RadioPop(context, list)
        radioPop!!.setTitle("选择年份")
        radioPop!!.setSelection(0)
        radioPop!!.setOnChangeListener {
            listener.invoke(years[it])
        }
        rlYear.setOnClickListener {
            radioPop!!.showAsDropDown(tvYear)
        }
    }

    fun setBalance(balance: Double) {
        tvBalance.text = if (possess) balance.toMoney() else (-balance + 0.0).toMoney()
    }


    fun setBalanceHintText(text: CharSequence) {
        tvBalanceHint.text = text
    }

    fun setYearText(text: CharSequence) {
        tvYear.text = text
    }

    fun setYearHintText(text: CharSequence) {
        tvYearHint.text = text
    }

    fun setIncomeText(text: CharSequence) {
        val spannable = SpannableStringBuilder(text)
        spannable.inSpans(spanSize) {
            append("\n")
            if (type == 0) {
                append(Utils.getString(R.string.bank_income))
            } else {
                append(Utils.getString(R.string.accountant_bill_date))
            }
        }
        tvIncome.text = spannable
    }

    fun setExpenditureText(text: CharSequence) {
        val spannable = SpannableStringBuilder(text)
        spannable.inSpans(spanSize, colorSize) {
            append("\n")
            if (type == 0) {
                append(Utils.getString(R.string.expenditure))
            } else {
                append(Utils.getString(R.string.present_credit))
            }
        }
        tvExpenditure.text = spannable
    }

}