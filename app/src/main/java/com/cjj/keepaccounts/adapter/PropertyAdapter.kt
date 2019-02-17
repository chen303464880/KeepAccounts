package com.cjj.keepaccounts.adapter

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.text.inSpans
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.account.AccountDetailsActivity
import com.cjj.keepaccounts.activity.account.BorrowMoneyRecordActivity
import com.cjj.keepaccounts.activity.account.SelectAccountTypeActivity
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.bean.Account
import com.cjj.keepaccounts.bean.ActivityInfoBean
import com.cjj.keepaccounts.dao.AccountDaoTool
import com.cjj.keepaccounts.enum.ListViewType
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.LogoManager
import com.cjj.keepaccounts.pop.PropertyCheckPop
import com.cjj.keepaccounts.utils.*
import com.cjj.keepaccounts.view.drawableStart
import java.util.*


/**
 * @author CJJ
 * Created by CJJ on 2017/11/23 14:51.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 */
class PropertyAdapter(private val accountList: List<Account>, val possess: Boolean) : ItemTouchHelperAdapter, BaseRecyclerViewAdapter<BaseViewHolder, Account>() {

    private var popWindow: PropertyCheckPop? = null
    private val sizeSpan = AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12))
    private val colorSpan = ForegroundColorSpan(Color.parseColor("#CCFFFFFF"))

    private val zoomInAnimations: Animation by lazy(LazyThreadSafetyMode.NONE) {
        val animationSet = AnimationSet(false)
        val scaleAnimation: Animation = ScaleAnimation(1F, 1.1F, 1F, 1.1F,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        val alphaAnimation: Animation = AlphaAnimation(1F, 0.8F)
        animationSet.addAnimation(scaleAnimation)
        animationSet.addAnimation(alphaAnimation)
        animationSet.fillAfter = true
        animationSet.duration = 100
        animationSet
    }

    private val zoomOutAnimations: Animation by lazy(LazyThreadSafetyMode.NONE) {
        val animationSet = AnimationSet(false)
        val scaleAnimation: Animation = ScaleAnimation(1.1F, 1F, 1.1F, 1F
                , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        val alphaAnimation: Animation = AlphaAnimation(0.8F, 1.0F)
        animationSet.addAnimation(scaleAnimation)
        animationSet.addAnimation(alphaAnimation)
        animationSet.fillAfter = true
        animationSet.duration = 100
        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                clearItemView.clearAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        animationSet
    }


    class HeadViewHolder(itemView: View) : BaseViewHolder(itemView) {

        /**
         * 资产或负债
         */
        @BindView(R.id.tv_property)
        lateinit var tvProperty: TextView
        /**
         * 所有资产
         */
        @BindView(R.id.tv_money)
        lateinit var tvMoney: TextView
        /**
         * 净资产
         */
        @BindView(R.id.tv_possess_money)
        lateinit var tvPossessMoney: TextView

    }

    class ContentViewHolder(itemView: View) : BaseViewHolder(itemView) {
        /**
         * 账户名字和描述
         */
        @BindView(R.id.iv_account_name)
        lateinit var ivAccountName: TextView
        /**
         * 账户资产
         */
        @BindView(R.id.tv_money)
        lateinit var tvMoney: TextView
        /**
         * 父容器,用于添加点击事件
         */
        @BindView(R.id.fl_content)
        lateinit var flContent: FrameLayout

    }

    class FooterViewHolder(itemView: View) : BaseViewHolder(itemView) {
        @BindView(R.id.tv_add_account)
        lateinit var tvAddAccount: TextView
    }

    override fun getItemViewType(position: Int): Int =
    //根据不同的position返回不同的item
            when (position) {
                0 -> ListViewType.TYPE_HEAD.type
                itemCount - 1 -> ListViewType.TYPE_FOOTER.type
                else -> ListViewType.TYPE_CONTENT.type
            }

    override fun insertData(data: Account) {
        this.data.add(data)
        notifyItemInserted(this.data.size)
    }

    override fun removeData(index: Int) {
        if (index < this.data.size) {
            this.data.removeAt(index)
            notifyItemRemoved(index + 1)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
            when (viewType) {
                ListViewType.TYPE_HEAD.type -> {//头布局
                    val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_propety_head)

                    HeadViewHolder(view)
                }
                ListViewType.TYPE_FOOTER.type -> {//脚布局
                    val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_propety_footer)
                    FooterViewHolder(view)
                }
                else -> {
                    val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_propety_content)
                    ContentViewHolder(view)
                }
            }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

        when (holder) {
            //内容出现的次数最多,优先判断
            is ContentViewHolder -> {
                //设置内容布局的数据
                val account = getItem(position - 1)
                val logo = getLogo(LogoManager.getHomeLogo(account.accountType.idImg))
                holder.ivAccountName.drawableStart = logo


                val stringSpan = SpannableStringBuilder(account.name)

                stringSpan.inSpans(sizeSpan, colorSpan) {
                    append("\n")
                    //添加账户描述
                    append(if (account.accountType.desc.isNullOrEmpty()) {
                        if (possess) {
                            Utils.context.getString(R.string.xxx_balance, account.accountType.name)
                        } else {//负债账户显示剩余额度
                            Utils.context.getString(R.string.balance_xxx, (account.creditLimit + account.money))
                        }
                    } else {
                        account.accountType.desc
                    })
                }
                //设置账户名
                holder.ivAccountName.text = stringSpan

                //显示
                holder.tvMoney.text = (if (possess) account.money else -account.money + 0.0).toMoney()

                //设置item背景
                holder.flContent.background = getContentBg(account.color)

                //打开详情页面
                holder.flContent.setOnClickListener {
                    when (account.accountType.indexNum) {
                        12, 13 -> {
                            val color = Color.parseColor(account.color)
                            val titleInfo = ActivityInfoBean(Utils.getString(R.string.all), Utils.getString(R.string.property), color, Utils.getString(R.string.settings))
                            ActivityTool.skipActivity<BorrowMoneyRecordActivity>(account, titleInfo)
                        }
                        else -> {
                            val colorStr = account.color
                            val color = Color.parseColor(if (colorStr.length == 6) "#ff$colorStr" else colorStr)
                            val activityInfo = ActivityInfoBean(account.name, Utils.getString(R.string.property), color, Utils.getString(R.string.settings))
                            ActivityTool.skipActivity<AccountDetailsActivity>(account, activityInfo)
                        }

                    }
                }

            }
            is HeadViewHolder -> {
                var money = 0.0
                var possessMoney = 0.0
                if (possess) {//计算所有的资产,包括负债
                    accountList.asSequence().filter { it.isShow == 0 }.forEach {
                        possessMoney += it.money
                    }
                }
                data.filter { it.isShow == 0 }.forEach {
                    //计算负债或者净资产
                    money += when (it.accountType.indexNum) {
                        3, 10, 11, 13 -> -it.money
                        else -> it.money
                    }
                }
                //设置净资产或者负债
                holder.tvMoney.text = MoneyUtils.formatMoney(money)
                if (possess) {
                    holder.tvPossessMoney.text = Utils.context.getString(R.string.possess_money, possessMoney)
                } else {
                    holder.tvProperty.text = Utils.getString(R.string.liabilities)
                    //显示负债账户时,隐藏净资产
                    holder.tvPossessMoney.visibility = View.GONE
                }

                if (data.isNotEmpty()) {//
                    holder.tvProperty.setOnClickListener {
                        //选择是否显示的账户
                        if (popWindow == null) {
                            popWindow = PropertyCheckPop(holder.tvProperty.context, this)
                        }
                        popWindow!!.showAsDropDown(holder.tvProperty)
                    }
                } else {
                    holder.tvProperty.setCompoundDrawables(null, null, null, null)
                }
                popWindow?.notifyDataSetChanged()

            }
            is FooterViewHolder -> {
                holder.tvAddAccount.setOnClickListener {
                    ActivityTool.skipActivity<SelectAccountTypeActivity>()
                }

            }
        }
    }

    private val orderMap: HashMap<Long, Account> by lazy(LazyThreadSafetyMode.NONE) {
        hashMapOf<Long, Account>()
    }

    override fun getFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return if (viewHolder.adapterPosition == 0 || viewHolder.adapterPosition == itemCount - 1) {
            0
        } else {
            ItemTouchHelper.UP or ItemTouchHelper.DOWN
        }
    }

    override fun onItemMove(source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) {
        val fromPosition = source.adapterPosition
        val toPosition = target.adapterPosition
        if (toPosition == 0) {
            return
        }
        val dataFromPosition = fromPosition - 1
        val dataToPosition = toPosition - 1
        val start = 1
        val end = data.size
        if (fromPosition in start..end && toPosition in start..end) {

            val temp = data[dataToPosition].orderIndex
            data[dataToPosition].orderIndex = data[dataFromPosition].orderIndex
            data[dataFromPosition].orderIndex = temp
            orderMap[data[dataToPosition].uuid] = data[dataToPosition]
            orderMap[data[dataFromPosition].uuid] = data[dataFromPosition]
//            AccountDaoTool.update(data[dataFromPosition], fromClone, false)
//            AccountDaoTool.update(data[dataToPosition], toClone, false)
            //交换数据位置
            Collections.swap(data, dataFromPosition, dataToPosition)
            //刷新位置交换
            notifyItemMoved(fromPosition, toPosition)

        }

    }

    override fun onItemDissmiss(source: RecyclerView.ViewHolder) {

    }

    private lateinit var clearItemView: View
    override fun onItemSelect(source: RecyclerView.ViewHolder) {

        val itemView = source.itemView
        itemView.startAnimation(zoomInAnimations)
    }

    override fun onItemClear(source: RecyclerView.ViewHolder) {
        clearItemView = source.itemView
        clearItemView.startAnimation(zoomOutAnimations)

        val list = arrayListOf<Account>()
        orderMap.forEach { (_, u) ->
            u.mTime = TimeUtils.timeOfSecond
            list.add(u)
        }
        AccountDaoTool.updateOrderIndex(list)

    }

    private fun getLogo(idImg: Int): Drawable {
        val drawable = Utils.getDrawable(idImg) as BitmapDrawable
        val drawableBg = Utils.getDrawable(R.drawable.shape_property_content_logo_bg)
        drawable.setBounds(0, 0, Utils.dip2px(35), Utils.dip2px(35))
        drawable.gravity = Gravity.CENTER
//        drawable.
//        drawable.layoutDirection
        drawableBg.setBounds(0, 0, Utils.dip2px(36), Utils.dip2px(36))
        val layerDrawable = LayerDrawable(arrayOf(drawableBg, drawable))

        layerDrawable.setBounds(0, 0, Utils.dip2px(36), Utils.dip2px(36))
        return layerDrawable
    }


    /**
     * 根据颜色动态生成内容布局的背景
     */
    private fun getContentBg(color: String): Drawable {

        val drawable1 = GradientDrawable()
        drawable1.setColor(Color.parseColor(if (color.length == 6) "#ff$color" else color))
        val radii = Utils.getDimension(R.dimen.radius).toFloat()
        drawable1.cornerRadius = radii

        val drawable2 = GradientDrawable()
        drawable2.setColor(Color.WHITE)
        drawable2.cornerRadii = floatArrayOf(0F, 0F, radii, radii, radii, radii, 0F, 0F)
        drawable2.alpha = 0x33

        val drawable = LayerDrawable(arrayOf(drawable1, drawable2))
        drawable.setLayerInset(1, Utils.dip2px(8F), 0, 0, 0)
        return drawable
    }
}