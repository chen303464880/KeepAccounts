package com.cjj.keepaccounts.activity.bill

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.cjj.keepaccounts.adapter.CalendarSelectAdapter
import com.cjj.keepaccounts.base.TransparencyActivity
import com.cjj.keepaccounts.mvp.activity.bill.calendarselect.CCalendarSelect
import com.cjj.keepaccounts.mvp.activity.bill.calendarselect.PCalendarSelect
import com.cjj.keepaccounts.utils.ActivityAnimationUtil
import com.cjj.keepaccounts.view.clearItemAnimator
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/22 17:02.
 */
class CalendarSelectActivity : TransparencyActivity<PCalendarSelect>(), CCalendarSelect.View {


    override val adapter = CalendarSelectAdapter()
    private val gridLayoutManager: GridLayoutManager by lazy {
        val gl = GridLayoutManager(this, 7)
        gl.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = if (adapter.getItem(position).isNode) 7 else 1
        }
        gl
    }
    private lateinit var rvCalendar: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWhiteStatusBar(true)
        rvCalendar = recyclerView {
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            layoutManager = gridLayoutManager
            clearItemAnimator()
            adapter = this@CalendarSelectActivity.adapter
        }
        initListener()
    }

    private fun initListener() {
        adapter.setCalendarChangeListener { dayBean, view ->
            EventBus.getDefault().post(dayBean)
            ActivityAnimationUtil.circularClose(this, view)
        }
    }

    override fun setPosition(position: Int) {
        rvCalendar.scrollToPosition(if (position != -1) position else adapter.itemCount - 1)
    }

}