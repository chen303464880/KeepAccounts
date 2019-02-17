package com.cjj.keepaccounts.activity.bill.budget

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.tool.EditTextActivity
import com.cjj.keepaccounts.adapter.BudgetClassifyEditAdapter
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.ActivityJumpInfo
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.bean.SameCityMainType
import com.cjj.keepaccounts.dao.BudgetTool
import com.cjj.keepaccounts.dao.RecordTypeTool
import com.cjj.keepaccounts.enum.InputType
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.utils.ToastUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.utils.toMoney
import kotlinx.android.synthetic.main.activity_budget_classify_edit.*

/**
 * @author CJJ
 * Created by CJJ on 2018/7/6 11:19.
 */
class BudgetClassifyEditActivity : WhiteActivity<EmptyPresenter>() {

    private val sameCityType: SameCityMainType by extra()
    private val adapter: BudgetClassifyEditAdapter = BudgetClassifyEditAdapter()

    override fun getContentView(): View = Utils.inflate(R.layout.activity_budget_classify_edit, this)

    override fun initView() {
        RecordTypeTool.addOnDaoChangeListener(recordTypeDaoChangeListener, presenter)
        setActivityTitleText(getString(R.string.type_budget, sameCityType.typeName))
        setActivityBackText(getString(R.string.back))
        setNextText(getString(R.string.accomplish))
        tv_type_name.text = sameCityType.typeName
        tv_money.text = sameCityType.budget.amount.toMoney()
        rv_budget_child.adapter = adapter
        adapter.setData(sameCityType.recordTypes)
        rv_budget_child.layoutManager = GridLayoutManager(this, 5)

    }

    override fun initListener() {
        super.initListener()

        fl_money.setOnClickListener {
            val activityInfo = ActivityJumpInfo(8888, Activity.RESULT_OK, InputType.MONEY.type, getActivityTitleText().toString(), getString(R.string.edit_money), tv_money.text.toString())
            EditTextActivity.openActivity(activityInfo)
        }

        fl_add_child.setOnClickListener {
            ActivityTool.skipActivity<AddBudgetChildClassifyActivity>()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                8888 -> {
                    val money = data.getStringExtra("contentStr")
                    if (money.toDouble() < sameCityType.childBudget) {
                        ToastUtils.shortToast("不可以小于子分类预算之和")
                        return
                    }
                    tv_money.text = money

                }
                else -> {

                }
            }

        }
    }

    override fun onNext() {
        super.onNext()
        val budget = sameCityType.budget.clone()
        budget.amount = tv_money.text.toString().toDouble()
        BudgetTool.update(sameCityType.budget, budget)
        finish()
    }

    private val recordTypeDaoChangeListener = object : OnDaoChangeListener<RecordType> {
        override fun onInsertEntity(entity: RecordType) {
        }

        override fun onUpdateEntity(oldEntity: RecordType, newEntity: RecordType) {
            val budgetTypeId = sameCityType.budget.uuid
            if ((oldEntity.budgetType == budgetTypeId || newEntity.budgetType == budgetTypeId)
                    && newEntity.budgetType != oldEntity.budgetType) {
                if (newEntity.budgetType == sameCityType.budget.uuid) {
                    adapter.data.forEachIndexed { index, recordType ->
                        if (newEntity.orderIndex < recordType.orderIndex) {
                            adapter.insertData(index, newEntity)
                            sameCityType.recordTypes.add(index, newEntity)
                            return
                        }
                    }
                    adapter.insertData(newEntity)
                    sameCityType.recordTypes.add(newEntity)
                } else {
                    adapter.data.forEachIndexed { index, recordType ->
                        if (newEntity.uuid == recordType.uuid) {
                            adapter.removeData(index)
                            sameCityType.recordTypes.removeAt(index)
                            return
                        }
                    }
                }
            }
        }

        override fun onDeleteEntity(entity: RecordType) {
        }
    }
}