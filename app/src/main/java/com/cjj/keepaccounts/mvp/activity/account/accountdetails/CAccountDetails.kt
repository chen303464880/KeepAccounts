package com.cjj.keepaccounts.mvp.activity.account.accountdetails

import com.cjj.keepaccounts.activity.account.AccountDetailsActivity
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IModel
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.bean.AccountMonthDetails
import com.cjj.keepaccounts.bean.Record
import rx.Observable

/**
 * @author CJJ
 * Created by CJJ on 2018/9/30 9:47.
 */
interface CAccountDetails {

    abstract class Presenter(view: AccountDetailsActivity, model: MAccountDetails) : BasePresenter<AccountDetailsActivity, MAccountDetails>(view, model) {
        abstract fun calculateData(uuid: Long, year: Int)
    }

    interface View : IView {
        fun setYears(years: ArrayList<Int>)
        fun setData(arrayList: ArrayList<Pair<AccountMonthDetails, ArrayList<Record>>>)
        fun detailsKongVisibility(visibility: Int)
        fun setBalanceHintText(hint: String)
        fun setType(type: Int)
        fun setDividerHeight(height: Int)
        fun setBalance(money: Double)
        fun setIncomeText(money: String)
        fun setExpenditureText(money: String)
    }

    interface Model : IModel {
        fun getYearData(uuid: Long, year: Int): Observable<List<Record>>
        fun getYears(year: Int): ArrayList<Int>
    }
}