package com.cjj.keepaccounts.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.databinding.typeLogo
import com.cjj.keepaccounts.base.BaseDeleteAdapter
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.bean.RecordDao
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.Utils
import org.jetbrains.anko.textColorResource

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/21 11:48.
 */
class RecordAdapter : BaseDeleteAdapter<RecordAdapter.ViewHolder, Record>() {


    class ViewHolder(itemView: View) : DeleteViewHolder(itemView) {
        @BindView(R.id.fl_content)
        lateinit var flContent: FrameLayout
        @BindView(R.id.tv_type)
        lateinit var tvType: TextView
        @BindView(R.id.tv_money)
        lateinit var tvMoney: TextView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_record)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val record = getItem(position)

        holder.tvType.typeLogo(record.recordType.imgSrcId, Utils.dip2px(30))

        val typeDesc = record.recordType.typeDesc
        val desc = if (record.typeId != -1L) {
            record.content
        } else {
            val first = DaoManager.getRecordDao().queryBuilder()
                    .where(RecordDao.Properties.ActionId.eq(record.actionId))
                    .where(RecordDao.Properties.AccountId.notEq(record.accountId))
                    .list().first()
            first.__setDaoSession(DaoManager.daoSession)
            if (record.rateMoney > 0) {
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
        val isIncoming = record.recordType.isIncoming
        if (isIncoming == 1 || (isIncoming == -1 && record.rateMoney > 0)) {
            holder.tvMoney.textColorResource = R.color.AppThemeColor
            val str = "+" + Utils.context.getString(R.string.format_money, record.rateMoney)
            holder.tvMoney.text = str
        } else {
            holder.tvMoney.textColorResource = R.color.text_color_655f5f
            val str = "-" + Utils.context.getString(R.string.format_money, Math.abs(record.rateMoney))
            holder.tvMoney.text = str
        }
    }
}