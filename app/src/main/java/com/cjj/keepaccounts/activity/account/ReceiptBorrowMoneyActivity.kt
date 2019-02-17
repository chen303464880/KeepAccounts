package com.cjj.keepaccounts.activity.account

import android.app.Activity
import android.content.Intent
import android.support.annotation.Nullable
import android.view.View
import butterknife.OnClick
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.tool.EditTextActivity
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.*
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.dialog.SelectAccountDialog
import com.cjj.keepaccounts.dialog.SelectDateDialog
import com.cjj.keepaccounts.enum.InputType
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.*
import kotlinx.android.synthetic.main.activity_receipt_borrow_money.*


class ReceiptBorrowMoneyActivity : WhiteActivity<EmptyPresenter>() {


    private var credit by extra<Credit>("credit")

    private var record by extra<Record?>("record")

    //利息账户
    private lateinit var interestRecord: Record
    //还款账户
    private lateinit var refundRecord: Record
    //收款账户
    private lateinit var gatheringRecord: Record

    private lateinit var account: Account
    private  var borrowAccount by extra<Account>("borrowAccount")

    private var isEdit = false

    private var isLend = false

    private var year: Int = TimeUtils.year
    private var month: Int = TimeUtils.month
    private var day: Int = TimeUtils.day

    private lateinit var paymentAccount: Account


    override fun getContentView(): View = Utils.inflate(R.layout.activity_receipt_borrow_money, this)


    /**
     * 剩余的金额
     */
    private var balance: Double = 0.0
    /**
     * 利息
     */
    private var interest: Double = 0.0

    private var allDebt = 0.0

    override fun initView() {
        showTitleLine()

        isLend = credit.isLend

        isEdit = record != null

        if (!isEdit) {
            val record = credit.borrowRecord
            account = record.account



            paymentAccount = account


            LogUtils.i(credit.toString())
            LogUtils.list(credit.records)

            allDebt = credit.money

            balance = allDebt - credit.repayMoney
            LogUtils.i("全部的金额$allDebt")
            LogUtils.i("剩余的金额$balance")

            var uuidLong = System.currentTimeMillis()
            val actionId = System.currentTimeMillis()
            interestRecord = Record(uuidLong++)
            interestRecord.typeId = credit.interestId
            interestRecord.creditID = credit.uuid
            interestRecord.actionId = actionId
            interestRecord.accountId = account.uuid

            refundRecord = Record(uuidLong++)
            refundRecord.typeId = credit.repayPropertyId
            refundRecord.creditID = credit.uuid
            refundRecord.actionId = actionId
            refundRecord.accountId = account.uuid
            refundRecord.rateMoney = if (isLend) balance else -balance

            gatheringRecord = Record(uuidLong + 1)
            gatheringRecord.typeId = credit.repayDebtId
            gatheringRecord.creditID = credit.uuid
            gatheringRecord.actionId = actionId
            gatheringRecord.accountId = borrowAccount.uuid
            gatheringRecord.rateMoney = if (isLend) -balance else balance


            tv_money.text = Math.abs(balance).toMoney()
            tv_interest.text = MoneyUtils.formatMoney(0F)
            tv_account.text = account.name
        } else {


            record!!.__setDaoSession(DaoManager.daoSession)
            val records = DaoManager.getRecordDao().queryBuilder()
                    .where(RecordDao.Properties.ActionId.eq(record!!.actionId))
                    .list()
            records.forEach {
                when (it.typeId) {
                    credit.interestId -> {
                        interestRecord = it
                        interest = it.rateMoney
                    }
                    credit.repayPropertyId -> {
                        refundRecord = it
                    }
                    credit.repayDebtId -> {
                        gatheringRecord = it
                    }
                }
            }

            allDebt = credit.money
            balance = credit.money - credit.repayMoney

            paymentAccount = record!!.account
            //
            account = record!!.account
            tv_money.text = Math.abs(refundRecord.rateMoney).toString()
            tv_interest.text = Math.abs(interestRecord.rateMoney).toMoney()
            tv_account.text = account.name

            tv_content.text = record!!.content

            year = TimeUtils.getYear(gatheringRecord.rTime)
            month = TimeUtils.getMonth(gatheringRecord.rTime)
            day = TimeUtils.getDay(gatheringRecord.rTime)

        }
        tv_time.text = getString(R.string.yyyy_MM_dd, year, month, day)


    }

    @Nullable
    @OnClick(R.id.fl_money, R.id.fl_interest, R.id.fl_account, R.id.fl_time, R.id.fl_content)
    fun onClick(view: View) {
        when (view.id) {
            R.id.fl_money -> {
                val info = ActivityJumpInfo(1, Activity.RESULT_OK, InputType.MONEY.type, getActivityTitleText().toString(), getString(R.string.edit_money), tv_money.text.toString())
                EditTextActivity.openActivity(info)
            }
            R.id.fl_interest -> {
                val info = ActivityJumpInfo(2, Activity.RESULT_OK, InputType.MONEY.type, getActivityTitleText().toString(), getString(R.string.edit_money), tv_interest.text.toString())
                EditTextActivity.openActivity(info)
            }
            R.id.fl_account -> {
                selectAccountDialog.show()
            }

            R.id.fl_time -> {
                selectDateDialog.setBirthday(year, month, day)
                selectDateDialog.show()
            }

            R.id.fl_content -> {
                val info = ActivityJumpInfo(3, Activity.RESULT_OK, InputType.TEXT.type, getActivityTitleText().toString(), getString(R.string.remarks), tv_content.text.toString())
                EditTextActivity.openActivity(info)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                1 -> {
                    var money = data.getStringExtra("contentStr").toDouble()

                    if (money > balance) {
                        interest = money - balance
                        money = balance
                    }

                    tv_money.text = Math.abs(money).toMoney()
                    tv_interest.text = Math.abs(interest).toMoney()
                }
                2 -> {

                    val interestStr = data.getStringExtra("contentStr")
                    interest = interestStr.toDouble()
                    tv_interest.text = interestStr
                }
                3 -> {
                    val content = data.getStringExtra("contentStr")
                    tv_content.text = content
                    interestRecord.content = content
                    refundRecord.content = content
                    gatheringRecord.content = content
                }
            }
        }
    }

    override fun onNext() {
        super.onNext()
        val money = tv_money.text.toString().toDouble()
        if (money == 0.0) {
            ToastUtils.shortToast("金额不可以为0")
            return
        }

        if (isEdit) {
            update(money)
        } else {
            insert(money)
        }
    }

    private fun insert(money: Double) {
        interestRecord.rateMoney = if (isLend) interest else -interest
        interestRecord.setDate(year, month - 1, day)
        refundRecord.rateMoney = if (isLend) money else -money
        refundRecord.setDate(year, month - 1, day)
        gatheringRecord.rateMoney = if (isLend) -money else money
        gatheringRecord.setDate(year, month - 1, day)

        RecordTool.insert(interestRecord)
        RecordTool.insert(refundRecord)
        RecordTool.insert(gatheringRecord)

        finish()
    }

    private fun update(money: Double) {
        val newInterestRecord = interestRecord.clone()
        newInterestRecord.rateMoney = if (isLend) interest else -interest
        newInterestRecord.accountId = paymentAccount.uuid
        newInterestRecord.isDeleted = if (newInterestRecord.rateMoney == 0.0) 1 else 0
        newInterestRecord.setDate(year, month - 1, day)
        RecordTool.update(interestRecord, newInterestRecord)

        val newRefundRecord = refundRecord.clone()
        newRefundRecord.rateMoney = if (isLend) money else -money
        newRefundRecord.accountId = paymentAccount.uuid
        newRefundRecord.setDate(year, month - 1, day)
        RecordTool.update(refundRecord, newRefundRecord)

        val newGatheringRecord = gatheringRecord.clone()
        newGatheringRecord.rateMoney = if (isLend) -money else money
        newGatheringRecord.accountId = borrowAccount.uuid
        newGatheringRecord.setDate(year, month - 1, day)
        RecordTool.update(gatheringRecord, newGatheringRecord)



        finish()
    }


    private val selectAccountDialog: SelectAccountDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = SelectAccountDialog(this, account.name)
        dialog.setOnAccountChangeListener {
            paymentAccount = it
            tv_account.text = it.name
        }
        dialog
    }

    private val selectDateDialog: SelectDateDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = SelectDateDialog(this)
        dialog.setOnDatePositiveListener { year, month, day ->
            this@ReceiptBorrowMoneyActivity.year = year
            this@ReceiptBorrowMoneyActivity.month = month
            this@ReceiptBorrowMoneyActivity.day = day

            tv_time.text = getString(R.string.yyyy_MM_dd, year, month, day)
        }
        dialog
    }
}
