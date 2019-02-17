package com.cjj.keepaccounts.activity.account

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.annotation.ColorInt
import android.view.View
import butterknife.OnClick
import com.cjj.keepaccounts.MainActivity
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.tool.EditTextActivity
import com.cjj.keepaccounts.activity.tool.SelectColorActivity
import com.cjj.keepaccounts.adapter.SelectAccountTypeAdapter
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.bean.*
import com.cjj.keepaccounts.dao.AccountDaoTool
import com.cjj.keepaccounts.databinding.ActivityAddAccountBinding
import com.cjj.keepaccounts.dialog.MsgDialog
import com.cjj.keepaccounts.dialog.RadioDialog
import com.cjj.keepaccounts.enum.InputType
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.LogoManager
import com.cjj.keepaccounts.mvp.activity.account.addaccount.CAddAccount
import com.cjj.keepaccounts.mvp.activity.account.addaccount.PAddAccount
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.drawableEnd
import com.cjj.keepaccounts.view.drawableStart
import kotlinx.android.synthetic.main.activity_add_account.*
import java.math.BigDecimal

/**
 * @author
 * 添加新账户的页面
 */
class AddAccountActivity : WhiteActivity<PAddAccount>(), CAddAccount.View {

    /**
     * 账户类型
     */
    private var accountType by extra<AccountType>("accountType")
    /**
     * 银行信息
     */
    private var bankInfo by extra<BankInfoBean?>("bankInfo")
    /**
     * 账户信息
     */
    private lateinit var account: Account
    private lateinit var newAccount: Account
    /**
     * 当前账户颜色
     */
    private var colorInt = 0
        set(value) {
            field = value
            account.color = "#${Integer.toHexString(value)}"
        }
    /**
     * 是否是编辑已有账户
     */
    private var isEdit: Boolean = false

    private lateinit var dataBinding: ActivityAddAccountBinding

    private var days: ArrayList<String>? = null
    private fun getDays(): List<String> {
        if (days == null) {
            days = (1..28).map { getString(R.string.xx_day, it) } as ArrayList<String>
            days!!.add("月末")
        }
        return days!!
    }

    private val billDateDialog: RadioDialog by lazy(LazyThreadSafetyMode.NONE) {
        val billDateDialog = RadioDialog(this)
        billDateDialog.setTitle(Utils.getString(R.string.accountant_bill_date))
        billDateDialog.data = getDays()
        billDateDialog.selection = if (account.billDay == -1) getDays().size - 1 else if (account.billDay == 0) 0 else account.billDay - 1
        billDateDialog.setOnItemChangeListener { position ->
            account.billDay = if (position + 1 == getDays().size) -1 else position + 1
            settingBillDay()
            if (isEdit) {
                newAccount.billDay = account.billDay
                AccountDaoTool.update(account, newAccount)
            }
        }
        billDateDialog
    }

    private val dueDateDialog: RadioDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dueDateDialog = RadioDialog(this)
        dueDateDialog.setTitle(Utils.getString(R.string.due_date))
        dueDateDialog.data = getDays()
        dueDateDialog.selection = if (account.returnDay == -1) getDays().size - 1 else if (account.returnDay == 0) 0 else account.returnDay - 1
        dueDateDialog.setOnItemChangeListener { position ->
            account.returnDay = if (position + 1 == getDays().size) -1 else position + 1
            settingReturnDay()
            if (isEdit) {
                newAccount.returnDay = account.returnDay
                AccountDaoTool.update(account, newAccount)
            }
        }
        dueDateDialog
    }

    private val deleteDialog: MsgDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = MsgDialog(this)
        dialog.setTitle("删除账户提示")
        dialog.setNegativeListener(null)
        dialog.message = "删除后不可恢复，你确定要删除该账户吗?"
        dialog
    }

    override fun getContentView(): View {
        dataBinding = Utils.inflateDataBinding(R.layout.activity_add_account, this)
        return dataBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //获取数据
        val tempAccount = getInfo<Account?>("account")

        if (tempAccount != null) {
            isEdit = true
            account = tempAccount
            newAccount = account.clone()
        } else {
            isEdit = false
            account = Account()
            account.typeId = accountType.typeId
            account.name = accountType.name
        }

        super.onCreate(savedInstanceState)
        //初始化
        if (isEdit) {
            if (account.uuid != 1L) {
                setNextTextDrawable(Utils.getDrawable(R.mipmap.account_add_shanchu))
            }
        } else {
            setNextText(getString(R.string.ok))
        }
        //初始化标题
        setActivityBackText(getString(R.string.back))
        setActivityTitleText(if (isEdit) getString(R.string.settings) else getString(R.string.add_account_type, accountType.name))
    }

    override fun initView() {
        dataBinding.account = account
        dataBinding.accountType = accountType
        dataBinding.bankInfo = bankInfo
        dataBinding.isEdit = isEdit
//        if (isEdit) {
//            fl_account_type.visibility = View.VISIBLE
//            tv_account_type.text = accountType.name
//        }

        //控制某些选项不出现
        when (accountType.indexNum) {
            1, 4, 5, 6, 7, 8, 9 -> {
//                fl_issuing_bank.visibility = View.GONE
//                fl_extend_credit.visibility = View.GONE
//                fl_accountant_bill_date.visibility = View.GONE
//                fl_due_date.visibility = View.GONE
            }
            10, 11 -> {//京东白条、蚂蚁花呗
//                fl_issuing_bank.visibility = View.GONE
//                tv_money_desc.text = getString(R.string.debt)
//                tv_extend_credit.text = getString(R.string.format_money, if (isEdit) account.creditLimit else 0F)
                settingBillDay()
                settingReturnDay()
            }
            2 -> {//储蓄卡
                //设置发卡行的名字
                bankInfo?.backName?.let {
                    account.bankName = bankInfo?.backName
                }
//                tv_issuing_bank.text = if (isEdit) account.bankName else bankInfo!!.backName
                if (isEdit) {
                    tv_issuing_bank.drawableEnd = Utils.getDrawable(R.mipmap.youjiantou_baobiao)
                }
//                fl_accountant_bill_date.visibility = View.GONE
//                fl_due_date.visibility = View.GONE
//                fl_extend_credit.visibility = View.GONE
            }
            3 -> {//信用卡
                //设置发卡行的名字
                bankInfo?.backName?.let {
                    account.bankName = bankInfo?.backName
                }
//                tv_issuing_bank.text = if (isEdit) account.bankName else bankInfo!!.backName
                if (isEdit) {
                    tv_issuing_bank.drawableEnd = Utils.getDrawable(R.mipmap.youjiantou_baobiao)
                }
//                tv_money_desc.text = getString(R.string.debt)
//                tv_extend_credit.text = getString(R.string.format_money, if (isEdit) account.creditLimit else 0F)
                settingBillDay()
                settingReturnDay()
            }
            12, 13 -> {//借入、借出
//                fl_issuing_bank.visibility = View.GONE
//                fl_extend_credit.visibility = View.GONE
//                fl_money.visibility = View.GONE
//                fl_accountant_bill_date.visibility = View.GONE
//                fl_due_date.visibility = View.GONE
            }
        }


        tv_money.text = getString(R.string.format_money, when (accountType.indexNum) {
            3, 10, 11 -> {
                if (isEdit) -account.money else 0.0
            }
            else -> {
                if (isEdit) account.money else 0.0
            }
        })

        //设置账户颜色图标
        colorInt = if (isEdit) {
            Color.parseColor(account.color)
        } else {
            //如果bankInfo不为空,则为新建银行卡,使用银行的颜色
            if (bankInfo != null) {
                bankInfo!!.color
            } else {
                SelectAccountTypeAdapter.colorList[accountType.idImg]
            }
        }
        val leftDrawable = getAccountColor(colorInt)
        val rightDrawable = Utils.getDrawable(R.mipmap.youjiantou_baobiao)
        tv_select_account_color.drawableStart = leftDrawable
        tv_select_account_color.drawableEnd = rightDrawable
    }

    override fun initListener() {
        super.initListener()
        if (isEdit) {
            if (accountType.indexNum == 2 || accountType.indexNum == 3) {
                fl_issuing_bank.setOnClickListener {
                    val intent = Intent(this, SelectBankActivity::class.java)
                    intent.putExtra("isEdit", true)
                    intent.putExtra("accountType", accountType)
                    startActivityForResult(intent, 5)
                }
            }
        }
        if (accountType.indexNum == 3 || accountType.indexNum == 10 || accountType.indexNum == 11) {
            fl_accountant_bill_date.setOnClickListener {
                billDateDialog.show()
            }
            fl_due_date.setOnClickListener {
                dueDateDialog.show()
            }
        }
    }

    private fun settingBillDay() {
        if (account.billDay == 0) {
            return
        }
        val str =
                if (account.billDay == -1) {
                    getDays()[getDays().size - 1]
                } else {
                    getDays()[account.billDay - 1]
                }
        tv_accountant_bill_date.text = str
    }

    private fun settingReturnDay() {
        if (account.returnDay == 0) {
            return
        }
        val str2 =
                if (account.returnDay == -1) {
                    getDays()[getDays().size - 1]
                } else {
                    getDays()[account.returnDay - 1]
                }
        tv_due_date.text = str2
    }

    @OnClick(R.id.fl_account_name, R.id.fl_money, R.id.fl_extend_credit, R.id.fl_select_account_color)
    fun onClick(view: View) {
        when (view.id) {
            R.id.fl_account_name -> {
                val info = ActivityJumpInfo(1, Activity.RESULT_OK, InputType.TEXT.type, getString(R.string.new_account), "编辑账户名称", tv_account_name.text.toString())
                EditTextActivity.openActivity(info)
            }
            R.id.fl_money -> {
                val info = ActivityJumpInfo(2, Activity.RESULT_OK, InputType.MONEY.type, getString(R.string.new_account), "编辑账户金额", tv_money.text.toString())
                EditTextActivity.openActivity(info)
            }
            R.id.fl_extend_credit -> {
                val info = ActivityJumpInfo(3, Activity.RESULT_OK, InputType.MONEY.type, getString(R.string.new_account), "编辑账户额度", tv_extend_credit.text.toString())
                EditTextActivity.openActivity(info)
            }
            R.id.fl_select_account_color -> {
                val imgId = if (bankInfo != null) {
                    bankInfo!!.logoRes
                } else {
                    LogoManager.getHomeLogo(accountType.idImg)
                }
                val info = SelectColorInfo(4, Activity.RESULT_OK, colorInt, accountType.name, imgId)
                val activityInfo = ActivityInfoBean(getString(R.string.select_account_color), getString(R.string.new_account))
                ActivityTool.skipActivityForResult<SelectColorActivity>(4, info, activityInfo)

            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        when (requestCode) {
            1 -> {
                val name = data.getStringExtra("contentStr")

                if (isEdit) {
                    newAccount.name = name
                    AccountDaoTool.update(account, newAccount)
                }

                account.name = name
            }
            2 -> {
                var money = data.getStringExtra("contentStr").toDouble()

                money = when (accountType.indexNum) {
                    3, 10, 11 -> {
                        -money
                    }
                    else -> {
                        money
                    }
                }
                if (isEdit) {
                    val priceSpread = (BigDecimal(money) - BigDecimal(account.money)).toDouble()
                    AccountDaoTool.changeAmount(account, priceSpread)
                }
                account.money = money
            }
            3 -> {
                val extendCreditStr = data.getStringExtra("contentStr")
                val extendCredit = BigDecimal(extendCreditStr).toFloat()
                if (isEdit) {
                    newAccount.creditLimit = extendCredit
                    AccountDaoTool.update(account, newAccount)
                }
                account.creditLimit = extendCredit
            }
            4 -> {
                colorInt = data.getIntExtra("color", colorInt)

                tv_select_account_color.drawableStart = getAccountColor(colorInt)
                tv_select_account_color.drawableEnd = Utils.getDrawable(R.mipmap.youjiantou_baobiao)


                if (isEdit) {
                    newAccount.color = "#${Integer.toHexString(colorInt)}"
                    AccountDaoTool.update(account, newAccount)
                }
            }
            5 -> {
                val bankInfo = data.getParcelableExtra<BankInfoBean>("bankInfo")
                colorInt = bankInfo.color
                tv_select_account_color.drawableStart = getAccountColor(colorInt)

                if (isEdit) {
                    newAccount.bankName = bankInfo.backName
                    newAccount.color = "#${Integer.toHexString(colorInt)}"
                    AccountDaoTool.update(account, newAccount)
                }
                account.bankName = bankInfo.backName
            }
        }
    }


    override fun onNext() {
        super.onNext()
        if (isEdit) {
            delete()
        } else {
            insert()
        }

    }

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission", "HardwareIds")
    private fun insert() {
        presenter.insert(account)
        ActivityTool.batchFinishActivity(MainActivity::class.java)
    }

    private fun delete() {
        deleteDialog.setPositiveListener {
            presenter.delete(account)
            ActivityTool.batchFinishActivity(MainActivity::class.java)
        }.show()
    }


    private fun getAccountColor(@ColorInt colorInt: Int): Drawable {
        val drawable = GradientDrawable()
        drawable.setColor(colorInt)
        drawable.cornerRadius = Utils.dip2px(2F).toFloat()
        val length = Utils.dip2px(20F)
        drawable.setSize(length, length)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        return drawable
    }


}
