package com.cjj.keepaccounts.mvp.activity.tool.selectcolor

import com.cjj.keepaccounts.bean.SelectColorBean
import com.cjj.keepaccounts.manager.LogoManager
import org.jetbrains.anko.collections.forEach
import javax.inject.Inject

/**
 * @author CJJ
 * Created by CJJ on 2018/8/27 15:03.
 */
class MSelectColor @Inject constructor() : CSelectColor.Model {
    override fun getColorList(color: Int): List<SelectColorBean> {
        val colorList = LogoManager.AccountColor.colorList
        val list = arrayListOf<SelectColorBean>()
        colorList.forEach { _, v ->
            list.add(SelectColorBean(v, color == v))
        }
        return list
    }
}