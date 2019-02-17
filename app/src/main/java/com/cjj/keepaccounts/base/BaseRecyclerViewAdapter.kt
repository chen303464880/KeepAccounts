package com.cjj.keepaccounts.base

import android.support.v7.widget.RecyclerView
import android.view.View
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.base.viewholder.ClickViewHolder

/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/8 10:55.
 */
abstract class BaseRecyclerViewAdapter<T : RecyclerView.ViewHolder, R> : RecyclerView.Adapter<T>() {

    val data = ArrayList<R>()
    protected var headCount = 0

    open class ContentViewHolder(itemView: View) : BaseViewHolder(itemView)

    protected var itemClickListener: ((holder: T, position: Int, itemInfo: R) -> Unit)? = null

    fun setOnItemClickListener(listener: ((holder: T, position: Int, itemInfo: R) -> Unit)) {
        this.itemClickListener = listener
    }


    open fun setData(data: List<R>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    open fun insertData(data: List<R>) {
        this.data.addAll(data)
        notifyItemRangeInserted(this.data.size - data.size - 1, data.size)
    }

    open fun insertData(data: R) {
        this.data.add(data)
        notifyItemInserted(this.data.size - 1)
    }

    open fun insertData(index: Int, data: R) {
        this.data.add(index, data)
        notifyItemInserted(index)
    }

    open fun updateData(data: R) {
        if (this.data.contains(data)) {
            val indexOf = this.data.indexOf(data)
            notifyItemChanged(indexOf)
        }
    }

    open fun updateData(index: Int) {
        notifyItemChanged(index)
    }

    open fun updateData(index: Int, data: R) {
        this.data[index] = data
        notifyItemChanged(index)
    }

    open fun removeData(data: R) {
        if (this.data.contains(data)) {
            val indexOf = this.data.indexOf(data)
            this.data.remove(data)
            notifyItemRemoved(indexOf)
        }
    }

    open fun removeData(index: Int) {
        if (index < this.data.size) {
            this.data.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    open fun removeData(index: Int, count: Int) {

        if (index + count - 1 < this.data.size) {
            for (i in 0 until count) {
                this.data.removeAt(index)
            }
            notifyItemRangeRemoved(index, count)
        }
    }

    open fun clear() {
        val size = this.data.size
        this.data.clear()
        notifyItemRangeRemoved(headCount, size)
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        if (holder is ClickViewHolder) {
            holder.itemView.setOnClickListener {
                itemClickListener?.invoke(holder, holder.adapterPosition - headCount, getItem(holder.adapterPosition - headCount))
            }
        }
    }

    open fun getItem(position: Int): R = data[position]

    override fun getItemCount(): Int = data.size

}

