package com.cjj.keepaccounts.fragment.statement

import com.cjj.keepaccounts.base.BaseTimeFragment
import com.cjj.keepaccounts.bean.StatementDetailBean
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.manager.LogoManager
import com.cjj.keepaccounts.utils.TimeMarkUtils
import com.cjj.keepaccounts.utils.ioToMain
import rx.Emitter
import rx.Observable
import rx.Subscription
import rx.functions.Action1
import java.math.BigDecimal
import java.util.*

/**
 * @author CJJ
 * Created by CJJ on 2017/11/13 13:55.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 * 显示成员的页面
 */
class MemberFragment : BaseTimeFragment() {

    val sql = "select tag.rowid , tag.uuid , tag.tag_name , sum( rate_money /(select count(*) from record_to_tag where record_to_tag.is_deleted==0 and record_id=record_name.uuid)) from record_tag as tag,record_to_tag as to_tag,record as record_name ,com_qeeniao_mobile_kdjz_Models_RecordType as type where  record_name.typeId>0 and record_name.typeId==type.uuid and type.isIncoming=? and record_name.the_date between ? and ? and tag.uuid=to_tag.tag_id and record_name.is_deleted=0 and to_tag.is_deleted==0 and to_tag.record_id=record_name.uuid group by tag.uuid"

    override fun getData(): Subscription? {
        return Observable.create<Pair<BigDecimal, ArrayList<StatementDetailBean>>>(action, Emitter.BackpressureMode.BUFFER)
                .ioToMain()
                .subscribe(onNext, onError)
    }

    private val action = Action1<Emitter<Pair<BigDecimal, ArrayList<StatementDetailBean>>>> {
        val countSql = "select count(*) from record where is_deleted==0 and typeId > 0 and the_date between ? and ?"
        val countQuery = DaoManager.daoSession.database.rawQuery(countSql, arrayOf(startDate.toString(), endDate.toString()))
        if (!countQuery.moveToNext() || countQuery.getInt(0) == 0) {
            it.onError(IllegalStateException())
        } else {

            val make = TimeMarkUtils.mark()
            val list = ArrayList<StatementDetailBean>()
            val arrayOf = arrayOf(isIncoming.toString(), startDate.toString(), endDate.toString())
            val rawQuery = DaoManager.daoSession.database.rawQuery(sql, arrayOf)

            while (rawQuery.moveToNext()) {
                val id = rawQuery.getInt(0)
                val uuid = rawQuery.getLong(1)
                val desc = rawQuery.getString(2)
                val money = rawQuery.getDouble(3)
                val bean = StatementDetailBean(startDate, endDate, uuid, money, isIncoming, desc, 1, LogoManager.getMemberColor(id), 0, 0.0)
                list.add(bean)
            }

            val totalSql = "select sum(r.rate_money) from record as r,com_qeeniao_mobile_kdjz_Models_RecordType as type where  r.typeId>0 and r.typeId==type.uuid and type.isIncoming=? and r.the_date between ? and ? and  r.is_deleted=0 "
            val totalQuery = DaoManager.daoSession.database.rawQuery(totalSql, arrayOf)

            val totalBigDecimal = if (totalQuery.moveToNext()) {
                BigDecimal(totalQuery.getDouble(0))
            } else {
                BigDecimal.ZERO
            }

            var memberMoney = BigDecimal.ZERO
            list.forEach { item ->
                memberMoney += BigDecimal(item.money)
            }

            val temp = (totalBigDecimal - memberMoney).toDouble()
            if (temp != 0.0) {
                val bean = StatementDetailBean(startDate, endDate, -1, temp, isIncoming, "无成员", 1, LogoManager.getMemberColor(0), 0, 0.0)
                list.add(bean)
            }

            list.sort()

            list.forEach { item ->
                item.ratio = (BigDecimal(item.money).multiply(BigDecimal(100)).divide(totalBigDecimal, 4, BigDecimal.ROUND_HALF_UP)).toDouble()
            }
            rawQuery.close()
            totalQuery.close()
            make.printTime("成员查询")
            it.onNext(Pair(totalBigDecimal, list))
            it.onCompleted()
        }
        countQuery.close()
    }
}
