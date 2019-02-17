package com.cjj.keepaccounts.mvp.activity.bill.calendarselect

import com.cjj.keepaccounts.activity.bill.CalendarSelectActivity
import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.base.empty.AdapterView
import com.cjj.keepaccounts.bean.CalendarDayBean

/**
 * Created by CJJ on 2019/1/31 18:26.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
interface CCalendarSelect {


    abstract class Presenter(view: CalendarSelectActivity, model: BaseModel) : BasePresenter<CalendarSelectActivity, BaseModel>(view, model)

    interface View : IView, AdapterView<CalendarDayBean> {
        fun setPosition(position: Int)
    }
}