package com.cjj.keepaccounts.bean

import android.text.SpannableStringBuilder

/**
 * Created by CJJ on 2018/3/31 15:36.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
data class CalculatorEventBean(val equation: String, val nums: ArrayList<String>, val result: SpannableStringBuilder)