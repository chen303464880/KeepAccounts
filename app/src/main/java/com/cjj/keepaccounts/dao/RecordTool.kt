package com.cjj.keepaccounts.dao

import com.cjj.keepaccounts.base.BaseRangeDaoTool
import com.cjj.keepaccounts.bean.*
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.LogUtils
import com.cjj.keepaccounts.utils.TimeUtils
import java.math.BigDecimal

/**
 * Created by CJJ on 2018/4/17 20:32.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
object RecordTool : BaseRangeDaoTool<Record>() {
    override fun insert(entity: Record, isNotify: Boolean) {
        val database = DaoManager.daoSession.database
        database.beginTransaction()

        try {


            //不是转账的时候,设置成员数据
            if (entity.typeId >= 0) {
                val members = entity.members
                if (members != null && members.size > 0) {
                    RecordToTagTool.rangeInsert(members)
                }
            }

            entity.__setDaoSession(DaoManager.daoSession)
            //计算该账户的钱
            val oldAccount = entity.account
            val newAccount = oldAccount.clone()
            if (entity.typeId > 0) {
                if (entity.recordType.isIncoming == 0) {//支出
                    newAccount.money = (BigDecimal(newAccount.money) - BigDecimal(entity.rateMoney)).toDouble()
                } else {//收入
                    newAccount.money = (BigDecimal(newAccount.money) + BigDecimal(entity.rateMoney)).toDouble()
                }
            } else {
                newAccount.money = (BigDecimal(newAccount.money) + BigDecimal(entity.rateMoney)).toDouble()
            }
            val dayLongTime = TimeUtils.getDayLongTime(entity.year, entity.month, entity.day)
            if (!TimeUtils.isTimeSameDate(entity.rTime, dayLongTime)) {
                entity.rTime = dayLongTime
            }
            //更新该账户
            AccountDaoTool.update(oldAccount, newAccount)


            entity.mTime = TimeUtils.timeOfSecond
            DaoManager.getRecordDao().insert(entity)

            database.setTransactionSuccessful()
            if (isNotify) {
                notifyDaoInsertChange(entity)
            }
        } catch (e: Exception) {
            LogUtils.exception(e)
        } finally {
            database.endTransaction()
        }
    }


    override fun update(oldEntity: Record, newEntity: Record, isNotify: Boolean) {
        val database = DaoManager.daoSession.database
        database.beginTransaction()

        try {

            oldEntity.__setDaoSession(DaoManager.daoSession)
            newEntity.__setDaoSession(DaoManager.daoSession)
            val oldType = oldEntity.recordType
            val newType = if (oldEntity.typeId != newEntity.typeId) {
                RecordTypeTool.getRecordType(newEntity.typeId)
            } else {
                oldType
            }
            //判断是否时相同账户
            if (oldEntity.accountId != newEntity.accountId) {
                //还原旧帐户的数据
                val oldAccount = oldEntity.account
                //克隆一个新的对象,避免对之前的对象进行修改
                val newAccount = oldAccount.clone()
                if (oldEntity.typeId > 0) {
                    if (oldType.isIncoming == 0) {//支出
                        //加上之前的金额
                        newAccount.money = (BigDecimal(newAccount.money) + BigDecimal(oldEntity.rateMoney)).toDouble()
                    } else {//收入
                        //减去之前的金额
                        newAccount.money = (BigDecimal(newAccount.money) - BigDecimal(oldEntity.rateMoney)).toDouble()
                    }
                } else {
                    //减去之前的金额
                    newAccount.money = (BigDecimal(newAccount.money) - BigDecimal(oldEntity.rateMoney)).toDouble()
                }
                //通知账户更新
                AccountDaoTool.update(oldAccount, newAccount)

                //修改新的账户的数据
                val account = newEntity.account
                val accountClone = account.clone()
                if (oldEntity.typeId > 0) {
                    if (newType.isIncoming == 0) {//支出
                        accountClone.money = (BigDecimal(accountClone.money) - BigDecimal(newEntity.rateMoney)).toDouble()
                    } else {//收入
                        accountClone.money = (BigDecimal(accountClone.money) + BigDecimal(newEntity.rateMoney)).toDouble()
                    }
                } else {
                    accountClone.money = (BigDecimal(accountClone.money) + BigDecimal(newEntity.rateMoney)).toDouble()
                }

                AccountDaoTool.update(account, accountClone)

            } else if (oldType.isIncoming != newType.isIncoming) { //与之前的收入类型是否相同  支出或收入

                //还原旧帐户的数据
                val oldAccount = newEntity.account
                //克隆一个新的对象,避免对之前的对象进行修改
                val newAccount = oldAccount.clone()
                //还原之前的数据
                if (oldType.isIncoming == 0) {//支出
                    //加上之前的金额
                    newAccount.money = (BigDecimal(newAccount.money) + BigDecimal(oldEntity.rateMoney)).toDouble()
                } else {//收入
                    //减去之前的金额
                    newAccount.money = (BigDecimal(newAccount.money) - BigDecimal(oldEntity.rateMoney)).toDouble()
                }

                //计算新的数据
                if (newType.isIncoming == 0) {//支出
                    newAccount.money = (BigDecimal(newAccount.money) - BigDecimal(newEntity.rateMoney)).toDouble()
                } else {//收入
                    newAccount.money = (BigDecimal(newAccount.money) + BigDecimal(newEntity.rateMoney)).toDouble()
                }
                AccountDaoTool.update(oldAccount, newAccount)


            } else {//相同
                //判断金额是否变化
                if (newEntity.rateMoney != oldEntity.rateMoney) {
                    //还原旧帐户的数据
                    val oldAccount = newEntity.account
                    //克隆一个新的对象,避免对之前的对象进行修改
                    val newAccount = oldAccount.clone()
                    if (oldEntity.typeId > 0) {
                        newAccount.money = (BigDecimal(newAccount.money) + (BigDecimal(oldEntity.rateMoney) - BigDecimal(newEntity.rateMoney))).toDouble()
                    } else {
                        newAccount.money = (BigDecimal(newAccount.money) - (BigDecimal(oldEntity.rateMoney) - BigDecimal(newEntity.rateMoney))).toDouble()
                    }
                    AccountDaoTool.update(oldAccount, newAccount)
                }
            }


            val dayLongTime = TimeUtils.getDayLongTime(newEntity.year, newEntity.month, newEntity.day)
            if (!TimeUtils.isTimeSameDate(oldEntity.rTime, dayLongTime)) {
                newEntity.rTime = dayLongTime
            }
            //获取成员
            val oldMembers = oldEntity.members
            val newMembers = newEntity.members
            if (newMembers.size != 0 && oldMembers.size != 0) {
                val memberMap = hashMapOf<Long, RecordToTag>()
                newMembers.forEach {
                    it.mTime = TimeUtils.timeOfSecond
                    memberMap[it.uuid] = it
                }
                val deleteMembers = arrayListOf<RecordToTag>()

                //查找被删除的成员
                oldMembers.forEach {
                    val recordTag = memberMap.remove(it.tagId)
                    if (recordTag == null) {
                        it.mTime = TimeUtils.timeOfSecond
                        deleteMembers.add(it)
                    }
                }

                if (deleteMembers.size > 0) {
                    RecordToTagTool.rangeDelete(deleteMembers)
                }


                if (newMembers.size > 0) {
                    //插入记录
                    RecordToTagTool.rangeInsert(newMembers)
                }
            }


            newEntity.money = newEntity.rateMoney
            newEntity.mTime = TimeUtils.timeOfSecond
            DaoManager.getRecordDao().update(newEntity)
            database.setTransactionSuccessful()
            if (isNotify) {
                RecordTool.notifyDaoUpdateChange(oldEntity, newEntity)
            }
        } catch (e: Exception) {
            LogUtils.exception(e)
        } finally {
            database.endTransaction()
        }
    }


    override fun delete(entity: Record) {
        if (entity.isDeleted == 0) {
            val database = DaoManager.daoSession.database
            database.beginTransaction()

            try {
                when (entity.typeId) {
                    -1L -> {//转账
                        val first = DaoManager.getRecordDao().queryBuilder()
                                .where(RecordDao.Properties.ActionId.eq(entity.actionId))
                                .where(RecordDao.Properties.AccountId.notEq(entity.accountId))
                                .unique()
                        delete(first, true)
                        delete(entity, true)
                    }
                    -105L, -110L -> delete(entity, true)  //利息
                    -103L, -104L, -108L, -109L -> {  //还款
                        val list = DaoManager.getRecordDao().queryBuilder()
                                .where(RecordDao.Properties.ActionId.eq(entity.actionId))
                                .list()
                        list.forEach {
                            delete(it, true)
                        }
                    }
                    -101L, -102L, -106L, -107L -> {//借款

                    }

                    else -> delete(entity, true)  //普通记录
                }
                database.setTransactionSuccessful()
            } catch (e: Exception) {
                LogUtils.exception(e)
            } finally {
                database.endTransaction()
            }
        }
    }

    override fun delete(entity: Record, isNotify: Boolean) {

        if (entity.isDeleted == 0) {
            val database = DaoManager.daoSession.database
            database.beginTransaction()

            try {

                //还原这个账户的金额
                val oldAccount = entity.account
                val newAccount = oldAccount.clone()
                if (entity.typeId <= 0L) {
                    newAccount.money = (BigDecimal(newAccount.money) - (BigDecimal(entity.rateMoney))).toDouble()
                } else {
                    if (entity.recordType.isIncoming == 0) {//支出
                        newAccount.money = (BigDecimal(newAccount.money) + (BigDecimal(entity.rateMoney))).toDouble()
                    } else {//收入
                        newAccount.money = (BigDecimal(newAccount.money) - (BigDecimal(entity.rateMoney))).toDouble()
                    }
                }
                AccountDaoTool.update(oldAccount, newAccount)

                //将该条记录置为删除
                entity.isDeleted = 1
                if (entity.typeId == -105L || entity.typeId == -110L) {
                    entity.rateMoney = 0.0
                }
                entity.mTime = TimeUtils.timeOfSecond
                DaoManager.getRecordDao().update(entity)
                database.setTransactionSuccessful()
                //通知观察者们更新
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

    private val recordInfoMap = hashMapOf<Int, MonthRecordInfo>()
    private const val sql = "select type.isIncoming , sum(r.rate_money) from record as r inner join com_qeeniao_mobile_kdjz_Models_RecordType as type where r.is_deleted==0 and r.typeId>0 and r.typeId==type.uuid and r.year==?  and r.month==? group by type.isIncoming"
    /**
     * 获取某个月的收入支出情况
     */
    fun getMonthRecord(year: Int, month: Int): MonthRecordInfo {
        val key = year * 100 + month
        var monthRecordInfo = recordInfoMap[key]
        if (monthRecordInfo == null) {
            monthRecordInfo = MonthRecordInfo(0.0, 0.0)
            val rawQuery = DaoManager.daoSession.database.rawQuery(sql, arrayOf(year.toString(), month.toString()))
            while (rawQuery.moveToNext()) {
                val isIncoming = rawQuery.getInt(0)
                if (isIncoming == 0) {
                    monthRecordInfo.expend = rawQuery.getDouble(1)
                } else {
                    monthRecordInfo.income = rawQuery.getDouble(1)
                }
            }
            rawQuery.close()
            recordInfoMap[key] = monthRecordInfo
        }
        return monthRecordInfo
    }

    fun monthRecordRefresh() {
        recordInfoMap.clear()
    }

    fun getMemberCount(uuid: Long): Long {
        return DaoManager.getRecordToTagDao().queryBuilder()
                .where(RecordToTagDao.Properties.RecordId.eq(uuid))
                .where(RecordToTagDao.Properties.IsDeleted.eq(1))
                .count()
    }

    override fun rangeInsert(entities: Iterable<Record>, isNotify: Boolean) {
        DaoManager.getRecordDao().insertInTx(entities)
    }

    override fun rangeUpdate(oldEntities: Iterable<Record>, newEntities: Iterable<Record>, isNotify: Boolean) {
        DaoManager.getRecordDao().updateInTx(newEntities)
    }

    override fun rangeDelete(entities: Iterable<Record>, isNotify: Boolean) {

    }
}
