package com.cjj.keepaccounts.base.empty

import android.support.v7.widget.RecyclerView
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter

/**
 * Created by CJJ on 2019/1/30 14:22.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
interface AdapterView<R> {
    val adapter: BaseRecyclerViewAdapter<out RecyclerView.ViewHolder, R>

    fun setAdapterData(data: List<R>) {
        adapter.setData(data)
    }

    fun insertData(data: List<R>) {
        adapter.insertData(data)
    }

    fun insertData(data: R) {
        adapter.insertData(data)
    }

    fun insertData(index: Int, data: R) {
        adapter.insertData(index, data)
    }

    fun updateData(data: R) {
        adapter.updateData(data)
    }

    fun updateData(index: Int) {
        adapter.updateData(index)
    }

    fun updateData(index: Int, data: R) {
        adapter.updateData(index, data)
    }

    fun removeData(data: R) {
        adapter.removeData(data)
    }

    fun removeData(index: Int) {
        adapter.removeData(index)
    }

    fun removeData(index: Int, count: Int) {
        adapter.removeData(index, count)
    }
}