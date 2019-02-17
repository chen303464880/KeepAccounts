package com.cjj.keepaccounts.adapter

import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.cjj.keepaccounts.base.BaseDeleteAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.utils.ViewHolderUtils


/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/6 17:03.
 */
abstract class HeaderAndFooterAdapter<T : BaseViewHolder, R> : BaseDeleteAdapter<BaseViewHolder, R>() {

    private val baseItemTypeHeader = 1000
    private val baseItemTypeFooter = 2000

    private val mHeaderViews = SparseArray<View>()
    private val mFooterViews = SparseArray<View>()


    fun addHeaderView(view: View) {
        mHeaderViews.put(mHeaderViews.size() + baseItemTypeHeader, view)
        headCount = mHeaderViews.size()
    }

    fun addFooterView(view: View) {
        mFooterViews.put(mFooterViews.size() + baseItemTypeFooter, view)
    }

    fun isHeaderViewPos(position: Int): Boolean = position < getHeaderViewCount()


    fun isFooterViewPos(position: Int): Boolean = position >= getHeaderViewCount() + getRealItemCount()


    fun getHeaderViewCount(): Int = mHeaderViews.size()
    fun getFooterViewCount(): Int = mFooterViews.size()


    override fun insertData(data: List<R>) {
        this.data.addAll(data)
        notifyItemRangeInserted(this.data.size - data.size - 1 + getHeaderViewCount(), data.size)
    }

    override fun insertData(data: R) {
        this.data.add(data)
        notifyItemInserted(this.data.size - 1 + getHeaderViewCount())
    }

    override fun insertData(index: Int, data: R) {
        this.data.add(index, data)
        notifyItemInserted(index + getHeaderViewCount())
    }

    override fun updateData(data: R) {
        if (this.data.contains(data)) {
            val indexOf = this.data.indexOf(data)
            notifyItemChanged(indexOf + getHeaderViewCount())
        }
    }

    override fun updateData(index: Int) {
        notifyItemChanged(index + getHeaderViewCount())
    }

    override fun updateData(index: Int, data: R) {
        this.data[index] = data
        notifyItemChanged(index + getHeaderViewCount())
    }

    override fun removeData(data: R) {
        if (this.data.contains(data)) {
            val indexOf = this.data.indexOf(data)
            this.data.remove(data)
            notifyItemRemoved(indexOf + getHeaderViewCount())
        }
    }

    override fun removeData(index: Int) {
        if (index < this.data.size) {
            this.data.removeAt(index)
            notifyItemRemoved(index + getHeaderViewCount())
        }
    }

    override fun removeData(index: Int, count: Int) {

        if (index + count - 1 < this.data.size) {
            for (i in 0 until count) {
                this.data.removeAt(index)
            }
            notifyItemRangeRemoved(index + getHeaderViewCount(), count)
        }
    }


    override fun getItemViewType(position: Int): Int {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position)
        } else if (isFooterViewPos(position)) {
            return mFooterViews.keyAt(position - getHeaderViewCount() - getRealItemCount())
        }
        return getContentItemViewType(position - getHeaderViewCount())
    }

    open fun getContentItemViewType(position: Int) = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        if (mHeaderViews.get(viewType) != null) {
            return ViewHolderUtils.createViewHolder<View>(parent.context, mHeaderViews.get(viewType))
        } else if (mFooterViews.get(viewType) != null) {
            return ViewHolderUtils.createViewHolder<View>(parent.context, mFooterViews.get(viewType))
        }
        return onCreateContentViewHolder(parent, viewType)
    }

    abstract fun onCreateContentViewHolder(parent: ViewGroup, viewType: Int): T

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (isHeaderViewPos(position)) {
            return
        }
        if (isFooterViewPos(position)) {
            return
        }
        super.onBindViewHolder(holder, position)
        @Suppress("UNCHECKED_CAST")
        onBindContentViewHolder(holder as T, position - getHeaderViewCount())
    }

    abstract fun onBindContentViewHolder(holder: T, position: Int)


    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.layoutPosition
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            val lp = holder.itemView.layoutParams

            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {

                lp.isFullSpan = true
            }
        }
    }

    private fun getRealItemCount(): Int = data.size

    override fun getItemCount(): Int = getHeaderViewCount() + getFooterViewCount() + getRealItemCount()
}