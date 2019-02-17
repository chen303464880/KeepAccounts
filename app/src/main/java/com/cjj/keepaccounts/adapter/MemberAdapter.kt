package com.cjj.keepaccounts.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseDeleteAdapter
import com.cjj.keepaccounts.bean.RecordTag
import com.cjj.keepaccounts.utils.Utils

/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/2 14:12.
 */
class MemberAdapter : BaseDeleteAdapter<MemberAdapter.ViewHolder, RecordTag>() {


    class ViewHolder(itemView: View) : DeleteViewHolder(itemView) {
        @BindView(R.id.tv_name)
        lateinit var tvName: TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_member)
        return ViewHolder(inflater)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val recordTag = getItem(position)
        holder.tvName.text = recordTag.tagName
    }
}