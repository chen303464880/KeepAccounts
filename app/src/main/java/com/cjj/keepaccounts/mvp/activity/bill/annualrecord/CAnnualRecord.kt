package com.cjj.keepaccounts.mvp.activity.bill.annualrecord

import com.cjj.keepaccounts.activity.bill.AnnualRecordActivity
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IModel
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.base.empty.AdapterView
import com.cjj.keepaccounts.bean.AnnalRecordMonthBean

/**
 * Created by CJJ on 2019/1/30 18:05.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
interface CAnnualRecord {

    abstract class Presenter(view: AnnualRecordActivity, model: MAnnualRecord) : BasePresenter<AnnualRecordActivity, MAnnualRecord>(view, model)

    interface View : IView, AdapterView<AnnalRecordMonthBean>

    interface Model : IModel {
        fun getMonth(): ArrayList<AnnalRecordMonthBean>
    }
}