package com.cjj.keepaccounts.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.enum.ListViewType
import com.cjj.keepaccounts.manager.LogoManager
import com.cjj.keepaccounts.utils.*
import org.jetbrains.anko.imageResource
import rx.Observable
import java.util.concurrent.TimeUnit


/**
 * @author chenjunjie
 * Created by CJJ on 2018/2/27 15:17.
 */
class BillRecordAdapter(context: Context) : BaseRecyclerViewAdapter<BaseViewHolder, Record>() {

    private var rlEdit: RelativeLayout? = null

    /**
     * 是否正在播放退出动画,播放时不可以做其他操作
     */
    private var isAnimation = false

    /**
     * 删除标记，动画播放完毕后在执行删除操作
     */
    private var isDeleteFlag = false
    private var deleteRecord: Record? = null

    private val viewWidth = Utils.dip2px(25)

    private var deleteListener: ((item: Record) -> Unit)? = null

    fun setOnDeleteListener(listener: (item: Record) -> Unit) {
        this.deleteListener = listener
    }

    private var editListener: ((holder: ViewHolder, position: Int, item: Record) -> Unit)? = null

    fun setOnEditListener(listener: (holder: ViewHolder, position: Int, item: Record) -> Unit) {
        this.editListener = listener
    }

    private val deleteView: ImageView by lazy(LazyThreadSafetyMode.NONE) {
        val imageView = ImageView(context)
        val layoutParams = RelativeLayout.LayoutParams(viewWidth, viewWidth)
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
        layoutParams.marginStart = Utils.getDimension(R.dimen.start)
        imageView.layoutParams = layoutParams
        imageView.imageResource = R.mipmap.bill_list_icon_shanchu
        imageView
    }

    private val editView: ImageView by lazy(LazyThreadSafetyMode.NONE) {
        val imageView = ImageView(context)
        val layoutParams = RelativeLayout.LayoutParams(viewWidth, viewWidth)
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
        layoutParams.marginEnd = Utils.getDimension(R.dimen.end)
        imageView.layoutParams = layoutParams
        imageView.imageResource = R.mipmap.bill_list_icon_bianji
        imageView
    }

    private val deleteAnimation: AnimationSet by lazy(LazyThreadSafetyMode.NONE) {
        getInAnimation(Utils.widthPixels.toFloat() / 2 - Utils.dip2px(13) - Utils.getDimension(R.dimen.start))
    }

    private val editAnimation: AnimationSet by lazy(LazyThreadSafetyMode.NONE) {
        getInAnimation(Utils.widthPixels.toFloat() / 2 - Utils.dip2px(13) - (Utils.widthPixels - viewWidth - Utils.getDimension(R.dimen.end)))
    }


    class ViewHolder(val view: View) : BaseViewHolder(view) {
        @BindView(R.id.rl_content)
        lateinit var rlContent: RelativeLayout
        @BindView(R.id.iv_logo)
        lateinit var ivLogo: ImageView
        @BindView(R.id.tv_name)
        lateinit var tvDesc: TextView
        @BindView(R.id.tv_content)
        lateinit var tvContent: TextView
    }

    class NodeViewHolder(val view: View) : BaseViewHolder(view) {
        @BindView(R.id.tv_day)
        lateinit var tvDay: TextView
        @BindView(R.id.tv_income)
        lateinit var tvIncome: TextView
        @BindView(R.id.tv_expend)
        lateinit var tvExpend: TextView
    }

    override fun getItemViewType(position: Int): Int =
            if (getItem(position).isNode) {
                ListViewType.TYPE_INTERVAL.type
            } else {
                ListViewType.TYPE_CONTENT.type
            }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val isNode = viewType == ListViewType.TYPE_INTERVAL.type
        val view = Utils.inflateRecyclerViewItem(parent, if (!isNode) R.layout.list_item_bill_record else R.layout.list_item_bill_record_node)
        return if (!isNode) {
            ViewHolder(view)
        } else {
            NodeViewHolder(view)
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ViewHolder -> {

                val type = item.recordType
                val name: String

                holder.tvContent.text = item.content
                val nameLayoutParams = holder.tvDesc.layoutParams as RelativeLayout.LayoutParams
                val contentLayoutParams = holder.tvContent.layoutParams as RelativeLayout.LayoutParams
                if (type.isIncoming == 0) {
                    //支出
                    name = "${type.typeDesc} ${MoneyUtils.formatMoney(item.rateMoney)}"
                    nameLayoutParams.removeRule(RelativeLayout.LEFT_OF)
                    nameLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.iv_logo)
                    contentLayoutParams.removeRule(RelativeLayout.LEFT_OF)
                    contentLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.iv_logo)

                    //收入
                } else {
                    name = "${MoneyUtils.formatMoney(item.rateMoney)} ${type.typeDesc}"
                    nameLayoutParams.removeRule(RelativeLayout.RIGHT_OF)
                    nameLayoutParams.addRule(RelativeLayout.LEFT_OF, R.id.iv_logo)
                    contentLayoutParams.removeRule(RelativeLayout.RIGHT_OF)
                    contentLayoutParams.addRule(RelativeLayout.LEFT_OF, R.id.iv_logo)
                }


                holder.tvDesc.text = name
                holder.tvDesc.requestLayout()
                holder.tvContent.requestLayout()
                holder.ivLogo.setImageResource(LogoManager.getTypeLogo(item.recordType.imgSrcId))
                holder.view.setOnClickListener {
                    if (isEdit()) {
                        cancelEdit()
                    } else {
                        deleteView.setOnClickListener {
                            if (!isAnimation) {
                                cancelEdit()
                                isDeleteFlag = true
                                deleteRecord = item
                            }
                        }
                        editView.setOnClickListener {
                            if (!isAnimation) {
                                cancelEdit()
                                editListener?.invoke(holder, position, item)
                            }
                        }
                        holder.rlContent.addView(deleteView, 0)
                        holder.rlContent.addView(editView, 0)
                        deleteAnimation.interpolator = OvershootInterpolator()
                        editAnimation.interpolator = OvershootInterpolator()
                        deleteView.startAnimation(deleteAnimation)
                        editView.startAnimation(editAnimation)
                        rlEdit = holder.rlContent
                    }
                }
            }
            is NodeViewHolder -> {
                if (item.day == 0) {
                    LogUtils.i(item.toString())
                }
                holder.tvDay.text = Utils.context.getString(R.string.xx_day, item.day)
                if (item.income != 0.0) {
                    holder.tvIncome.text = Utils.context.getString(R.string.xx_income, item.income)
                } else {
                    holder.tvIncome.text = ""
                }
                if (item.expend != 0.0) {
                    holder.tvExpend.text = Utils.context.getString(R.string.expend_xx, item.expend)
                } else {
                    holder.tvExpend.text = ""
                }
                holder.itemView.setOnClickListener {
                    if (isEdit()) {
                        cancelEdit()
                    }
                }
            }
        }
    }

    private fun getInAnimation(x: Float): AnimationSet {
        val animationSet = AnimationSet(true)
        //参数1：从哪个旋转角度开始
        //参数2：转到什么角度
        //后4个参数用于设置围绕着旋转的圆的圆心在哪里
        //参数3：确定x轴坐标的类型，有ABSOLUT绝对坐标、RELATIVE_TO_SELF相对于自身坐标、RELATIVE_TO_PARENT相对于父控件的坐标
        //参数4：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
        //参数5：确定y轴坐标的类型
        //参数6：y轴的值，0.5f表明是以自身这个控件的一半长度为x轴
        val rotateAnimation = RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)
        //参数1～2：x轴的开始位置
        //参数3～4：y轴的开始位置
        //参数5～6：x轴的结束位置
        //参数7～8：x轴的结束位置
        val translateAnimation = TranslateAnimation(
                Animation.ABSOLUTE, x,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f)
        animationSet.addAnimation(rotateAnimation)
        animationSet.addAnimation(translateAnimation)
        animationSet.fillAfter = true
        animationSet.duration = 500
        return animationSet
    }

    fun isEdit() = rlEdit != null

    fun cancelEdit() {
        if (rlEdit != null) {
            if (isAnimation) {
                isAnimation = false
                deleteAnimation.setAnimationListener(null)
                deleteView.clearAnimation()
                rlEdit!!.removeView(deleteView)
                editAnimation.setAnimationListener(null)
                editView.clearAnimation()
                rlEdit!!.removeView(editView)
                rlEdit = null
                if (isDeleteFlag) {
                    isDeleteFlag = false
                    deleteRecord = null
                }
            } else {
                isAnimation = true
                deleteAnimation.interpolator = ReverseOvershootInterpolator()
                editAnimation.interpolator = ReverseOvershootInterpolator()
                deleteView.startAnimation(deleteAnimation)
                editView.startAnimation(editAnimation)
                deleteAnimation.setAnimationListener(animationListener)
                editAnimation.setAnimationListener(animationListener)
            }
        }
    }

    private val animationListener = object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {

        }

        override fun onAnimationEnd(animation: Animation) {
            removeView()
        }

        override fun onAnimationStart(animation: Animation?) {

        }
    }

    var index = 0
    private fun removeView() {
        index += 1
        if (index == 2) {
            index = 0
            Observable.timer(50, TimeUnit.MILLISECONDS)
                    .observeOnMain()
                    .subscribe {
                        isAnimation = false
                        deleteAnimation.setAnimationListener(null)
                        deleteView.clearAnimation()
                        rlEdit!!.removeView(deleteView)
                        editAnimation.setAnimationListener(null)
                        editView.clearAnimation()
                        rlEdit!!.removeView(editView)
                        rlEdit = null
                        if (isDeleteFlag) {
                            isDeleteFlag = false
                            deleteListener?.invoke(deleteRecord!!)
                            deleteRecord = null
                        }
                    }
        }
    }
}