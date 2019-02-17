package com.cjj.keepaccounts.base.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.ButterKnife

/**
 * @author CJJ
 * Created by CJJ on 2017/11/23 16:15.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 */
open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    init {
        @Suppress("LeakingThis")
        ButterKnife.bind(this, itemView)
    }
}