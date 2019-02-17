package com.cjj.keepaccounts.view

import android.support.v4.view.ViewPager
import android.widget.RadioButton
import android.widget.RadioGroup

/**
 * @author CJJ
 * Created by CJJ on 2017/11/13 14:36.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 */
fun ViewPager.setRadioGroup(rg: RadioGroup) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            rg.check(rg.getChildAt(position).id)
        }
    })
    rg.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
        currentItem = radioGroup.indexOfChild(radioGroup.findViewById<RadioButton>(i))
    }
}