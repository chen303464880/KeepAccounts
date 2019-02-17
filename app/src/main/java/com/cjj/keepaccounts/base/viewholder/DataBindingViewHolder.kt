package com.cjj.keepaccounts.base.viewholder

import android.databinding.ViewDataBinding

/**
 * @author CJJ
 * Created by CJJ on 2018/11/12 15:08.
 */
open class DataBindingViewHolder<T : ViewDataBinding>(val dataBinding: T) : BaseViewHolder(dataBinding.root)