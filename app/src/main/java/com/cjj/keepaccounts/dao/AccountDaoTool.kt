package com.cjj.keepaccounts.dao

import com.cjj.keepaccounts.base.BaseRangeDaoTool
import com.cjj.keepaccounts.bean.Account
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.TimeUtils

/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/12 16:49.
 */
object AccountDaoTool : BaseRangeDaoTool<Account>() {
    override fun insert(entity: Account, isNotify: Boolean) {
        entity.mTime = System.currentTimeMillis()
        val insert = DaoManager.getAccountDao().insert(entity)
        if (insert > -1L && isNotify) {
            notifyDaoInsertChange(entity)
        }
    }

    override fun update(oldEntity: Account, newEntity: Account, isNotify: Boolean) {
        newEntity.mTime = System.currentTimeMillis()
        DaoManager.getAccountDao().update(newEntity)
        if (isNotify) {
            notifyDaoUpdateChange(oldEntity, newEntity)
        }
    }

    override fun delete(entity: Account, isNotify: Boolean) {
        entity.mTime = System.currentTimeMillis()
        entity.isDeleted = 1
        DaoManager.getAccountDao().update(entity)
        if (isNotify) {
            notifyDaoDeleteChange(entity)
        }
    }

    fun changeAmount(account: Account, money: Double) {
        val newRecord = Record(System.currentTimeMillis())
        newRecord.accountId = account.uuid
        newRecord.rateMoney = money
        newRecord.typeId = -2L
        newRecord.setDate(TimeUtils.year, TimeUtils.month - 1, TimeUtils.day)
        RecordTool.insert(newRecord)
    }

    fun updateOrderIndex(entities: Iterable<Account>) {
        DaoManager.getAccountDao().updateInTx(entities)
    }

    override fun rangeInsert(entities: Iterable<Account>, isNotify: Boolean) {
        DaoManager.getAccountDao().insertInTx(entities)
    }

    override fun rangeUpdate(oldEntities: Iterable<Account>, newEntities: Iterable<Account>, isNotify: Boolean) {
        DaoManager.getAccountDao().updateInTx(newEntities)
    }

    override fun rangeDelete(entities: Iterable<Account>, isNotify: Boolean) {

    }
}