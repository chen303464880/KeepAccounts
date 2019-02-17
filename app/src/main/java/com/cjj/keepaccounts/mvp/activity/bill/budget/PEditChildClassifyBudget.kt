package com.cjj.keepaccounts.mvp.activity.bill.budget

import com.cjj.keepaccounts.activity.bill.budget.EditChildClassifyBudgetActivity
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.bean.SameCityMainType
import com.cjj.keepaccounts.dao.BudgetTool
import com.cjj.keepaccounts.dao.RecordTypeTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import org.jetbrains.anko.collections.forEachWithIndex
import javax.inject.Inject

/**
 * @author CJJ
 * Created by CJJ on 2018/9/26 16:21.
 */
class PEditChildClassifyBudget @Inject constructor(view: EditChildClassifyBudgetActivity, model: MEditChildClassifyBudget) : CEditChildClassifyBudget.Presenter(view, model) {
    private var sameCityType by extra<SameCityMainType>()
    override fun onCreate() {
        super.onCreate()
        RecordTypeTool.addOnDaoChangeListener(recordTypeDaoChangeListener, this)
    }

    override fun presenter() {
        view.setAdapterData(sameCityType.recordTypes)
        view.setHeadViewData(sameCityType)
    }

    private val recordTypeDaoChangeListener = object : OnDaoChangeListener<RecordType> {
        override fun onInsertEntity(entity: RecordType) {

        }

        override fun onUpdateEntity(oldEntity: RecordType, newEntity: RecordType) {
            if (oldEntity.budgetType == sameCityType.budget.uuid && oldEntity.budgetType == newEntity.budgetType) {
                sameCityType.recordTypes.forEachWithIndex { index, recordType ->
                    if (recordType.uuid == newEntity.uuid) {
                        view.updateData(index, newEntity)
                        sameCityType.recordTypes[index] = newEntity
                        if (sameCityType.childBudget > sameCityType.budget.amount) {
                            val budget = sameCityType.budget.clone()
                            budget.amount = sameCityType.childBudget
                            BudgetTool.update(sameCityType.budget, budget)
                            sameCityType.budget = budget
                            view.setHeadViewData(sameCityType)
                        }
                        return
                    }
                }
            } else if (oldEntity.budgetType != newEntity.budgetType) {
                if (oldEntity.budgetType == sameCityType.budget.uuid) {//从该分类中移除
                    sameCityType.recordTypes.forEachWithIndex { index, recordType ->
                        if (recordType.uuid == newEntity.uuid) {
                            view.removeData(index)
                            sameCityType.recordTypes.removeAt(index)
                            return
                        }
                    }
                } else if (newEntity.budgetType == sameCityType.budget.uuid) {//添加到该分类中
                    run {
                        sameCityType.recordTypes.forEachWithIndex { index, recordType ->
                            if (newEntity.orderIndex < recordType.orderIndex) {
                                view.insertData(index, newEntity)
                                sameCityType.recordTypes.add(index, newEntity)
                                return@run
                            }
                        }
                        view.insertData(newEntity)
                        sameCityType.recordTypes.add(newEntity)
                    }
                    if (sameCityType.childBudget > sameCityType.budget.amount) {
                        val budget = sameCityType.budget.clone()
                        budget.amount = sameCityType.childBudget
                        BudgetTool.update(sameCityType.budget, budget)
                        sameCityType.budget = budget
                        view.setHeadViewData(sameCityType)
                    }
                }
            }
        }

        override fun onDeleteEntity(entity: RecordType) {

        }
    }

}