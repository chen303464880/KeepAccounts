package com.cjj.keepaccounts.mvp.activity.account.addaccount

import com.cjj.keepaccounts.activity.account.AddAccountActivity
import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.bean.Account

/**
 * Created by CJJ on 2019/1/23 15:48.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
interface CAddAccount {
    abstract class Presenter(view: AddAccountActivity, model: BaseModel) : BasePresenter<AddAccountActivity,BaseModel>(view, model) {
        abstract fun delete(account: Account)
        abstract fun insert(account: Account)

    }

    interface View : IView
}