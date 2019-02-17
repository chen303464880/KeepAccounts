package com.cjj.keepaccounts.fragment.statement

import com.cjj.keepaccounts.base.BaseTimeFragment
import com.cjj.keepaccounts.bean.StatementDetailBean
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.TimeMarkUtils
import com.cjj.keepaccounts.utils.ioToMain
import rx.Emitter
import rx.Observable
import rx.Subscription
import rx.functions.Action1
import java.math.BigDecimal

/**
 * @author CJJ
 * Created by CJJ on 2017/11/13 13:55.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 * 显示分类的页面
 */
class ClassifyFragment : BaseTimeFragment() {

    override fun getData(): Subscription? {
        return Observable.create<Pair<BigDecimal, ArrayList<StatementDetailBean>>>(action, Emitter.BackpressureMode.BUFFER)
                .ioToMain()
                .subscribe(onNext, onError)

    }

    private val action = Action1<Emitter<Pair<BigDecimal, java.util.ArrayList<StatementDetailBean>>>> {
        val countSql = "select count(*) from record where is_deleted==0 and typeId > 0 and the_date between ? and ?"
        val countQuery = DaoManager.daoSession.database.rawQuery(countSql, arrayOf(startDate.toString(), endDate.toString()))
        if (!countQuery.moveToNext() || countQuery.getInt(0) == 0) {
            it.onError(IllegalStateException())
        } else {
            val make = TimeMarkUtils.mark()
            val arrayList = arrayListOf<StatementDetailBean>()
            val sql = "select record.typeId,type.imgSrcId,type.color,type.typeDesc,sum(rate_money) as sum  from record ,com_qeeniao_mobile_kdjz_Models_RecordType as type where record.is_deleted==0 and record.typeId>0 and record.typeId==type.uuid and type.isIncoming=? and  record.the_date between ? and ? group by typeId order by sum desc"
            val rawQuery = DaoManager.daoSession.database.rawQuery(sql, arrayOf(isIncoming.toString(), startDate.toString(), endDate.toString()))
            var totalBigDecimal = BigDecimal(0)
            while (rawQuery.moveToNext()) {
                val uuid = rawQuery.getLong(0)
                val imgSrcId = rawQuery.getInt(1)
                val color = rawQuery.getInt(2)
                val desc = rawQuery.getString(3)
                val money = rawQuery.getDouble(4)
                totalBigDecimal += BigDecimal(money)
                val detailBean = StatementDetailBean(startDate, endDate, uuid, money, isIncoming
                        , desc, 0, color, imgSrcId, 0.0)
                arrayList.add(detailBean)
            }
            rawQuery.close()
            arrayList.forEach { item ->
                item.ratio = (BigDecimal(item.money).multiply(BigDecimal(100)).divide(totalBigDecimal, 4, BigDecimal.ROUND_HALF_UP)).toDouble()
            }
            make.printTime("分类查询")
            it.onNext(Pair(totalBigDecimal, arrayList))
            it.onCompleted()
        }
        countQuery.close()
    }

}