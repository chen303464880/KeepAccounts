package com.cjj.keepaccounts.dao

import com.cjj.keepaccounts.base.BaseDaoTool
import com.cjj.keepaccounts.bean.Credit
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.LogUtils
import com.cjj.keepaccounts.utils.TimeUtils

/**
 * Created by CJJ on 2018/4/22 13:42.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
object CreditTool : BaseDaoTool<Credit>() {
    override fun insert(entity: Credit, isNotify: Boolean) {
        entity.mTime = TimeUtils.timeOfSecond
        DaoManager.getCreditDao().insert(entity)
        if (isNotify) {
            notifyDaoInsertChange(entity)
        }
    }


    override fun update(oldEntity: Credit, newEntity: Credit, isNotify: Boolean) {
//        newEntity.mTime = TimeUtils.timeOfSecond
        DaoManager.getCreditDao().update(newEntity)
        if (isNotify) {
            notifyDaoUpdateChange(oldEntity, newEntity)
        }
    }

    override fun delete(entity: Credit, isNotify: Boolean) {
        val records = entity.records
        val database = DaoManager.daoSession.database
        database.beginTransaction()

        try {
            records.forEach {
                RecordTool.delete(it, true)
            }

            entity.mTime = TimeUtils.timeOfSecond
            entity.isDeleted = 1
            DaoManager.getCreditDao().update(entity)

            database.setTransactionSuccessful()
            if (isNotify) {
                notifyDaoDeleteChange(entity)
            }
        } catch (e: Exception) {
            LogUtils.exception(e)
        } finally {
            database.endTransaction()
        }
    }
}