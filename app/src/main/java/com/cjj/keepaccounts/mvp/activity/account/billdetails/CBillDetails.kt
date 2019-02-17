package com.cjj.keepaccounts.mvp.activity.account.billdetails

import com.cjj.keepaccounts.activity.account.BillDetailsActivity
import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.bean.Record

/**
 * Created by CJJ on 2019/1/27 15:58.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
interface CBillDetails {

    abstract class Presenter(view: BillDetailsActivity, model: com.cjj.keepaccounts.base.BaseModel) : BasePresenter<BillDetailsActivity, BaseModel>(view, model) {
        abstract fun deleteRecord()
    }

    interface View : IView {
        fun setData(record: Record)
    }


}