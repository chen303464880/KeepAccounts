package com.cjj.keepaccounts.mvp.activity.account.borrowmoneyrecord

import android.graphics.Color
import com.cjj.keepaccounts.activity.account.BorrowMoneyRecordActivity
import com.cjj.keepaccounts.bean.Account
import com.cjj.keepaccounts.bean.Credit
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.dao.AccountDaoTool
import com.cjj.keepaccounts.dao.CreditTool
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.toMoney
import org.jetbrains.anko.collections.forEachWithIndex
import javax.inject.Inject

/**
 * Created by CJJ on 2019/1/30 14:31.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class PBorrowMoneyRecord @Inject constructor(view: BorrowMoneyRecordActivity, model: MBorrowMoneyRecord) : CBorrowMoneyRecord.Presenter(view, model) {

    override var account: Account by extra()
    private var isBorrow = true
    private lateinit var borrowMoneyRecord: ArrayList<Credit>
    override fun onCreate() {
        super.onCreate()
        RecordTool.addOnDaoChangeListener(recordDaoChangeListener, this)
        AccountDaoTool.addOnDaoChangeListener(accountChangeListener, this)
        CreditTool.addOnDaoChangeListener(creditDaoChangeListener, this)
    }

    override fun presenter() {
        super.presenter()
        borrowMoneyRecord = model.getData(account.uuid)
        view.setIntervalTime(borrowMoneyRecord[0].mTime)
        view.setAdapterData(borrowMoneyRecord)
        view.refreshView(borrowMoneyRecord.size)
        isBorrow = account.accountType.indexNum == 12
    }

    private val accountChangeListener = object : OnDaoChangeListener<Account> {
        override fun onInsertEntity(entity: Account) {

        }

        override fun onUpdateEntity(oldEntity: Account, newEntity: Account) {

            if (newEntity.uuid == account.uuid) {
                if (newEntity.name != account.name) {
                    view.setActivityTitleText(newEntity.name)
                }
                if (newEntity.color != account.color) {

                    val color = Color.parseColor(newEntity.color)
                    view.setActivityTitleColor(color)
                }
                if (newEntity.money != account.money) {
                    account.money = newEntity.money
                    view.setMoney(newEntity.money.toMoney())
                }
                account = newEntity
            }

        }

        override fun onDeleteEntity(entity: Account) {

        }
    }

    private val creditDaoChangeListener = object : OnDaoChangeListener<Credit> {
        override fun onInsertEntity(entity: Credit) {
            var index = 0
            kotlin.run {
                if (borrowMoneyRecord.size != 0) {
                    borrowMoneyRecord.forEachWithIndex { i, credit ->
                        if (TimeUtils.isTimeSameDate(credit.mTime, entity.mTime)) {
                            borrowMoneyRecord.add(i + 1, entity)
                            view.insertData(i + 1, entity)
                            return
                        } else {
                            if (entity.mTime > credit.mTime) {
                                index = i
                                return@run
                            }
                        }
                    }
                    index = borrowMoneyRecord.size
                }
            }

            val node = Credit()
            node.mTime = entity.mTime
            node.isNode = true
            view.insertData(index, node)
            borrowMoneyRecord.add(index, node)
            view.insertData(index + 1, entity)
            borrowMoneyRecord.add(index + 1, entity)
            if (index == 0) {
                view.setIntervalTime(entity.mTime)
            }
            view.refreshView(borrowMoneyRecord.size)

        }

        override fun onUpdateEntity(oldEntity: Credit, newEntity: Credit) {
            if (TimeUtils.isTimeSameDate(oldEntity.mTime, newEntity.mTime)) {
                borrowMoneyRecord.forEachWithIndex { i, credit ->
                    if (credit.uuid == newEntity.uuid) {
                        borrowMoneyRecord[i] = newEntity
                        view.updateData(i, newEntity)
                        return
                    }
                }
            } else {
                onDeleteEntity(oldEntity)
                onInsertEntity(newEntity)
            }
        }

        override fun onDeleteEntity(entity: Credit) {
            var nodeIndex = 0
            for (i in 0 until borrowMoneyRecord.size) {
                val credit = borrowMoneyRecord[i]
                if (credit.isNode) {
                    nodeIndex = i
                } else {
                    if (credit.uuid == entity.uuid) {
                        if (i - 1 == nodeIndex && (i + 1 >= borrowMoneyRecord.size || !TimeUtils.isTimeSameDate(borrowMoneyRecord[i + 1].mTime, entity.mTime))) {
                            view.removeData(nodeIndex, 2)

                            for (index in 0 until 2) {
                                borrowMoneyRecord.removeAt(index)
                            }

                            if (nodeIndex == 0 && borrowMoneyRecord.size > 0) {
                                view.setIntervalTime(borrowMoneyRecord[0].mTime)
                            }
                            break
                        } else {
                            borrowMoneyRecord.removeAt(i)
                            view.removeData(i)
                            break
                        }
                    }
                }
            }
            view.refreshView(borrowMoneyRecord.size)
        }
    }

    private val recordDaoChangeListener = object : OnDaoChangeListener<Record> {
        override fun onInsertEntity(entity: Record) {
            if (entity.typeId == (if (isBorrow) -103L else -108L)) {
                borrowMoneyRecord.forEachWithIndex { i, credit ->
                    if (credit.uuid == entity.creditID) {
                        credit.repayRecord = null
                        credit.repayMoney = 0.0
                        view.updateData(i, credit)
                        return
                    }
                }
            }
        }

        override fun onUpdateEntity(oldEntity: Record, newEntity: Record) {
            if (oldEntity.typeId == (if (isBorrow) -103L else -108L)) {
                borrowMoneyRecord.forEachWithIndex { i, credit ->
                    if (credit.uuid == oldEntity.creditID) {
                        credit.repayRecord = null
                        credit.repayMoney = 0.0
                        view.updateData(i, credit)
                        return
                    }
                }
            }
        }

        override fun onDeleteEntity(entity: Record) {
            if (entity.typeId == (if (isBorrow) -103L else -108L)) {
                borrowMoneyRecord.forEachWithIndex { i, credit ->
                    if (credit.uuid == entity.creditID) {
                        credit.repayRecord = null
                        credit.repayMoney = 0.0
                        view.updateData(i, credit)
                        return
                    }
                }
            }
        }
    }
}