package com.cjj.keepaccounts.activity.bill

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.AnnualRecordAdapter
import com.cjj.keepaccounts.base.TransparencyActivity
import com.cjj.keepaccounts.mvp.activity.bill.annualrecord.CAnnualRecord
import com.cjj.keepaccounts.mvp.activity.bill.annualrecord.PAnnualRecord
import com.cjj.keepaccounts.utils.Utils
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/26 10:41.
 * 全年的记录的浏览视图
 */
class AnnualRecordActivity : TransparencyActivity<PAnnualRecord>(), CAnnualRecord.View {
    private lateinit var rvAnnualRecord: RecyclerView
    override val adapter: AnnualRecordAdapter = AnnualRecordAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWhiteStatusBar(true)
        rvAnnualRecord = recyclerView {
            backgroundColorResource = R.color.white
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setPadding(Utils.getDimension(R.dimen.start), 0, 0, 0)
        }
        initView()

        initListener()
    }

    private fun initListener() {
        adapter.setOnItemClickListener { _, _, itemInfo ->
            EventBus.getDefault().post(itemInfo)
            finish()
        }
    }


    private fun initView() {

        //将数据设置给recyclerView
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = if (adapter.getItem(position).isNode) 3 else 1
        }
        rvAnnualRecord.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                val adapterPosition = parent.getChildAdapterPosition(view)
                if (adapterPosition % 13 == 0) {
                    outRect.set(0, Utils.dip2px(8F), 0, Utils.dip2px(8F))
                } else {
                    outRect.set(0, Utils.dip2px(6F), Utils.getDimension(R.dimen.end), Utils.dip2px(6F))
                }
            }
        })
        rvAnnualRecord.layoutManager = gridLayoutManager
        rvAnnualRecord.adapter = adapter

        presenter.presenter()

    }
}