package com.cjj.keepaccounts.dao

import android.util.LongSparseArray
import androidx.core.util.set
import com.cjj.keepaccounts.base.BaseDaoTool
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.bean.RecordTypeDao
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.LogUtils

/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/16 9:30.
 */
object RecordTypeTool : BaseDaoTool<RecordType>() {
    override fun insert(entity: RecordType, isNotify: Boolean) {
        entity.mTime = System.currentTimeMillis()
        DaoManager.getRecordTypeDao().insert(entity)
        if (isNotify) {
            notifyDaoInsertChange(entity)
        }
    }

    override fun update(oldEntity: RecordType, newEntity: RecordType, isNotify: Boolean) {
        newEntity.mTime = System.currentTimeMillis()
        DaoManager.getRecordTypeDao().update(newEntity)
        if (isNotify) {
            notifyDaoUpdateChange(oldEntity, newEntity)
        }
    }

    override fun delete(entity: RecordType, isNotify: Boolean) {
        entity.mTime = System.currentTimeMillis()
        entity.isDeleted = 1
        DaoManager.getRecordTypeDao().update(entity)
        if (isNotify) {
            notifyDaoDeleteChange(entity)
        }
    }

    fun recrodTypeRefresh() {
        typeMap.clear()
    }

    private var typeMap = LongSparseArray<RecordType>()
    fun getRecordType(uuid: Long): RecordType {
        var recordType = typeMap[uuid]
        if (recordType == null) {
            recordType = DaoManager.getRecordTypeDao().queryBuilder()
                    .where(RecordTypeDao.Properties.Uuid.eq(uuid)).unique()
            if (recordType == null) {
                LogUtils.i(uuid.toString())
            }
            typeMap[uuid] = recordType
        }
        return recordType!!
    }

    fun updateOrderIndex(entities: Iterable<RecordType>) {
        DaoManager.getRecordTypeDao().updateInTx(entities)
    }


}