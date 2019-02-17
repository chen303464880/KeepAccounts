package com.cjj.keepaccounts.adapter

import android.view.ViewGroup
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.viewholder.ClickViewHolder
import com.cjj.keepaccounts.base.viewholder.DataBindingViewHolder
import com.cjj.keepaccounts.bean.StatementDetailBean
import com.cjj.keepaccounts.databinding.ListItemBillClassifyBinding
import com.cjj.keepaccounts.utils.Utils

/**
 * @author CJJ
 * Created by CJJ on 2018/5/14 14:39.
 */
class BillClassifyAdapter : HeaderAndFooterAdapter<BillClassifyAdapter.ViewHolder, StatementDetailBean>() {

    class ViewHolder(dataBinding: ListItemBillClassifyBinding) : DataBindingViewHolder<ListItemBillClassifyBinding>(dataBinding), ClickViewHolder

    override fun onCreateContentViewHolder(parent: ViewGroup, viewType: Int): BillClassifyAdapter.ViewHolder {
        return ViewHolder(Utils.inflateDataBindingItem(parent, R.layout.list_item_bill_classify))
    }

    override fun onBindContentViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.dataBinding.statementDetail = getItem(position)
    }
}