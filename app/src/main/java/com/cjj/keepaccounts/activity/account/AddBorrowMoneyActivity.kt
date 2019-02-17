package com.cjj.keepaccounts.activity.account

import android.app.Activity
import android.content.Intent
import android.view.View
import butterknife.OnClick
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.tool.EditTextActivity
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.Account
import com.cjj.keepaccounts.bean.ActivityJumpInfo
import com.cjj.keepaccounts.bean.Credit
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.dao.CreditTool
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.databinding.ActivityAddBorrowMoneyBinding
import com.cjj.keepaccounts.dialog.MsgDialog
import com.cjj.keepaccounts.dialog.SelectAccountDialog
import com.cjj.keepaccounts.dialog.SelectDateDialog
import com.cjj.keepaccounts.enum.InputType
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.TimeUtils.day
import com.cjj.keepaccounts.utils.TimeUtils.month
import com.cjj.keepaccounts.utils.TimeUtils.year
import com.cjj.keepaccounts.utils.ToastUtils
import com.cjj.keepaccounts.utils.Utils

class AddBorrowMoneyActivity : WhiteActivity<EmptyPresenter>() {

    private lateinit var dataBinding: ActivityAddBorrowMoneyBinding

    private val account: Account by extra()

    //使用的账户
    private var borrowAccount: Account? = null

    private lateinit var credit: Credit
    private lateinit var newCredit: Credit

    private var isEdit: Boolean = false

    private var isBorrow = false


    private val hintDialog: MsgDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = MsgDialog(this)
        dialog.setTitle("提示")
        dialog.setPositiveListener(null)
        dialog
    }


    //账户选择对话框
    private val selectAccountDialog: SelectAccountDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = SelectAccountDialog(this, if (borrowAccount != null) borrowAccount!!.name else "")
        dialog.setOnAccountChangeListener {
            borrowAccount = it
            dataBinding.borrowAccount = it
        }
        dialog
    }


    //借款日期选择框
    private val selectDateDialog: SelectDateDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = SelectDateDialog(this)
        dialog.unlimited = false
        if (newCredit.mTime != 0L) {
            dialog.setDate(newCredit.mTime)
        }
        dialog.setOnDatePositiveListener { year, month, day ->
            newCredit.mTime = TimeUtils.getDayLongTime(year, month - 1, day)
        }
        dialog
    }

    //还款日期选择框
    private val expireSelectDateDialog: SelectDateDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = SelectDateDialog(this)
        dialog.unlimited = true
        if (newCredit.settlementTime != 0L) {
            dialog.setDate(newCredit.settlementTime)
        } else {
            dialog.setDate(newCredit.mTime)
        }
        dialog.setOnDatePositiveListener { year, month, day ->
            val longTime = TimeUtils.getDayLongTime(year, month - 1, day)
            if (longTime < newCredit.mTime && !TimeUtils.isTimeSameDate(longTime, newCredit.mTime)) {
                hintDialog.message = "还款日期不可以小于借款日期"
                hintDialog.show()
            } else {
                newCredit.settlementTime = longTime / 1000
            }
        }
        dialog
    }

    override fun getContentView(): View {
        dataBinding = Utils.inflateDataBinding(R.layout.activity_add_borrow_money, this)
        return dataBinding.root
    }


    override fun initView() {
        isBorrow = account.accountType.indexNum == 12

        val tempCredit: Credit? = getInfo("credit")
        if (tempCredit != null) {
            isEdit = true
            credit = tempCredit
            newCredit = credit.clone()

            val record = credit.records.last { it.typeId == if (isBorrow) -101L else -106L }
            record.__setDaoSession(DaoManager.daoSession)
            borrowAccount = record.account
            dataBinding.borrowAccount = borrowAccount
        } else {
            isEdit = false
            credit = Credit(System.currentTimeMillis())
            newCredit = credit.clone()
        }

        dataBinding.isBorrow = isBorrow
        dataBinding.credit = newCredit


    }

    @OnClick(R.id.fl_name, R.id.fl_money, R.id.fl_account, R.id.fl_time, R.id.fl_expire_time, R.id.fl_content)
    fun onClick(view: View) {
        when (view.id) {
            R.id.fl_name -> {
                val info = ActivityJumpInfo(1, Activity.RESULT_OK, InputType.TEXT.type, getActivityTitleText().toString(), getString(R.string.edit_name), newCredit.dcUName)
                EditTextActivity.openActivity(info)
            }
            R.id.fl_money -> {
                val info = ActivityJumpInfo(2, Activity.RESULT_OK, InputType.MONEY.type, getActivityTitleText().toString(), getString(R.string.edit_money), newCredit.money.toString())
                EditTextActivity.openActivity(info)
            }
            R.id.fl_account -> {
                selectAccountDialog.show()
            }
            R.id.fl_time -> {
//                selectDateDialog.setBirthday(year, month, day)
                selectDateDialog.show()
            }
            R.id.fl_expire_time -> {
                expireSelectDateDialog.show()
            }
            R.id.fl_content -> {
                val info = ActivityJumpInfo(3, Activity.RESULT_OK, InputType.TEXT.type, getActivityTitleText().toString(), getString(R.string.remarks), newCredit.content)
                EditTextActivity.openActivity(info)
            }
            else -> {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                1 -> {
                    val name = data.getStringExtra("contentStr")
                    newCredit.dcUName = name
                }
                2 -> {
                    val moneyStr = data.getStringExtra("contentStr")
                    val money = moneyStr.toDouble()
                    if (isEdit && money < credit.repayMoney) {
                        hintDialog.message = "金额不可以少于已还的金额"
                        hintDialog.show()
                    } else {
                        newCredit.money = money
                    }

                }
                3 -> {
                    val content = data.getStringExtra("contentStr")
                    newCredit.content = content
                }
            }
        }
    }

    override fun onNext() {
        super.onNext()
        if (isEdit) {
            updateCredit()
        } else {
            insertCredit()
        }
        finish()
    }

    private fun insertCredit() {

        if (newCredit.dcUName.isEmpty()) {
            ToastUtils.shortToast("名称不可以为空")
            return
        }

        if (borrowAccount == null) {
            ToastUtils.shortToast("账户不可以为空")
            return
        }


        if (newCredit.money <= 0.0) {
            ToastUtils.shortToast("金额应大于0")
            return
        }


        val actionId = System.currentTimeMillis()
        val record = Record(System.currentTimeMillis())
        record.accountId = borrowAccount!!.uuid
        record.typeId = if (isBorrow) -101L else -106L
        record.rateMoney = if (isBorrow) -newCredit.money else newCredit.money

        record.setDate(year, month - 1, day)

        record.creditID = newCredit.uuid
        record.actionId = actionId

        val borrowRecord = Record(record.uuid + 1L)
        borrowRecord.accountId = account.uuid
        borrowRecord.typeId = if (isBorrow) -102L else -107L
        borrowRecord.rateMoney = if (isBorrow) newCredit.money else -newCredit.money

        borrowRecord.creditID = newCredit.uuid
        borrowRecord.actionId = actionId

        RecordTool.insert(record)
        RecordTool.insert(borrowRecord)

        CreditTool.insert(newCredit)


    }

    private fun updateCredit() {
        if (newCredit.dcUName.isNullOrEmpty()) {
            ToastUtils.shortToast("名称不可以为空")
            return
        }

        if (borrowAccount == null) {
            ToastUtils.shortToast("账户不可以为空")
            return
        }

        if (newCredit.money <= 0.0) {
            ToastUtils.shortToast("金额应大于0")
            return
        }


        val records = credit.records
        val last = records.first()
        val newLast = last.clone()
        newLast.rateMoney = if (isBorrow) -newCredit.money else newCredit.money
        newLast.accountId = borrowAccount!!.uuid
        RecordTool.update(last, newLast)


        val record = records[1]
        val newRecord = record.clone()
        newRecord.rateMoney = if (isBorrow) newCredit.money else -newCredit.money
        newRecord.accountId = account.uuid
        RecordTool.update(record, newRecord)

        CreditTool.update(credit, newCredit)
    }

}
