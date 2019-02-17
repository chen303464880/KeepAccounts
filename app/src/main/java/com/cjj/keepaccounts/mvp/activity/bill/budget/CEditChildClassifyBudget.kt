package com.cjj.keepaccounts.mvp.activity.bill.budget


import com.cjj.keepaccounts.activity.bill.budget.EditChildClassifyBudgetActivity
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IModel
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.base.empty.AdapterView
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.bean.SameCityMainType

/**
 * @author CJJ
 * Created by CJJ on 2018/9/26 16:21.
 */
interface CEditChildClassifyBudget {
    abstract class Presenter(view: EditChildClassifyBudgetActivity, model: MEditChildClassifyBudget) : BasePresenter<EditChildClassifyBudgetActivity, MEditChildClassifyBudget>(view, model)

    interface View : IView, AdapterView<RecordType> {
        fun setHeadViewData(sameCityType: SameCityMainType)
    }

    interface Model : IModel
}