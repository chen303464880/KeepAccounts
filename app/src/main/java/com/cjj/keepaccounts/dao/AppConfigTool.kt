package com.cjj.keepaccounts.dao

import com.cjj.keepaccounts.base.BaseRangeDaoTool
import com.cjj.keepaccounts.bean.AppConfig
import com.cjj.keepaccounts.manager.DaoManager

/**
 * @author CJJ
 * Created by CJJ on 2018/5/21 15:31.
 */
object AppConfigTool : BaseRangeDaoTool<AppConfig>() {
    override fun insert(entity: AppConfig, isNotify: Boolean) {

    }

    override fun update(oldEntity: AppConfig, newEntity: AppConfig, isNotify: Boolean) {

    }

    override fun delete(entity: AppConfig, isNotify: Boolean) {

    }

    override fun rangeInsert(entities: Iterable<AppConfig>, isNotify: Boolean) {
        DaoManager.getAppConfigDao().insertInTx(entities)
        if (isNotify) {
            notifyRangeDaoInsertChange(entities)
        }
    }

    override fun rangeUpdate(oldEntities: Iterable<AppConfig>, newEntities: Iterable<AppConfig>, isNotify: Boolean) {
        DaoManager.getAppConfigDao().updateInTx(newEntities)
        if (isNotify) {
            notifyRangeDaoUpdateChange(oldEntities, newEntities)
        }
    }

    override fun rangeDelete(entities: Iterable<AppConfig>, isNotify: Boolean) {
        entities.forEach {
            it.confVal = "0"
        }
        DaoManager.getAppConfigDao().updateInTx(entities)
        if (isNotify) {
            notifyRangeDaoDeleteChange(entities)
        }
    }
}