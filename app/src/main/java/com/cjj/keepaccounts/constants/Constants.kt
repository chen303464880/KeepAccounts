package com.cjj.keepaccounts.constants

import android.content.Context
import android.text.TextUtils
import com.cjj.keepaccounts.utils.Utils
import java.util.*

/**
 * Created by CJJ on 2018/7/11 20:55.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
object Constants {

    @JvmStatic
    private var deviceUuid_: String? = null
    @JvmStatic
    val deviceUuid: String
        get() {
            if (deviceUuid_ == null) {
                val preferences = Utils.context.getSharedPreferences("uuid", Context.MODE_PRIVATE)
                val temp = preferences.getString("deviceUuid", "")
                if (TextUtils.isEmpty(temp)) {
                    deviceUuid_ = UUID.randomUUID().toString()
                    preferences.edit().putString("deviceUuid", deviceUuid_).apply()
                } else {
                    deviceUuid_ = temp
                }
            }

            return deviceUuid_!!
        }

    /**
     * 借出 - 资产账户出钱 支出行为
     */
    const val LEND_MONEY_INCOME = -101L
    /**
     * 借出 - 借出账户收钱 收入行为
     */
    const val LEND_MONEY_EXPEND = -102L

    /**
     * 收款 - 还钱到资产账户中 是收入行为
     */
    const val LEND_MONEY_REPAY_INCOME = -103L
    /**
     * 收款 - 借出账户还钱 是支出行为
     */
    const val LEND_MONEY_REPAY_EXPEND = -104L

    /**
     * 利息 - 借钱给别人,收的别人的利息 是收入行为
     */
    const val LEND_MONEY_INTEREST = -105L

    /**
     * 借入 - 资产账户收钱 收入行为
     */
    const val BORROW_MONEY_INCOME = -106L
    /**
     * 借入 - 借入账户出钱 支出行为
     */
    const val BORROW_MONEY_EXPEND = -107L

    /**
     * 还款 - 资产账户中还钱 支出行为
     */
    const val BORROW_MONEY_REPAY_EXPEND = -108L

    /**
     * 还款 - 借入账户收钱 收入行为
     */
    const val BORROW_MONEY_REPAY_INCOME = -109L

    /**
     * 利息 - 向别人借钱,支付的别人的利息 是支出行为
     */
    const val BORROW_MONEY_INTEREST = -110L


}