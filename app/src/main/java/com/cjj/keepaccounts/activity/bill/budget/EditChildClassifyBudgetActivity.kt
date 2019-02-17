package com.cjj.keepaccounts.activity.bill.budget

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.tool.EditTextActivity
import com.cjj.keepaccounts.adapter.EditBudgetChildClassifyAdapter
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.bean.ActivityJumpInfo
import com.cjj.keepaccounts.bean.SameCityMainType
import com.cjj.keepaccounts.dao.BudgetTool
import com.cjj.keepaccounts.dao.RecordTypeTool
import com.cjj.keepaccounts.enum.InputType
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.mvp.activity.bill.budget.CEditChildClassifyBudget
import com.cjj.keepaccounts.mvp.activity.bill.budget.PEditChildClassifyBudget
import com.cjj.keepaccounts.utils.RecyclerSpace
import com.cjj.keepaccounts.utils.ToastUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.utils.toMoney
import com.cjj.keepaccounts.view.EditChildClassifyBudgetHeadView
import com.cjj.keepaccounts.view.clearItemAnimator
import kotlinx.android.synthetic.main.activity_edit_child_classify_budget.*

/**
 * @author CJJ
 * Created by CJJ on 2018/7/9 11:03.
 */
class EditChildClassifyBudgetActivity : WhiteActivity<PEditChildClassifyBudget>(), CEditChildClassifyBudget.View {
    override val adapter: EditBudgetChildClassifyAdapter = EditBudgetChildClassifyAdapter()
    private lateinit var headView: EditChildClassifyBudgetHeadView
    private lateinit var footView: View
    private lateinit var tvAddChild: View
    private val sameCityType: SameCityMainType by extra()
    override fun getContentView(): View = Utils.inflate(R.layout.activity_edit_child_classify_budget, this)

    override fun initView() {
        setActivityTitleText(getString(R.string.type_budget, sameCityType.typeName))
        setActivityBackText(getString(R.string.back))
        showTitleLine()
        headView = EditChildClassifyBudgetHeadView(this)
        footView = Utils.inflate(R.layout.foot_view_edit_budget, this)
        tvAddChild = footView.findViewById(R.id.tv_add_child)
        footView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Utils.dip2px(95))
        headView.setData(sameCityType)
        adapter.addHeaderView(headView.headView)
        adapter.addFooterView(footView)
        rv_budget_child.layoutManager = LinearLayoutManager(this)
        rv_budget_child.adapter = adapter
        rv_budget_child.addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e7)))
        rv_budget_child.clearItemAnimator()
        adapter.setData(sameCityType.recordTypes)
        presenter.presenter()
    }

    override fun setHeadViewData(sameCityType: SameCityMainType) {
        headView.setData(sameCityType)
    }

    override fun initListener() {
        super.initListener()
        adapter.setOnItemClickListener { _, position, itemInfo ->
            val activityJumpInfo = ActivityJumpInfo(position, Activity.RESULT_OK
                    , InputType.MONEY.type, getActivityTitleText().toString()
                    , getString(R.string.edit_money), itemInfo.budgetAmount.toMoney())
            EditTextActivity.openActivity(activityJumpInfo)
        }
        headView.headView.setOnClickListener {
            val activityJumpInfo = ActivityJumpInfo(8888, Activity.RESULT_OK
                    , InputType.MONEY.type, getActivityTitleText().toString()
                    , getString(R.string.edit_money), sameCityType.budget.amount.toMoney())
            EditTextActivity.openActivity(activityJumpInfo)
        }
        tvAddChild.setOnClickListener {
            ActivityTool.skipActivity<AddBudgetChildClassifyActivity>()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val money = data.getStringExtra("contentStr").toFloat()
            if (money < 0F) {
                ToastUtils.shortToast("预算不可以小于0")
                return
            }
            if (requestCode != 8888) {
                val recordTypeClone = adapter.getItem(requestCode).clone()
                recordTypeClone.budgetAmount = money
                RecordTypeTool.update(adapter.getItem(requestCode), recordTypeClone)
                adapter.updateData(requestCode, recordTypeClone)

            } else {
                if (money.toDouble() < sameCityType.childBudget) {
                    ToastUtils.shortToast("不可以小于子分类预算之和")
                    return
                }
                val budget = sameCityType.budget.clone()
                budget.amount = money.toDouble()
                BudgetTool.update(sameCityType.budget, budget)
                sameCityType.budget = budget
                headView.setData(sameCityType)
            }
        }
    }
}