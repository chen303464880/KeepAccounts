package com.cjj.keepaccounts.mvp.activity.account.borrowmoneyrecord


import com.cjj.keepaccounts.activity.account.BorrowMoneyRecordActivity
import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IModel
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.bean.Account
import com.cjj.keepaccounts.bean.Credit

/**
 * Created by CJJ on 2019/1/30 14:20.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
interface CBorrowMoneyRecord {

    abstract class Presenter(view: BorrowMoneyRecordActivity, model: MBorrowMoneyRecord) : BasePresenter<BorrowMoneyRecordActivity, MBorrowMoneyRecord>(view, model) {
        abstract val account: Account

    }

    interface View<R> : IView, com.cjj.keepaccounts.base.empty.AdapterView<R> {
        fun refreshView(itemCount: Int)
        fun setIntervalTime(time: Long)
        fun setMoney(money: String)
    }

    interface Model : IModel {
        fun getData(uuid: Long): ArrayList<Credit>
    }
}