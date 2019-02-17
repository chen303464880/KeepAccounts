package com.cjj.keepaccounts.utils

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder


/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/6 17:06.
 */
class ViewHolderUtils<out T : View>(val context: Context, itemView: View) : BaseViewHolder(itemView) {
    private val mViews = SparseArray<T>()

    companion object {

        fun <T : View> createViewHolder(context: Context, itemView: View): BaseViewHolder {
            return ViewHolderUtils<T>(context, itemView)
        }

        fun <T : View> createViewHolder(context: Context,
                                        parent: ViewGroup, layoutId: Int): BaseViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                    false)
            return ViewHolderUtils<T>(context, itemView)
        }
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    fun getView(viewId: Int): T {
        var view = mViews.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view
    }

    fun getConvertView(): View {
        return itemView
    }
}