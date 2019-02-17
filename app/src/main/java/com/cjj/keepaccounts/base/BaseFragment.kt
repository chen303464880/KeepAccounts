package com.cjj.keepaccounts.base

import android.content.Context
import android.support.v4.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.cjj.keepaccounts.utils.Utils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * @author CJJ
 * Created by CJJ on 2017/11/13 13:40.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 */
abstract class BaseFragment<P : BasePresenter<out IView, out IModel>> : Fragment() {

    @Inject
    lateinit var presenter: P

    private lateinit var butterKnife: Unbinder

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        lifecycle.addObserver(presenter)
    }


    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        butterKnife = ButterKnife.bind(this, view)
        initView()
        initListener()
    }


    protected abstract fun initView()

    protected open fun initListener() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        //解除绑定
        butterKnife.unbind()
        Utils.fixInputMethodManagerLeak(context!!)
    }
}