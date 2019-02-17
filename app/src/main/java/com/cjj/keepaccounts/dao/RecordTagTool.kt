package com.cjj.keepaccounts.dao

import com.cjj.keepaccounts.base.BaseDaoTool
import com.cjj.keepaccounts.bean.RecordTag
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.TimeUtils

/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/8 9:54.
 */
object RecordTagTool : BaseDaoTool<RecordTag>() {

    override fun insert(entity: RecordTag, isNotify: Boolean) {
        entity.mTime = TimeUtils.timeOfSecond
        if (DaoManager.getRecordTagDao().insert(entity) > -1L && isNotify) {
            notifyDaoInsertChange(entity)
        }
    }

    override fun update(oldEntity: RecordTag, newEntity: RecordTag, isNotify: Boolean) {
        newEntity.mTime = TimeUtils.timeOfSecond
        DaoManager.getRecordTagDao().update(newEntity)
        if (isNotify) {
            notifyDaoUpdateChange(oldEntity, newEntity)
        }
    }

    override fun delete(entity: RecordTag, isNotify: Boolean) {
        entity.isDeleted = 1
        entity.mTime = TimeUtils.timeOfSecond
        DaoManager.getRecordTagDao().update(entity)
        if (isNotify) {
            notifyDaoDeleteChange(entity)
        }
    }
}