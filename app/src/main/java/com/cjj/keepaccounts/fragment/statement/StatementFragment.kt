package com.cjj.keepaccounts.fragment.statement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.ViewPagerAdapter
import com.cjj.keepaccounts.base.BaseFragment
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.view.setRadioGroup
import kotlinx.android.synthetic.main.fragment_statement.*


/**
 * @author CJJ
 * Created by CJJ on 2017/11/10 16:48.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 * 显示报表的页面
 */
class StatementFragment : BaseFragment<EmptyPresenter>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_statement, container, false)
    }

    override fun initView() {
        val classifyFragment = ClassifyFragment()
        val trendFragment = TrendFragment()
        val contrastFragment = ContrastFragment()
        val memberFragment = MemberFragment()
        val fragmentList = arrayListOf(classifyFragment, trendFragment, contrastFragment, memberFragment)
        vp_statement.offscreenPageLimit = 3
        vp_statement.adapter = ViewPagerAdapter(activity!!.supportFragmentManager, fragmentList)
        vp_statement.setRadioGroup(rg_statement)
    }

}