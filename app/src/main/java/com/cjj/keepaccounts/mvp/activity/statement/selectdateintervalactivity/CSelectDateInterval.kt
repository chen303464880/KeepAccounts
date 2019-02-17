package com.cjj.keepaccounts.mvp.activity.statement.selectdateintervalactivity

import com.cjj.keepaccounts.activity.statement.SelectDateIntervalActivity
import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IView

/**
 * @author CJJ
 * Created by CJJ on 2018/8/27 14:45.
 */
interface CSelectDateInterval {
    abstract class Presenter(view: SelectDateIntervalActivity, model: BaseModel) : BasePresenter<SelectDateIntervalActivity, BaseModel>(view, model)

    interface View : IView
}