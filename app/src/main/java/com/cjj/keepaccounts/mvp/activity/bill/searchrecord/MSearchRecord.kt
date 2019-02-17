package com.cjj.keepaccounts.mvp.activity.bill.searchrecord

import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.ioToMain
import rx.Observable
import rx.functions.Func0
import java.math.BigDecimal
import javax.inject.Inject

/**
 * @author CJJ
 * Created by CJJ on 2018/9/28 15:25.
 */
class MSearchRecord @Inject constructor() : CSearchRecord.Model() {
    override var income = BigDecimal.ZERO!!
    override var expend = BigDecimal.ZERO!!


    private var key: String = ""
    override fun getRecordDate(key: String): Observable<List<Record>> {
        this.key = key
        return Observable.defer(observableFactory)
                .ioToMain()
    }

    private val sql = "select t.photo3,t.source,t.uuid,t.typeId,t.content,t.the_date,t.synced1,t.rate_money,t.mtime,t.list_id,t.photo2,t.rate_name,t.photo1,t.day,t.money,t.notice_id,t.creditID,t.year,t.the_hour,t.month,t.is_impulse,t.device_id,t.is_deleted,t.action_id,t.accountId,t.createTime,t.rtime,t.user_id from record as t ,com_qeeniao_mobile_kdjz_Models_RecordType as j where t.typeId>0 and t.typeId!=10000 and t.is_deleted=0 and t.typeId=j.uuid and (t.content like ? or j.typeDesc like ? ) order by t.the_date desc ,t.rtime desc , t.createTime desc"

    private val observableFactory = Func0<Observable<List<Record>>> {
        val list = ArrayList<Record>()
        val rawQuery = DaoManager.daoSession.database.rawQuery(sql, arrayOf(key, key))
        income = BigDecimal.ZERO
        expend = BigDecimal.ZERO
        while (rawQuery.moveToNext()) {
            val record = Record()
            record.photo3 = rawQuery.getString(0)
            record.source = rawQuery.getString(1)
            record.uuid = rawQuery.getLong(2)
            record.typeId = rawQuery.getLong(3)
            record.content = rawQuery.getString(4)
            record.theDate = rawQuery.getInt(5)
            record.synced1 = rawQuery.getInt(6)
            record.rateMoney = rawQuery.getDouble(7)
            record.mTime = rawQuery.getLong(8)
            record.listId = rawQuery.getLong(9)
            record.photo2 = rawQuery.getString(10)
            record.rateName = rawQuery.getString(11)
            record.photo1 = rawQuery.getString(12)
            record.day = rawQuery.getInt(13)
            record.money = rawQuery.getDouble(14)
            record.noticeId = rawQuery.getLong(15)
            record.creditID = rawQuery.getLong(16)
            record.year = rawQuery.getInt(17)
            record.theHour = rawQuery.getInt(18)
            record.month = rawQuery.getInt(19)
            record.isImpulse = rawQuery.getInt(20)
            record.deviceId = rawQuery.getString(21)
            record.isDeleted = rawQuery.getInt(22)
            record.actionId = rawQuery.getLong(23)
            record.accountId = rawQuery.getLong(24)
            record.createTime = rawQuery.getLong(25)
            record.rTime = rawQuery.getLong(26)
            record.userId = rawQuery.getString(27)

            val isIncoming = record.recordType.isIncoming
            if (isIncoming == 1 || (isIncoming == -1 && record.rateMoney > 0)) {
                income += BigDecimal(record.rateMoney)
            } else {
                expend += BigDecimal(Math.abs(record.rateMoney))
            }
            list.add(record)
        }
        rawQuery.close()
        Observable.just(list)
    }

}