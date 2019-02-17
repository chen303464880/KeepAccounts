package com.cjj.keepaccounts.activity.bill.budget

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.BudgetAdapter
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.Budget
import com.cjj.keepaccounts.bean.ListBook
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.bean.SameCityMainTypeDao
import com.cjj.keepaccounts.dao.BudgetTool
import com.cjj.keepaccounts.dao.ListBookTool
import com.cjj.keepaccounts.dao.RecordTypeTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.RecyclerSpace
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.BudgetHeadView
import kotlinx.android.synthetic.main.activity_budget.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.dip

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/16 15:38.
 */
class BudgetActivity : WhiteActivity<EmptyPresenter>() {


    private lateinit var budgetHeadView: BudgetHeadView
    private lateinit var adapter: BudgetAdapter

    override fun getContentView(): View = Utils.inflate(R.layout.activity_budget, this)


    override fun initView() {
        showTitleLine()
        setActivityBackText(getString(R.string.bill))
        setActivityTitleText(getString(R.string.budget))
        setNextText(getString(R.string.settings))
        ListBookTool.addOnDaoChangeListener(listBookDaoChangeListener, presenter)
        RecordTypeTool.addOnDaoChangeListener(recordTypeDaoChangeListener, presenter)
        BudgetTool.addOnDaoChangeListener(budgetDaoChangeListener, presenter)


        budgetHeadView = BudgetHeadView(this)
        budgetHeadView.setData(ListBookTool.getBudget())
        adapter = BudgetAdapter()
        adapter.addHeaderView(budgetHeadView.headView)
        adapter.addHeaderView(View(this).apply {
            backgroundResource = R.color.bg_color_fb
            layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, dip(10))
        })
        rv_budget.adapter = adapter
        rv_budget.layoutManager = LinearLayoutManager(this)
        rv_budget.addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.bg_color_ebeceb)))
        val list = DaoManager.getSameCityMainTypeDao()
                .queryBuilder().where(SameCityMainTypeDao.Properties.TypeId.gt(1))
                .list()
        adapter.insertData(list)
    }

    override fun initListener() {
        super.initListener()
        adapter.setOnItemClickListener { _, _, itemInfo ->
            if (itemInfo.budget.amount == 0.0) {
                ActivityTool.skipActivity<BudgetClassifyEditActivity>(itemInfo)
            } else {
                ActivityTool.skipActivity<EditChildClassifyBudgetActivity>(itemInfo)
            }
        }
    }


    private val listBookDaoChangeListener = object : OnDaoChangeListener<ListBook> {
        override fun onInsertEntity(entity: ListBook) {

        }

        override fun onUpdateEntity(oldEntity: ListBook, newEntity: ListBook) {
            if (newEntity.isDefault == 1) {
                budgetHeadView.setData(newEntity)
            }
        }


        override fun onDeleteEntity(entity: ListBook) {

        }
    }

    private val recordTypeDaoChangeListener = object : OnDaoChangeListener<RecordType> {
        override fun onInsertEntity(entity: RecordType) {

        }

        override fun onUpdateEntity(oldEntity: RecordType, newEntity: RecordType) {
            if (oldEntity.budgetType == 0L && newEntity.budgetType == 0L) {
                return
            }
            if (oldEntity.budgetType == newEntity.budgetType
                    && oldEntity.budgetAmount != newEntity.budgetAmount) {//改了月预算
                adapter.data.forEachWithIndex { _, samecityMainType ->
                    if (newEntity.budgetType == samecityMainType.budget.uuid) {
                        val childBudget = samecityMainType.childBudget
                        if (childBudget > samecityMainType.budget.amount) {
                            val newBudget = samecityMainType.budget.clone()
                            newBudget.amount = childBudget
                            BudgetTool.update(samecityMainType.budget, newBudget)
                        }
                        return
                    }
                }
            } else if (oldEntity.budgetType != newEntity.budgetType
                    && oldEntity.budgetAmount == newEntity.budgetAmount) {
                if (newEntity.budgetType == 0L) {//从某种预算中删除
                    adapter.data.forEachWithIndex { i, sameCityMainType ->
                        if (oldEntity.budgetType == sameCityMainType.budget.uuid) {
                            sameCityMainType.removeRecordType(oldEntity)
                            adapter.updateData(i)
                            return
                        }
                    }
                } else {//添加到某种预算中
                    adapter.data.forEachWithIndex { i, sameCityMainType ->
                        if (newEntity.budgetType == sameCityMainType.budget.uuid) {
                            sameCityMainType.addRecordType(newEntity)
                            adapter.updateData(i)
                            return
                        }
                    }
                }
            }
        }

        override fun onDeleteEntity(entity: RecordType) {

        }
    }

    private val budgetDaoChangeListener = object : OnDaoChangeListener<Budget> {
        override fun onInsertEntity(entity: Budget) {

        }

        override fun onUpdateEntity(oldEntity: Budget, newEntity: Budget) {
            adapter.data.forEachWithIndex { i, samecityMainType ->
                if (samecityMainType.budget.uuid == newEntity.uuid) {
                    samecityMainType.budget = newEntity
                    adapter.updateData(i)
                    return
                }
            }
        }

        override fun onDeleteEntity(entity: Budget) {

        }
    }

    override fun onNext() {
        super.onNext()
        ActivityTool.skipActivity<BudgetSettingActivity>(ListBookTool.getBudget())
    }

}