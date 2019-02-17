package com.cjj.keepaccounts.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.base.viewholder.ClickViewHolder
import com.cjj.keepaccounts.bean.LogoInfo
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.rippleBorderless

/**
 * Created by CJJ on 2018/3/11 20:46.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class ClassifyLogoAdapter : BaseRecyclerViewAdapter<ClassifyLogoAdapter.ViewHolder, LogoInfo>() {
    private val requestOptions = RequestOptions().override(Utils.dip2px(36F))

    class ViewHolder(itemView: ImageView) : BaseViewHolder(itemView), ClickViewHolder {
        val view = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassifyLogoAdapter.ViewHolder {
        val view = ImageView(parent.context)
        view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Utils.dip2px(70F))
        view.scaleType = ImageView.ScaleType.CENTER
        view.rippleBorderless()
        return ClassifyLogoAdapter.ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ClassifyLogoAdapter.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        Glide.with(holder.view)
                .load(getItem(position).imgRes)
                .apply(requestOptions)
                .into(holder.view)
    }
}