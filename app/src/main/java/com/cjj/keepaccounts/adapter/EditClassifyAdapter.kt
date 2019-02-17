package com.cjj.keepaccounts.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.databinding.typeLogo
import com.cjj.keepaccounts.base.BaseDeleteAdapter
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.utils.Utils

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/8 15:12.
 */
class EditClassifyAdapter : BaseDeleteAdapter<EditClassifyAdapter.ViewHolder, RecordType>() {

    val diameter = Utils.dip2px(25F)

    class ViewHolder(itemView: View) : DeleteViewHolder(itemView) {
        @BindView(R.id.tv_classify)
        lateinit var tvClassify: TextView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_edit_classify)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val recordType = getItem(position)
        holder.tvClassify.text = recordType.typeDesc
        holder.tvClassify.typeLogo(recordType.imgSrcId, diameter)
    }
}