package com.cjj.keepaccounts.mvp.activity.statement.selectdateintervalactivity

import com.cjj.keepaccounts.activity.statement.SelectDateIntervalActivity
import com.cjj.keepaccounts.base.BaseModel
import javax.inject.Inject

/**
 * @author CJJ
 * Created by CJJ on 2018/8/27 14:49.
 */
class PSelectDateInterval @Inject constructor(view: SelectDateIntervalActivity, model: BaseModel) : CSelectDateInterval.Presenter(view, model)