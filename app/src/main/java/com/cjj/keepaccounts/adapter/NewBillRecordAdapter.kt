package com.cjj.keepaccounts.adapter

import android.animation.Animator
import android.animation.ValueAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.base.viewholder.ClickViewHolder
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.dao.RecordTypeTool
import com.cjj.keepaccounts.enum.ListViewType
import com.cjj.keepaccounts.manager.LogoManager
import com.cjj.keepaccounts.utils.ItemTouchHelperAdapter
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.drawableTop
import com.cjj.keepaccounts.view.rippleBorderless
import org.jetbrains.anko.*

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/1 16:35.
 */
class NewBillRecordAdapter : BaseRecyclerViewAdapter<BaseViewHolder, RecordType>(), ItemTouchHelperAdapter {

    //图标的直径
    private val diameter = Utils.dip2px(34F)

    private var pivotX = 0F
    private var pivotY = 0F

    var isEdit = false
        set(value) {
            field = value
            if (value) {
                valueAnimator.start()
            } else {
                valueAnimator.cancel()
                valueAnimator.removeAllListeners()
                valueAnimator.removeAllUpdateListeners()
            }
            listener?.invoke(value)
        }

    private var listener: ((isEdit: Boolean) -> Unit)? = null

    fun setOnEditChangeListener(listener: (isEdit: Boolean) -> Unit) {
        this.listener = listener
    }

    private val valueAnimator = ValueAnimator.ofFloat(-2F, 2F)
    private var isReverse = false

    init {
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.repeatMode = ValueAnimator.REVERSE
        valueAnimator.duration = 200
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                isReverse = !isReverse
            }

            override fun onAnimationEnd(animation: Animator?) {

            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })
    }

    class ViewHolder(itemView: FrameLayout) : BaseViewHolder(itemView), ClickViewHolder {
        val textView: TextView = itemView.getChildAt(0) as TextView
        val deleteView: ImageView = itemView.getChildAt(1) as ImageView
    }

    class AddViewHolder(itemView: TextView) : ContentViewHolder(itemView) {
        val textView: TextView = itemView
    }

    override fun getItemViewType(position: Int): Int =
            if (position < itemCount - 1) {
                ListViewType.TYPE_CONTENT.type
            } else {
                ListViewType.TYPE_FOOTER.type
            }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == ListViewType.TYPE_CONTENT.type) {
            return NewBillRecordAdapter.ViewHolder(parent.context.frameLayout {
                textView {
                    setTextColor(Utils.getColor(R.color.text_color_655f5f))
                    gravity = Gravity.CENTER_HORIZONTAL
                    maxLines = 1
                    ellipsize = TextUtils.TruncateAt.END
                    compoundDrawablePadding = Utils.dip2px(8F)
                    setPadding(0, dip(10), 0, 0)
                }.lparams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                imageView {
                    imageResource = R.mipmap.icon_circle_close
                    visibility = View.GONE
                }.lparams(dip(22), dip(22)) {
                    topMargin = 10
                    rightMargin = diameter / 2
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                rippleBorderless()
            })
        } else {
            val textView = TextView(parent.context)
            textView.apply {
                setTextColor(Utils.getColor(R.color.text_color_655f5f))
                gravity = Gravity.CENTER_HORIZONTAL
                maxLines = 1
                ellipsize = TextUtils.TruncateAt.END
                compoundDrawablePadding = Utils.dip2px(8F)
                setPadding(0, dip(10), 0, 0)
                rippleBorderless()
                layoutParams = RecyclerView.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            }
            return NewBillRecordAdapter.AddViewHolder(textView)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

        when (holder) {
            is ViewHolder -> {
                val recordType = getItem(position)
                holder.textView.text = recordType.typeDesc
                holder.textView.drawableTop = Utils.getDrawable(LogoManager.getTypeLogo(recordType.imgSrcId), diameter)
                holder.deleteView.visibility = if (isEdit) View.VISIBLE else View.GONE
                if (pivotX == 0F) {
                    holder.textView.doOnPreDraw {
                        pivotX = (holder.textView.width / 2).toFloat()
                        pivotY = (holder.textView.topPadding + diameter / 2).toFloat()
                        holder.itemView.pivotX = pivotX
                        holder.itemView.pivotY = pivotY
                    }
                } else {
                    holder.itemView.pivotX = pivotX
                    holder.itemView.pivotY = pivotY
                }


                if (holder.itemView.getTag(R.id.value_update_animator) == null) {
                    val offset: Float
                    if (holder.itemView.getTag(R.id.offset) == null) {
                        offset = (Math.random() * 2.0).toFloat()
                        holder.itemView.setTag(R.id.offset, offset)
                    } else {
                        offset = holder.itemView.getTag(R.id.offset) as Float
                    }
                    val updateListener = getAnimatorUpdateListener(offset, holder)
                    valueAnimator.addUpdateListener(updateListener)
                    holder.itemView.setTag(R.id.value_update_animator, updateListener)
                }

                if (holder.itemView.getTag(R.id.value_animator) == null) {
                    val listener = getAnimatorListener(holder)
                    valueAnimator.addListener(listener)
                    holder.itemView.setTag(R.id.value_animator, listener)
                }

                holder.itemView.setOnLongClickListener {
                    if (!isEdit) {
                        isEdit = true
                        return@setOnLongClickListener true
                    }
                    return@setOnLongClickListener false
                }

                holder.deleteView.setOnClickListener {
                    RecordTypeTool.delete(recordType)
                }
            }
            is NewBillRecordAdapter.AddViewHolder -> {
                holder.textView.text = Utils.getString(R.string.edit)
                val drawable = Utils.getDrawable(R.drawable.image_shape_new_classify)
                drawable.setBounds(0, 0, diameter, diameter)
                holder.textView.drawableTop = drawable
            }
        }



        holder.itemView.setOnClickListener {
            if (isEdit) {
                isEdit = false
            } else {
                val adapterPosition = holder.adapterPosition
                itemClickListener?.invoke(holder, adapterPosition, if (adapterPosition < data.size) getItem(position) else RecordType())
            }
        }
    }

    private fun getAnimatorListener(holder: ViewHolder): Animator.AnimatorListener {
        return object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                holder.itemView.setLayerType(View.LAYER_TYPE_NONE, null)
            }

            override fun onAnimationCancel(animation: Animator?) {
                holder.itemView.rotation = 0F
                holder.deleteView.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animator?) {
                holder.itemView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
                holder.deleteView.visibility = View.VISIBLE
            }
        }
    }

    private fun getAnimatorUpdateListener(offset: Float, holder: ViewHolder): ValueAnimator.AnimatorUpdateListener {
        return ValueAnimator.AnimatorUpdateListener {
            var rotation: Float
            if (isReverse) {
                rotation = (it.animatedValue as Float) - offset
                if (rotation < -2F) {
                    rotation = -4F - rotation
                }
            } else {
                rotation = offset + (it.animatedValue as Float)
                if (rotation > 2F) {
                    rotation = 4F - rotation
                }
            }
            holder.itemView.rotation = rotation
        }
    }


    private var startPosition = 0
    private var endPosition = 0
    private var selectUpdateListener: ValueAnimator.AnimatorUpdateListener? = null

    override fun onItemMove(source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) {

        val fromPosition = source.adapterPosition
        val toPosition = target.adapterPosition
//        Log.i("TAG", "fromPosition:$fromPosition \t toPosition:$toPosition")
        val start = 0
        val end = data.size - 1
        if (fromPosition in start..end && toPosition in start..end) {

            //交换数据位置
            val recordType = data.removeAt(fromPosition)
            data.add(toPosition, recordType)
            //刷新位置交换
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    override fun getFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return if (viewHolder.adapterPosition != itemCount - 1) {
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END
        } else {
            0
        }
    }

    override fun onItemDissmiss(source: RecyclerView.ViewHolder) {

    }

    override fun onItemSelect(source: RecyclerView.ViewHolder) {
        //获取开始的位置
        startPosition = source.adapterPosition
        //暂停动画
        selectUpdateListener = source.itemView.getTag(R.id.value_update_animator) as ValueAnimator.AnimatorUpdateListener
        valueAnimator.removeUpdateListener(selectUpdateListener)

        source.itemView.rotation = 0F
    }

    override fun onItemClear(source: RecyclerView.ViewHolder) {
        //开始动画
        valueAnimator.addUpdateListener(selectUpdateListener)
        //获取结束的位置
        endPosition = source.adapterPosition

        //位置不变则结束方法
        if (endPosition == startPosition) {
            return
        }
        val list = arrayListOf<RecordType>()
        //更新orderIndex目标item的位置
        //修改沿线的item的orderIndex
        if (startPosition < endPosition) {//向后移动

            for (i in endPosition downTo startPosition) {
                val item = getItem(i)
                if (i != endPosition) {
                    item.orderIndex -= 1
                } else {
                    item.orderIndex = getItem(i - 1).orderIndex
                }
                item.mTime = TimeUtils.timeOfSecond
                list.add(item)
            }
        } else {//向前移动
            for (i in endPosition..startPosition) {
                val item = getItem(i)
                if (i != endPosition) {
                    item.orderIndex += 1
                } else {
                    item.orderIndex = getItem(i + 1).orderIndex
                }
                item.mTime = TimeUtils.timeOfSecond
                list.add(item)
            }
        }
        //更新数据库
        RecordTypeTool.updateOrderIndex(list)

    }
}