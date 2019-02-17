package com.cjj.keepaccounts.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * @author CJJ
 * Created by CJJ on 2017/11/10 16:53.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 */
class ViewPagerAdapter(fm: FragmentManager?, private val fragmentList: List<Fragment>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

}