package com.cjj.keepaccounts.mvp.activity.tool.selectcolor

import com.cjj.keepaccounts.activity.tool.SelectColorActivity
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IModel
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.bean.SelectColorBean

/**
 * @author CJJ
 * Created by CJJ on 2018/8/27 15:03.
 */
interface CSelectColor {

    abstract class Presenter(view: SelectColorActivity, model: MSelectColor) : BasePresenter<SelectColorActivity, MSelectColor>(view, model) {
        abstract fun presenter(color: Int)
    }


    interface View : IView {
        fun setData(list: List<SelectColorBean>)
    }

    interface Model : IModel {
        fun getColorList(color: Int): List<SelectColorBean>
    }
}