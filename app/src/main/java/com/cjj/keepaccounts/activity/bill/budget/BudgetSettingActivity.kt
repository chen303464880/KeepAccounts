package com.cjj.keepaccounts.activity.bill.budget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.tool.EditTextActivity
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.ActivityJumpInfo
import com.cjj.keepaccounts.bean.ListBook
import com.cjj.keepaccounts.dao.ListBookTool
import com.cjj.keepaccounts.enum.InputType
import com.cjj.keepaccounts.pop.RadioPop
import com.cjj.keepaccounts.utils.ToastUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.utils.toMoney
import kotlinx.android.synthetic.main.activity_budget_setting.*


/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/16 15:39.
 */
class BudgetSettingActivity : WhiteActivity<EmptyPresenter>() {


    private val budgetModes = Utils.resources.getStringArray(R.array.budget_mode)

    private val listBook: ListBook by extra()
    private lateinit var newListBook: ListBook

    private var _radioPop: RadioPop? = null
    private val radioPop: RadioPop
        get() {
            if (_radioPop == null) {
                _radioPop = RadioPop(this, budgetModes.toList())
                _radioPop!!.setTitle("预算显示设置")
                _radioPop!!.setSelection(budgetModes.indexOf(ListBookTool.getBudgetModeText(listBook)))
                _radioPop!!.setOnChangeListener {
                    tv_budget_mode.text = budgetModes[it]
                    newListBook.listBudgetShow = ListBookTool.getBudgetMode(budgetModes[it])
                    ListBookTool.update(listBook, newListBook)
                }

            }
            return _radioPop!!
        }


    override fun getContentView(): View = Utils.inflate(R.layout.activity_budget_setting, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityTitleText(getString(R.string.budget_setting))
        setActivityBackText(getString(R.string.budget))
    }

    override fun initView() {
        newListBook = listBook.clone()
        sw_open_budget.isChecked = listBook.listBudgetIshide == 0
        tv_money_budget.text = listBook.listBudget.toMoney()
        tv_budget_mode.text = ListBookTool.getBudgetModeText(listBook)
    }


    override fun initListener() {
        super.initListener()
        sw_open_budget.setOnCheckedChangeListener { _, isChecked ->
            newListBook.listBudgetIshide = if (isChecked) 0 else 1
            ListBookTool.update(listBook, newListBook)
        }
        fl_budget_mode.setOnClickListener {
            radioPop.showAsDropDown(tv_budget_mode)
        }
        fl_money_budget.setOnClickListener {
            val info = ActivityJumpInfo(1, Activity.RESULT_OK, InputType.MONEY.type, getString(R.string.budget_setting), "编辑预算金额", tv_money_budget.text.toString())
            EditTextActivity.openActivity(info)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("contentStr")
            val budget = stringExtra.toDouble()
            if (budget < 0.0) {
                ToastUtils.shortToast("预算不可以小于0")
                return
            }
            newListBook.listBudget = budget
            ListBookTool.update(listBook, newListBook)
            tv_money_budget.text = budget.toMoney()
        }
    }
}