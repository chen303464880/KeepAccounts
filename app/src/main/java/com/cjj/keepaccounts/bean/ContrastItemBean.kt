package com.cjj.keepaccounts.bean

import android.util.SparseArray

/**
 * Created by CJJ on 2018/5/17 20:52.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
data class ContrastItemBean(val uuid: Long, val desc: String, val imgId: Int, val color: Int, val maxMoney: Double, val totalMoney: Double, val monthCount: Int, val average: Double, val monthInfo: SparseArray<Double>, val orderIndex: Int) : Comparable<ContrastItemBean> {
    override fun compareTo(other: ContrastItemBean): Int {
        return this.orderIndex - other.orderIndex
    }
}