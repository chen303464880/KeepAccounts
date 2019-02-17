package com.cjj.keepaccounts.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.databinding.typeLogo
import com.cjj.keepaccounts.base.BaseDeleteAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.base.viewholder.DataBindingViewHolder
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.bean.RecordDao
import com.cjj.keepaccounts.databinding.ListItemTextIntervalBinding
import com.cjj.keepaccounts.enum.ListViewType
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.MoneyUtils
import com.cjj.keepaccounts.utils.Utils
import org.jetbrains.anko.textColorResource

/**
 * Created by CJJ on 2018/5/14 21:26.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class ClassifyDetailsAdapter : BaseDeleteAdapter<BaseViewHolder, Record>() {

    class IntervalViewHolder(dataBinding: ListItemTextIntervalBinding) : DataBindingViewHolder<ListItemTextIntervalBinding>(dataBinding)

    class ClassifyDetailsViewHolder(itemView: View) : BaseDeleteAdapter.DeleteViewHolder(itemView) {
        @BindView(R.id.tv_type)
        lateinit var tvType: TextView
        @BindView(R.id.tv_money)
        lateinit var tvMoney: TextView
    }

    override fun getItemViewType(position: Int): Int = if (getItem(position).isNode) ListViewType.TYPE_INTERVAL.type else ListViewType.TYPE_CONTENT.type


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == ListViewType.TYPE_INTERVAL.type) {
            IntervalViewHolder(Utils.inflateDataBindingItem(parent, R.layout.list_item_text_interval))
        } else {
            val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_bill_classify_details)
            ClassifyDetailsViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        when (holder) {
            is IntervalViewHolder -> {
                holder.dataBinding.time = item.rTime
            }
            is ClassifyDetailsAdapter.ClassifyDetailsViewHolder -> {
                holder.tvType.typeLogo(item.recordType.imgSrcId, Utils.dip2px(30F))


                val typeDesc = item.recordType.typeDesc
                val desc = if (item.typeId != -1L) {
                    item.content
                } else {
                    val first = DaoManager.getRecordDao().queryBuilder()
                            .where(RecordDao.Properties.ActionId.eq(item.actionId))
                            .where(RecordDao.Properties.AccountId.notEq(item.accountId))
                            .unique()
                    first.__setDaoSession(DaoManager.daoSession)
                    if (item.rateMoney > 0) {
                        "[${first.account.name}转入]"
                    } else {
                        "[转出到${first.account.name}]"
                    }

                }
                //有描述的时候,添加描述
                if (!desc.isNullOrEmpty()) {
                    val spannableString = SpannableString("$typeDesc\n$desc")
                    spannableString.setSpan(ForegroundColorSpan(Utils.getColor(R.color.text_color_9e9b9b)), typeDesc.length + 1, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    spannableString.setSpan(AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12)), typeDesc.length + 1, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    holder.tvType.text = spannableString
                } else {
                    holder.tvType.text = typeDesc
                    holder.tvType.gravity = Gravity.CENTER_VERTICAL
                }
                val isIncoming = item.recordType.isIncoming
                if (isIncoming == 1 || (isIncoming == -1 && item.rateMoney > 0)) {
                    holder.tvMoney.textColorResource = R.color.AppThemeColor
                    val str = "+" + MoneyUtils.formatMoney(item.money)
                    holder.tvMoney.text = str
                } else {
                    holder.tvMoney.textColorResource = R.color.text_color_655f5f
                    val str = "-" + MoneyUtils.formatMoney(item.money)
                    holder.tvMoney.text = str
                }
            }
        }
    }

}