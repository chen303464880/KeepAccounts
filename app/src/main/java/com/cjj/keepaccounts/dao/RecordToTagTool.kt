package com.cjj.keepaccounts.dao

import com.cjj.keepaccounts.base.BaseRangeDaoTool
import com.cjj.keepaccounts.bean.RecordToTag
import com.cjj.keepaccounts.manager.DaoManager

/**
 * Created by CJJ on 2018/4/17 21:00.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
object RecordToTagTool : BaseRangeDaoTool<RecordToTag>() {

    override fun insert(entity: RecordToTag, isNotify: Boolean) {
        DaoManager.getRecordToTagDao().insert(entity)
        if (isNotify) {
            notifyDaoInsertChange(entity)
        }
    }

    override fun rangeInsert(entities: Iterable<RecordToTag>, isNotify: Boolean) {
        DaoManager.getRecordToTagDao().insertInTx(entities)
        if (isNotify) {
            notifyRangeDaoInsertChange(entities)
        }
    }

    override fun update(oldEntity: RecordToTag, newEntity: RecordToTag, isNotify: Boolean) {
        DaoManager.getRecordToTagDao().update(newEntity)
        if (isNotify) {
            notifyDaoUpdateChange(oldEntity, newEntity)
        }
    }

    override fun rangeUpdate(oldEntities: Iterable<RecordToTag>, newEntities: Iterable<RecordToTag>, isNotify: Boolean) {
        if (oldEntities.count() != newEntities.count()) {
            throw IllegalStateException()
        }
        DaoManager.getRecordToTagDao().updateInTx(newEntities)
        if (isNotify) {
            notifyRangeDaoUpdateChange(oldEntities, newEntities)
        }
    }

    override fun rangeDelete(entities: Iterable<RecordToTag>, isNotify: Boolean) {
        entities.forEach {
            it.isDeleted = 1
        }
        DaoManager.getRecordToTagDao().updateInTx(entities)
        if (isNotify) {
            notifyRangeDaoDeleteChange(entities)
        }
    }

    override fun delete(entity: RecordToTag, isNotify: Boolean) {
        entity.isDeleted = 1
        DaoManager.getRecordToTagDao().update(entity)
        if (isNotify) {
            notifyDaoDeleteChange(entity)
        }
    }


}