package com.cjj.keepaccounts.utils

import java.util.*

/**
 * @author chenjunjie
 * Created by CJJ on 2017/12/21 11:10.
 */
object MoneyUtils {
    @JvmStatic
    fun formatMoney(money: Float): String = String.format(Locale.CHINA, "%.2f", money)

    @JvmStatic
    fun formatMoney(money: Double): String = String.format(Locale.CHINA, "%.2f", money)
}

fun Double.toMoney(): String = String.format(Locale.CHINA, "%.2f", this)

fun Float.toMoney(): String = String.format(Locale.CHINA, "%.2f", this)