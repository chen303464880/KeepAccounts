package com.cjj.keepaccounts.mvp.activity.bill.searchrecord

import com.cjj.keepaccounts.activity.bill.SearchRecordActivity
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IModel
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.bean.Record
import rx.Observable
import java.math.BigDecimal

/**
 * @author CJJ
 * Created by CJJ on 2018/9/28 15:16.
 */
interface CSearchRecord {
    abstract class Presenter(view: SearchRecordActivity, model: MSearchRecord) : BasePresenter<SearchRecordActivity, MSearchRecord>(view, model) {
        abstract fun search(key: String)
    }

    interface View : IView {
        fun setMoneyInfo(moneyInfo: String)
        fun setRecordDate(date: List<Record>)
        fun updateData(index: Int, entity: Record)
        fun removeData(index: Int)
    }

    abstract class Model : IModel {
        abstract var income: BigDecimal
        abstract var expend: BigDecimal
        abstract fun getRecordDate(key: String): Observable<List<Record>>
    }
}