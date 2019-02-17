package com.cjj.keepaccounts.fragment.bill

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.text.inSpans
import butterknife.BindView
import butterknife.OnClick
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.bean.Account
import com.cjj.keepaccounts.bean.CalculatorEventBean
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.bean.RecordDao
import com.cjj.keepaccounts.dialog.SelectAccountDialog
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.EventBusUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.utils.toMoney
import com.cjj.keepaccounts.view.spanColor
import kotlinx.android.synthetic.main.fragment_bill_transfer_accounts.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.append

/**
 * @author chenjunjie
 * Created by CJJ on 2018/2/28 14:26.
 */
class BillTransferAccountsFragment : RecordTypeFragment() {
    override var typeId: Long
        get() = -1L
        set(_) {}
    /**
     * 转出账户
     */
    var expendAccount: Account? = null
    /**
     * 转入账户
     */
    var incomeAccount: Account? = null

    /**
     * 转出账户容器
     */
    @BindView(R.id.fl_expend_account)
    lateinit var flExpendAccount: FrameLayout
    /**
     * 转入账户容器
     */
    @BindView(R.id.fl_income_account)
    lateinit var flIncomeAccount: FrameLayout

    /**
     * 转出账户名字与金额
     */
    @BindView(R.id.tv_income_desc)
    lateinit var tvIncomeDesc: TextView
    @BindView(R.id.tv_income_money)
    lateinit var tvIncomeMoney: TextView
    /**
     * 转入账户名字与金额
     */
    @BindView(R.id.tv_expend_desc)
    lateinit var tvExpendDesc: TextView
    @BindView(R.id.tv_expend_money)
    lateinit var tvExpendMoney: TextView


    /**
     * 是否是编辑状态
     */
    private var isEdit = false

    private lateinit var money: SpannableStringBuilder


    private val selectExpendAccountDialog: SelectAccountDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = SelectAccountDialog(activity!!, "")
        dialog.setOnAccountChangeListener {
            refreshExpendAccount(it)
        }
        dialog
    }


    private val selectIncomeAccountDialog: SelectAccountDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = SelectAccountDialog(activity!!, "")
        dialog.setOnAccountChangeListener {
            refreshIncomeAccount(it)
        }
        dialog
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return Utils.inflate(R.layout.fragment_bill_transfer_accounts, activity!!)
    }

    override fun initView() {
        EventBusUtils.register(this, presenter)

        val record: Record? = arguments?.getParcelable("info")
        isEdit = record != null
        if (isEdit) {
            money = SpannableStringBuilder()
            money.append(Math.abs(record!!.rateMoney).toMoney(), spanColor)

            if (record.typeId == -1L) {
                val first = DaoManager.getRecordDao().queryBuilder()
                        .where(RecordDao.Properties.ActionId.eq(record.actionId))
                        .where(RecordDao.Properties.AccountId.notEq(record.accountId))
                        .unique()
                first.__setDaoSession(DaoManager.daoSession)
                Log.i("TAG", record.rateMoney.toString())

                if (record.rateMoney > 0) {
                    refreshIncomeAccount(record.account)
                    refreshExpendAccount(first.account)
                } else {
                    refreshIncomeAccount(first.account)
                    refreshExpendAccount(record.account)
                }
            } else {
                flExpendAccount.background = getDrawable(Color.WHITE)
                flIncomeAccount.background = getDrawable(Color.WHITE)
                val rateMoney = record.rateMoney
                if (rateMoney != 0.0) {
                    val builder = SpannableStringBuilder()
                    builder.inSpans(ForegroundColorSpan(Utils.getColor(R.color.text_color_655f5f))) {
                        append(Math.abs(rateMoney).toMoney())
                    }
                    tv_expend_money.text = builder
                    tv_income_money.text = builder
                }
            }

        } else {
            flExpendAccount.background = getDrawable(Color.WHITE)
            flIncomeAccount.background = getDrawable(Color.WHITE)
            money = SpannableStringBuilder()
            money.append(Utils.getString(R.string.have_no_money), ForegroundColorSpan(Utils.getColor(R.color.text_color_a39f9f)))
        }
        refreshMoney()

    }

    @OnClick(R.id.fl_expend_account, R.id.fl_income_account)
    fun onClick(view: View) {
        when (view.id) {
            R.id.fl_expend_account -> {
                selectExpendAccountDialog.show()
            }
            R.id.fl_income_account -> {
                selectIncomeAccountDialog.show()
            }
            else -> {

            }
        }
    }


    /**
     * 刷新转入账户
     */
    private fun refreshIncomeAccount(it: Account) {
        incomeAccount = it
        flIncomeAccount.background = getDrawable(Color.parseColor(it.color))
        tvIncomeDesc.text = it.name
        tvIncomeDesc.setTextColor(Color.WHITE)
        refreshMoney()
    }

    /**
     * 刷新转出账户
     */
    private fun refreshExpendAccount(it: Account) {
        expendAccount = it
        flExpendAccount.background = getDrawable(Color.parseColor(it.color))
        tvExpendDesc.text = it.name
        tvExpendDesc.setTextColor(Color.WHITE)
        refreshMoney()
    }

    private fun refreshMoney() {
        val spanStart = money.getSpanStart(spanColor)
        val spanEnd = money.getSpanEnd(spanColor)
        Log.i("TAG", spanStart.toString())
        if (expendAccount != null) {
            if (spanStart != -1) {
                val builder = SpannableStringBuilder()
                builder.append(money)
                builder.setSpan(ForegroundColorSpan(Utils.getColor(R.color.white)), spanStart, spanEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                builder.setSpan(ForegroundColorSpan(Utils.getColor(R.color.transparent_white)), spanEnd, builder.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                tvExpendMoney.text = builder
            } else {
                money = SpannableStringBuilder()
                money.append(Utils.getString(R.string.have_no_money), ForegroundColorSpan(Utils.getColor(R.color.transparent_white)))
                tvExpendMoney.text = money
            }
        } else {
            tvExpendMoney.text = money
        }

        if (incomeAccount != null) {
            if (spanStart != -1) {
                val builder = SpannableStringBuilder()
                builder.append(money)
                builder.setSpan(ForegroundColorSpan(Utils.getColor(R.color.white)), spanStart, spanEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                builder.setSpan(ForegroundColorSpan(Utils.getColor(R.color.transparent_white)), spanEnd, builder.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                tvIncomeMoney.text = builder
            } else {
                money = SpannableStringBuilder()
                money.append(Utils.getString(R.string.have_no_money), ForegroundColorSpan(Utils.getColor(R.color.transparent_white)))
                tvIncomeMoney.text = money
            }
        } else {
            tvIncomeMoney.text = money
        }
    }

    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(result: CalculatorEventBean) {
        money = result.result
        refreshMoney()
    }


    private fun getDrawable(color: Int): Drawable {
        val drawable = GradientDrawable()
        drawable.setColor(color)
        drawable.cornerRadius = Utils.getDimension(R.dimen.radius).toFloat()
        drawable.setStroke(1, Utils.getColor(R.color.divider_color_dedddd))
        return drawable
    }

}