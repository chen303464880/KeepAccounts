package com.cjj.keepaccounts.dao

import com.cjj.keepaccounts.base.BaseDaoTool
import com.cjj.keepaccounts.bean.Budget
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.TimeUtils

/**
 * @author CJJ
 * Created by CJJ on 2018/7/5 17:33.
 */
object BudgetTool : BaseDaoTool<Budget>() {
    override fun insert(entity: Budget, isNotify: Boolean) {
        entity.mTime = TimeUtils.timeOfSecond
        DaoManager.getBudgetDao().insert(entity)
        if (isNotify) {
            notifyDaoInsertChange(entity)
        }
    }

    override fun update(oldEntity: Budget, newEntity: Budget, isNotify: Boolean) {
        newEntity.mTime = TimeUtils.timeOfSecond
        DaoManager.getBudgetDao().update(newEntity)
        if (isNotify) {
            notifyDaoUpdateChange(oldEntity, newEntity)
        }
    }

    override fun delete(entity: Budget, isNotify: Boolean) {
    }
}