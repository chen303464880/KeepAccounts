package com.cjj.keepaccounts.mvp.activity.account.addaccount

import com.cjj.keepaccounts.activity.account.AddAccountActivity
import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.bean.Account
import com.cjj.keepaccounts.bean.AccountDao
import com.cjj.keepaccounts.bean.AccountType
import com.cjj.keepaccounts.dao.AccountDaoTool
import com.cjj.keepaccounts.manager.DaoManager
import javax.inject.Inject

/**
 * Created by CJJ on 2019/1/23 15:58.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class PAddAccount @Inject constructor(view: AddAccountActivity, model: BaseModel) : CAddAccount.Presenter(view, model) {

    private var accountType by extra<AccountType>("accountType")

    private fun getOrderIndex(): Long =
            when (accountType.indexNum) {
                1, 2, 4, 5, 6, 7, 8, 9, 12 -> {
                    DaoManager.getAccountDao().queryBuilder()
                            .where(AccountDao.Properties.TypeId.`in`(1, 2, 4, 5, 6, 7, 8, 9, 12))
                            .orderAsc(AccountDao.Properties.OrderIndex).list().last().orderIndex + 1
                }
                else -> {
                    DaoManager.getAccountDao().queryBuilder()
                            .where(AccountDao.Properties.TypeId.`in`(3, 10, 11, 13))
                            .orderAsc(AccountDao.Properties.OrderIndex).list().last().orderIndex + 1
                }
            }


    override fun delete(account: Account) {
        AccountDaoTool.delete(account, true)
    }

    override fun insert(account: Account) {
        account.name = account.name.toString()
        account.isDeleted = 0
        account.isShow = 0
        val deviceUuid = com.cjj.keepaccounts.constants.Constants.deviceUuid
        account.deviceId = deviceUuid.toString()
        account.lastBTime = 0

        account.uuid = System.currentTimeMillis()
        account.orderIndex = getOrderIndex()
        AccountDaoTool.insert(account)
    }
}