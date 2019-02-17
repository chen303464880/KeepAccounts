package com.cjj.keepaccounts.activity.bill.budget

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.BudgetChildClassifyAdapter
import com.cjj.keepaccounts.adapter.BudgetClassifyAdapter
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.bean.RecordTypeDao
import com.cjj.keepaccounts.bean.SameCityMainType
import com.cjj.keepaccounts.bean.SameCityMainTypeDao
import com.cjj.keepaccounts.dao.RecordTypeTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.RecyclerSpace
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.clearItemAnimator
import kotlinx.android.synthetic.main.activity_add_budget_child_classify.*
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author CJJ
 * Created by CJJ on 2018/7/5 14:40.
 */
class AddBudgetChildClassifyActivity : WhiteActivity<EmptyPresenter>() {

    private lateinit var budgetClassifyAdapter: BudgetClassifyAdapter
    private lateinit var budgetChildClassifyAdapter: BudgetChildClassifyAdapter

    private lateinit var budgetList: List<SameCityMainType>

    private lateinit var checkType: SameCityMainType

    private val blackRecordTypes: ArrayList<RecordType> by lazy {
        DaoManager.getRecordTypeDao().queryBuilder()
                .where(RecordTypeDao.Properties.BudgetType.eq(0))
                .where(RecordTypeDao.Properties.ListId.eq(1))
                .where(RecordTypeDao.Properties.IsDeleted.eq(0))
                .where(RecordTypeDao.Properties.ImgSrcId.ge(0))
                .where(RecordTypeDao.Properties.IsIncoming.eq(0))
                .orderAsc(RecordTypeDao.Properties.OrderIndex).list() as ArrayList
    }


    override fun getContentView(): View = Utils.inflate(R.layout.activity_add_budget_child_classify, this)

    override fun initView() {
        setActivityBackText(getString(R.string.back))
        setActivityTitleText(getString(R.string.add_budget_child_classify))
        showTitleLine()

        RecordTypeTool.addOnDaoChangeListener(recordTypeDaoChangeListener, presenter)

        budgetClassifyAdapter = BudgetClassifyAdapter()
        rv_budget.adapter = budgetClassifyAdapter
        rv_budget.layoutManager = LinearLayoutManager(this)
        rv_budget.addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e7)))
        rv_budget.clearItemAnimator()
        DaoManager.getSameCityMainTypeDao().detachAll()
        budgetList = DaoManager.getSameCityMainTypeDao()
                .queryBuilder().where(SameCityMainTypeDao.Properties.TypeId.gt(1))
                .list()
        budgetClassifyAdapter.setData(budgetList)


        budgetChildClassifyAdapter = BudgetChildClassifyAdapter()
        rv_budget_child.adapter = budgetChildClassifyAdapter
        rv_budget_child.layoutManager = LinearLayoutManager(this)
        rv_budget_child.addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e7)))
        checkType = budgetList[0]
        budgetChildClassifyAdapter.setData(getRecords())
        budgetChildClassifyAdapter.budgetTypeId = checkType.budget.uuid
    }

    override fun initListener() {
        super.initListener()
        budgetClassifyAdapter.setOnChangeListener {
            checkType = it
            budgetChildClassifyAdapter.setData(getRecords())
            budgetChildClassifyAdapter.budgetTypeId = it.budget.uuid
        }
    }

    private fun getRecords(): List<RecordType> {
        return if (checkType.recordTypes.isNotEmpty()) {
            val list = arrayListOf<RecordType>()
            val size = blackRecordTypes.size
            val checkSize = checkType.recordTypes.size
            var type = checkType.recordTypes.first()
            var index = 0
            var i = 0
            while (i < size) {
                val blankType = blackRecordTypes[i]
                if (index < checkSize && type.orderIndex < blankType.orderIndex) {
                    list.add(type)
                    index++
                    if (index < checkSize) {
                        type = checkType.recordTypes[index]
                    }
                } else {
                    list.add(blankType)
                    i++
                }
            }
            if (index < checkSize) {
                for (j in index until checkSize) {
                    list.add(checkType.recordTypes[j])
                }
            }
            list
        } else {
            blackRecordTypes
        }
    }

    private val recordTypeDaoChangeListener = object : OnDaoChangeListener<RecordType> {
        override fun onInsertEntity(entity: RecordType) {
        }

        override fun onUpdateEntity(oldEntity: RecordType, newEntity: RecordType) {
            if (oldEntity.budgetType != newEntity.budgetType) {
                if (oldEntity.budgetType != checkType.budget.uuid) {
                    run {
                        budgetChildClassifyAdapter.data.forEachWithIndex { i, recordType ->
                            if (newEntity.uuid == recordType.uuid) {
                                budgetChildClassifyAdapter.updateData(i, newEntity)
                                return@run
                            }
                        }
                    }
                    checkType.addRecordType(newEntity)

                    run {
                        blackRecordTypes.forEachWithIndex { i, recordType ->
                            if (oldEntity.uuid == recordType.uuid) {
                                blackRecordTypes.removeAt(i)
                                return@run
                            }
                        }
                    }

                } else {
                    run {
                        budgetChildClassifyAdapter.data.forEachWithIndex { i, recordType ->
                            if (oldEntity.uuid == recordType.uuid) {
                                budgetChildClassifyAdapter.updateData(i, newEntity)
                                return@run
                            }
                        }
                    }

                    checkType.removeRecordType(oldEntity)



                    run {
                        blackRecordTypes.forEachWithIndex { i, recordType ->
                            if (newEntity.orderIndex < recordType.orderIndex) {
                                blackRecordTypes.add(i, newEntity)
                                return@run
                            }
                        }
                        blackRecordTypes.add(newEntity)
                    }
                }
            }
        }

        override fun onDeleteEntity(entity: RecordType) {
        }
    }

}