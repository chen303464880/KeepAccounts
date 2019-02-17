package com.cjj.keepaccounts.mvp.activity.account.accountdetails

import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.bean.RecordDao
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.TimeUtils
import rx.Observable
import javax.inject.Inject

/**
 * Created by CJJ on 2019/1/17 16:20.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class MAccountDetails @Inject constructor() : CAccountDetails.Model {
    override fun getYearData(uuid: Long, year: Int): Observable<List<Record>> {
        //查询当前年份的数据
        return DaoManager.getRecordDao().queryBuilder()
                .where(RecordDao.Properties.AccountId.eq(uuid))
                .where(RecordDao.Properties.Year.eq(year))
                .where(RecordDao.Properties.IsDeleted.eq(0))
                .where(RecordDao.Properties.TypeId.notEq(-105))
                .orderDesc(RecordDao.Properties.TheDate)
                .orderDesc(RecordDao.Properties.RTime)
                .orderDesc(RecordDao.Properties.CreateTime)
                .rx().list()
    }

    override fun getYears(year: Int): ArrayList<Int> {
        val sqlDistinctName = "select distinct ${RecordDao.Properties.Year.columnName} from ${RecordDao.TABLENAME} where ${RecordDao.Properties.Year.columnName}!=0 and ${RecordDao.Properties.RTime.columnName}!=0 order by ${RecordDao.Properties.Year.columnName} desc"
        val years = ArrayList<Int>()
        val c = DaoManager.getRecordDao().database.rawQuery(sqlDistinctName, null)
        while (c.moveToNext()) {
            years.add(c.getInt(0))
        }
        c.close()
        if (years.isNotEmpty()) {
            if (years.first() != TimeUtils.year) {
                val start = if (years.first() < TimeUtils.year) TimeUtils.year else years.first()
                val end = years.last()
                years.clear()
                years += start downTo end
            }
        }
        return years
    }
}