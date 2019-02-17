package com.cjj.keepaccounts.mvp.activity.account.borrowmoneyrecord

import com.cjj.keepaccounts.bean.Credit
import com.cjj.keepaccounts.bean.CreditDao
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.TimeUtils
import javax.inject.Inject

/**
 * Created by CJJ on 2019/1/30 14:48.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class MBorrowMoneyRecord @Inject constructor() : CBorrowMoneyRecord.Model {
    override fun getData(uuid: Long): ArrayList<Credit> {
        val borrowMoneyRecord = arrayListOf<Credit>()
        val list = DaoManager.getCreditDao().queryBuilder()
                .where(CreditDao.Properties.IsDeleted.eq(0))
                .orderDesc(CreditDao.Properties.MTime)
                .orderDesc(CreditDao.Properties.CTime)
                .list()

        var node = Credit()
        list.forEach {
            if (it.records.isNotEmpty() && it.records.last().accountId == uuid) {
                if (!TimeUtils.isTimeSameDate(node.mTime, it.mTime)) {
                    node = Credit()
                    node.mTime = it.mTime
                    node.isNode = true
                    borrowMoneyRecord.add(node)
                }
                borrowMoneyRecord.add(it)
            }
        }
        return borrowMoneyRecord

    }
}