package com.cjj.keepaccounts.base

import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.base.viewholder.ClickViewHolder
import com.cjj.keepaccounts.view.SlideDeleteView

/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/8 10:55.
 */
abstract class BaseDeleteAdapter<T : RecyclerView.ViewHolder, R> : BaseRecyclerViewAdapter<T, R>() {
    private val openViews = arrayListOf<SlideDeleteView>()

    private var deleteListener: ((position: Int, itemInfo: R) -> Unit)? = null

    fun setOnDeleteListener(listener: (position: Int, itemInfo: R) -> Unit) {
        this.deleteListener = listener
    }

    open class DeleteViewHolder(itemView: View) : BaseViewHolder(itemView), ClickViewHolder {
        @BindView(R.id.delete_view)
        lateinit var deleteView: SlideDeleteView
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        if (holder is DeleteViewHolder) {
            holder.deleteView.setOnSlideListener(object : SlideDeleteView.SlideListener {
                override fun onStartOpen(mSlideDeleteView: SlideDeleteView) {
                    close()
                }

                override fun onStartClose(mSlideDeleteView: SlideDeleteView) {
                }

                override fun onOpen(mSlideDeleteView: SlideDeleteView) {
                    openViews.add(mSlideDeleteView)
                }

                override fun onClose(mSlideDeleteView: SlideDeleteView) {
                    openViews.remove(mSlideDeleteView)
                }

                override fun onDraging(mSlideDeleteView: SlideDeleteView, mSlideOffset: Float) {
                }
            })
            holder.deleteView.setOnDeleteListener {
                close()
                deleteListener?.invoke(holder.adapterPosition - headCount, getItem(holder.adapterPosition - headCount))
            }
            holder.deleteView.setOnContentClickListener(View.OnClickListener {
                if (isOpen()) {
                    close()
                } else {
                    itemClickListener?.invoke(holder, holder.adapterPosition - headCount, getItem(holder.adapterPosition - headCount))
                }
            })
        } else if (holder is ClickViewHolder) {
            holder.itemView.setOnClickListener {
                itemClickListener?.invoke(holder, holder.adapterPosition - headCount, getItem(holder.adapterPosition - headCount))
            }
        }
    }

    open fun isOpen(): Boolean = openViews.size != 0

    open fun close() {
        openViews.forEach {
            it.close(true)
        }
        openViews.clear()
    }
}
