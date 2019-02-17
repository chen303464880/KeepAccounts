package com.cjj.keepaccounts.utils

import android.support.v7.widget.RecyclerView


/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/13 15:56.
 */
interface ItemTouchHelperAdapter {

    fun getFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int

    fun onItemMove(source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder)
    //数据删除
    fun onItemDissmiss(source: RecyclerView.ViewHolder)

    //drag或者swipe选中
    fun onItemSelect(source: RecyclerView.ViewHolder)

    //状态清除
    fun onItemClear(source: RecyclerView.ViewHolder)

}