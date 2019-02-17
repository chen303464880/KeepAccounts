package com.cjj.keepaccounts.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.bean.Account
import com.cjj.keepaccounts.utils.Utils

/**
 * @author chenjunjie
 * Created by CJJ on 2017/11/27 14:51.
 */
class PropertyCheckAdapter(private val adapter: PropertyAdapter) : RecyclerView.Adapter<PropertyCheckAdapter.ContentViewHolder>() {

    override fun getItemCount(): Int = adapter.data.size

    private fun getItem(position: Int): Account = adapter.data[position]


    class ContentViewHolder(itemView: View) : BaseViewHolder(itemView) {
        @BindView(R.id.cb_property_check)
        lateinit var cbPropertyCheck: CheckBox
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_propety_check_content)
        return ContentViewHolder(view)
    }


    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val item = getItem(position)
        holder.cbPropertyCheck.setOnCheckedChangeListener(null)
        holder.cbPropertyCheck.text = item.name
        holder.cbPropertyCheck.isChecked = item.isShow == 0
        holder.cbPropertyCheck.setOnCheckedChangeListener { _, isChecked ->
            item.isShow = if (isChecked) 0 else 1
            item.update()
            adapter.notifyDataSetChanged()
        }
    }
}