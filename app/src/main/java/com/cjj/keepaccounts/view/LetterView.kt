package com.cjj.keepaccounts.view

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/10 14:04.
 */
class LetterView : View {

    /**
     * #>触碰时候的背景颜色
     */
    val COLOR_BG = 0x17000000
    /**
     * #>没有触碰时的背景颜色
     */
    val COLOR_NO_BG = 0x00000000
    /**
     * #>触碰状态下所有字母的颜色
     */
    val TEXT_COLOR_NORMAL = -0xababac
    /**
     * #>选中的字母颜色
     */
    val TEXT_COLOR_SELECTED = -0x5c5c5d
    /**
     * 没有触碰状态下的字母颜色
     */
    val TEXT_COLOR_UNTOUCH = -0x5c5c5d
    val letters = "☆ABCDEFGHIJKLMNOPQRSTUVWXYZ#"

    //每个字母的高度
    private val abcHeight: Int = 0
    private val paint: Paint? = null
    private val selectedIndex = 0//#>被选中字母的下标
    private val isTouch = false//#>是否处于触碰的状态

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

}