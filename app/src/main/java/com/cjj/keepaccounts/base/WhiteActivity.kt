package com.cjj.keepaccounts.base

import android.graphics.Color
import android.os.Bundle
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.utils.Utils

/**
 * @author chenjunjie
 * Created by CJJ on 2017/11/29 11:21.
 */
abstract class WhiteActivity<P : BasePresenter<out IView, out IModel>> : TitleActivity<P>() {
    private var isShowLine = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(Color.TRANSPARENT)
        nextTextColor = Utils.getColor(R.color.AppThemeColor)
        if (!isShowLine) {
            setActivityTitleColor(Color.WHITE)
        }
        setWhiteStatusBar(true)
        setActivityBackColor(Utils.getColor(R.color.AppThemeColor))
        setActivityBackIc(Utils.getDrawable(R.mipmap.fanhui_lan))
        setActivityTitleTextColor(Utils.getColor(R.color.text_color_655f5f))
    }

    override fun showTitleLine() {
        isShowLine = true
        setActivityTitleBackground(R.drawable.shape_bottom_while_e7_line)
    }

}