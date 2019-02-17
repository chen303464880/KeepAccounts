package com.cjj.keepaccounts.mvp.activity.account.accountdetails

import android.graphics.Color
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.account.AccountDetailsActivity
import com.cjj.keepaccounts.bean.Account
import com.cjj.keepaccounts.bean.AccountMonthDetails
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.dao.AccountDaoTool
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.utils.*
import org.jetbrains.anko.collections.forEachWithIndex
import javax.inject.Inject

/**
 * Created by CJJ on 2019/1/17 16:20.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class PAccountDetails @Inject constructor(view: AccountDetailsActivity, model: MAccountDetails) : CAccountDetails.Presenter(view, model) {

    private val accountDetails = ArrayList<Pair<AccountMonthDetails, ArrayList<Record>>>()

    private var year = 0
    private var month = 0
    private var day = 0

    private var expenditure = 0.0
    private var income = 0.0

    override fun onCreate() {
        super.onCreate()
        AccountDaoTool.addOnDaoChangeListener(accountChangeListener, this)
        RecordTool.addOnDaoChangeListener(recordChangeListener, this)
    }

    override fun presenter() {
        year = TimeUtils.year

        val years = model.getYears(year)

        if (years.size > 0) {
            view.setYears(years)
        }
    }

    override fun calculateData(uuid: Long, year: Int) {
        this.year = year
        model.getYearData(uuid, year)
                .subscribeMain { list ->
                    var monthDetails = arrayListOf<Record>()
                    var monthIncome = 0.0
                    var monthExpenditure = 0.0
                    if (list.isEmpty()) {
                        view.detailsKongVisibility(View.VISIBLE)
                        view.setDividerHeight(0)
                        income = 0.0
                        expenditure = 0.0
                    } else {
                        view.setDividerHeight(1)
                        view.detailsKongVisibility(View.GONE)
                        month = list.first().month
                        list.forEachWithIndex { i, it ->
                            if (month == it.month && day != it.day) {
                                val node = Record()
                                node.isNode = true
                                node.year = it.year
                                node.month = it.month
                                node.day = it.day
                                monthDetails.add(node)
                                day = it.day
                            }
                            if (month != it.month) {

                                if (monthDetails.size > 1) {
                                    val monthInfo = AccountMonthDetails(monthDetails.first().year, month, monthIncome, monthExpenditure)
                                    accountDetails.add(Pair(monthInfo, monthDetails))
                                    monthDetails = arrayListOf()
                                    monthIncome = 0.0
                                    monthExpenditure = 0.0
                                }
                                if (day != it.day) {
                                    val node = Record()
                                    node.isNode = true
                                    node.year = it.year
                                    node.month = it.month
                                    node.day = it.day
                                    monthDetails.add(node)
                                    day = it.day
                                }
                            }

                            //计算年累计与月累计
                            //判断收入与支出
                            val isIncoming = it.recordType.isIncoming
                            if (isIncoming == 1 || (isIncoming == -1 && it.rateMoney > 0)) {
                                income += it.rateMoney
                                monthIncome += it.rateMoney
                            } else {
                                val abs = Math.abs(it.rateMoney)
                                expenditure += abs
                                monthExpenditure += abs
                            }

                            month = it.month
                            monthDetails.add(it)
                            if (i == list.size - 1) {
                                val monthInfo = AccountMonthDetails(monthDetails.first().year, month, monthIncome, monthExpenditure)
                                accountDetails.add(Pair(monthInfo, monthDetails))
                            }
                        }

                    }

                    //设置统计数据
                    when (view.account.accountType.indexNum) {
                        3, 10, 11 -> {
                            view.setBalanceHintText(view.getString(R.string.present_debt))
                            view.setType(1)

                            view.setBalance(view.account.money)
                            view.setExpenditureText((view.account.creditLimit + view.account.money).toMoney())
                            view.setIncomeText(if (view.account.lastBTime == 0) view.getString(R.string.end_a_month) else view.getString(R.string.xx_day, view.account.lastBTime))

                        }
                        else -> {
                            view.setBalance(view.account.money)
                            view.setExpenditureText(Math.abs(expenditure).toMoney())
                            view.setIncomeText(income.toMoney())
                        }
                    }
                    view.setData(accountDetails)
                }
    }


    private fun refreshView(month: Int, entity: Record, money: Double) {
        LogUtils.i(entity.toString())
        if (accountDetails.size == 0) {
            return
        }
        //计算年累计与月累计
        //判断收入与支出
        if (entity.typeId > 0) {
            val isIncoming = entity.recordType.isIncoming
            if (isIncoming == 1) {
                income += money
                accountDetails[month].first.monthIncome += money
                view.setIncomeText(income.toMoney())
            } else {
                expenditure += money
                accountDetails[month].first.monthExpenditure += money
                view.setExpenditureText(expenditure.toMoney())
            }
        } else {
            if (entity.rateMoney > 0.0) {
                income += money
                accountDetails[month].first.monthIncome += money
                view.setIncomeText(income.toMoney())
            } else {
                expenditure -= money
                accountDetails[month].first.monthExpenditure -= money
                view.setExpenditureText(expenditure.toMoney())
            }
        }

    }

    /**
     * 账户数据改变监听
     */
    private val accountChangeListener = object : OnDaoChangeListener<Account> {
        override fun onInsertEntity(entity: Account) {

        }

        override fun onUpdateEntity(oldEntity: Account, newEntity: Account) {
            if (newEntity.uuid == view.account.uuid) {
                if (newEntity.name != view.account.name) {
                    view.setActivityTitleText(newEntity.name)
                }
                if (newEntity.color != view.account.color) {
                    val color = Color.parseColor(newEntity.color)
                    view.setActivityTitleColor(color)
                }
                if (newEntity.money != view.account.money) {
                    view.setBalance(newEntity.money)
                }
                view.account = newEntity
            }

        }


        override fun onDeleteEntity(entity: Account) {

        }
    }


    private val recordChangeListener = object : OnDaoChangeListener<Record> {
        override fun onInsertEntity(entity: Record) {
            if (entity.accountId == view.account.uuid && entity.year == year) {
                var insertMonthIndex = 0
                var insertDayIndex = 0
                //是否是新的一个月的记录
                var newMonth = true
                //是否是新的一天的记录
                var newDay = true
                if (accountDetails.isNotEmpty()) {//该年有记录
                    if (accountDetails.first().first.month < entity.month) {//新的一个月的记录
                        insertMonthIndex = 0
                    } else {

                        run month@{

                            accountDetails.forEachWithIndex { i, pair ->
                                if (pair.first.month == entity.month) {//找到同一个月的记录
                                    insertMonthIndex = i
                                    newMonth = false
                                    val days = pair.second
                                    if (days.isEmpty() || days.first().day < entity.day) {//新的一天的记录
                                        insertDayIndex = 0
                                        return@month
                                    }
                                    days.forEachIndexed { index, record ->
                                        if (record.day == entity.day) {//找到同一天的记录
                                            newDay = false
                                            insertDayIndex = index + 1
                                            return@month
                                        } else if (record.day < entity.day) {
                                            insertDayIndex = index
                                            return@month
                                        }
                                    }
                                    insertDayIndex = days.size
                                    return@month
                                } else if (pair.first.month < entity.month) {//没找到同一个月的记录
                                    insertMonthIndex = i
                                    return@month
                                }
                                insertMonthIndex = accountDetails.size
                            }
                        }

                    }

                } else {//全年没有记录
                    insertMonthIndex = 0
                }

                if (!newDay) {
                    accountDetails[insertMonthIndex].second.add(insertDayIndex, entity)
                } else {
                    if (!newMonth) {
                        val node = Record()
                        node.isNode = true
                        node.year = entity.year
                        node.month = entity.month
                        node.day = entity.day
                        val day = accountDetails[insertMonthIndex].second
                        day.add(insertDayIndex, node)
                        day.add(insertDayIndex + 1, entity)
                    } else {

                        val days = arrayListOf<Record>()
                        val node = Record()
                        node.isNode = true
                        node.year = entity.year
                        node.month = entity.month
                        node.day = entity.day
                        days.add(insertDayIndex, node)
                        days.add(insertDayIndex + 1, entity)

                        val monthInfo = AccountMonthDetails(entity.year, entity.month, 0.0, 0.0)
                        accountDetails.add(insertMonthIndex, Pair(monthInfo, days))
                    }
                }

                refreshView(insertMonthIndex, entity, entity.rateMoney)
                view.setData(accountDetails)
                if (accountDetails.size != 0) {
                    view.detailsKongVisibility(View.GONE)
                }
            }
        }

        override fun onUpdateEntity(oldEntity: Record, newEntity: Record) {
            if (oldEntity.accountId == newEntity.accountId && oldEntity.year == newEntity.year) {
                if (oldEntity.month == newEntity.month && oldEntity.day == newEntity.day) {
                    val monthIndex = accountDetails.binaryReversedSearch(oldEntity.month) { it.first.month }
                    val days = accountDetails[monthIndex].second
                    days.forEachWithIndex { i, record ->
                        if (record.uuid == newEntity.uuid) {
                            days[i] = newEntity

                            refreshView(monthIndex, oldEntity, -oldEntity.rateMoney)
                            refreshView(monthIndex, newEntity, newEntity.rateMoney)

                            view.setData(accountDetails)
                            return
                        }
                    }
                } else {
                    onDeleteEntity(oldEntity)
                    onInsertEntity(newEntity)
                }
            } else {
                onDeleteEntity(oldEntity)
            }

        }


        override fun onDeleteEntity(entity: Record) {
            if (entity.accountId == view.account.uuid && entity.year == year) {
                val index = accountDetails.binaryReversedSearch(entity.month) { it.first.month }
                val days = accountDetails[index].second
                if (days.size > 2) {
                    val filter = days.filter { it.day == entity.day }
                    if (filter.size > 2) {
                        run {
                            days.forEachWithIndex { i, record ->
                                if (entity.uuid == record.uuid) {
                                    days.removeAt(i)
                                    return@run
                                }
                            }
                        }
                    } else {
                        days.removeAll(filter)
                    }
                } else {
                    accountDetails.removeAt(index)
                }
                refreshView(index, entity, -entity.rateMoney)


                view.setData(accountDetails)

                if (accountDetails.size == 0) {
                    view.detailsKongVisibility(View.VISIBLE)
                }
            }
        }
    }

}