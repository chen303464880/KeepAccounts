package com.cjj.keepaccounts.mvp.activity.tool.selectcolor

import com.cjj.keepaccounts.activity.tool.SelectColorActivity
import javax.inject.Inject

/**
 * @author CJJ
 * Created by CJJ on 2018/8/27 15:06.
 */
class PSelectColor @Inject constructor(view: SelectColorActivity, model: MSelectColor) : CSelectColor.Presenter(view, model) {
    override fun presenter(color: Int) {
        view.setData(model.getColorList(color))
    }
}