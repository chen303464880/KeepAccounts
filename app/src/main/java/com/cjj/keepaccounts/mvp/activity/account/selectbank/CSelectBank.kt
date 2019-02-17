package com.cjj.keepaccounts.mvp.activity.account.selectbank

import com.cjj.keepaccounts.activity.account.SelectBankActivity
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IModel
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.base.empty.AdapterView
import com.cjj.keepaccounts.bean.BankInfoBean

/**
 * Created by CJJ on 2019/1/30 17:32.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
interface CSelectBank {

    abstract class Presenter(view: SelectBankActivity, model: MSelectBank) : BasePresenter<SelectBankActivity, MSelectBank>(view, model) {
        abstract fun searchBank(key: String)
    }

    interface View : IView, AdapterView<BankInfoBean>

    interface Model : IModel {
        val backs: ArrayList<BankInfoBean>
        fun searchBank(key: String): ArrayList<BankInfoBean>
    }
}