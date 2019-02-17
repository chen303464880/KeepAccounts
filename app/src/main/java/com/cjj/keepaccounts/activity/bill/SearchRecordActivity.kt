package com.cjj.keepaccounts.activity.bill

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.account.BillDetailsActivity
import com.cjj.keepaccounts.adapter.SearchRecordAdapter
import com.cjj.keepaccounts.base.TransparencyActivity
import com.cjj.keepaccounts.bean.ActivityInfoBean
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.mvp.activity.bill.searchrecord.CSearchRecord
import com.cjj.keepaccounts.mvp.activity.bill.searchrecord.PSearchRecord
import com.cjj.keepaccounts.utils.RecyclerSpace
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.addAfterTextChangedListener
import com.cjj.keepaccounts.view.drawableEnd
import kotlinx.android.synthetic.main.activity_search_record.*
import org.jetbrains.anko.*


/**
 * @author CJJ
 * Created by CJJ on 2018/7/2 10:09.
 */
class SearchRecordActivity : TransparencyActivity<PSearchRecord>(), CSearchRecord.View {

    private lateinit var adapter: SearchRecordAdapter
    private val headView: TextView by lazy(LazyThreadSafetyMode.NONE) {
        val textView = TextView(this)
        textView.horizontalPadding = Utils.getDimension(R.dimen.start)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.textSizeDimen = R.dimen.text_size12
        textView.textColorResource = R.color.text_color_655f5f
        textView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, dip(35))
        textView
    }

    private var _tvSearchBlank: TextView? = null
    private val tvSearchBlank: TextView
        get() {
            if (_tvSearchBlank == null) {
                _tvSearchBlank = vs_blank.inflate() as TextView
            }
            return _tvSearchBlank!!
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWhiteStatusBar(true)
        setContentView(R.layout.activity_search_record)
        ll_record.layoutParams.height = Utils.getTitleHeight() + Utils.getDimension(R.dimen.title_height)
        ll_record.topPadding = Utils.getTitleHeight()
        adapter = SearchRecordAdapter()
        adapter.addHeaderView(headView)
        rv_search_record.adapter = adapter
        rv_search_record.layoutManager = LinearLayoutManager(this)
        rv_search_record.addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e7)))
        initListener()
    }

    private fun initListener() {
        et_search.addAfterTextChangedListener {
            if (et_search.text.isNotEmpty()) {
                presenter.search(et_search.text.toString())
            } else {
                adapter.clear()
                rv_search_record.visibility = View.GONE
                if (_tvSearchBlank != null) {
                    tvSearchBlank.visibility = View.GONE
                }
            }
        }

        et_search.setOnTouchListener { _, event ->
            //当按到清除文字图标时,清空密码框的字符
            if (et_search.text.isNotEmpty() && event.action == MotionEvent.ACTION_UP) {
                val x = event.x
                val y = event.y
                val drawable = et_search.drawableEnd!!
                if (x >= et_search.width - et_search.totalPaddingRight
                        && x <= et_search.width - et_search.paddingRight
                        && y >= et_search.height / 2 - drawable.minimumHeight / 2
                        && y <= et_search.height / 2 + drawable.minimumHeight / 2) {
                    et_search.setText("")
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

        adapter.setOnItemClickListener { _, _, itemInfo ->
            val activityInfo = ActivityInfoBean(Utils.getString(R.string.details), Utils.getString(R.string.search_result), itemInfo.recordType.color)
            ActivityTool.skipActivity<BillDetailsActivity>(itemInfo, activityInfo)
        }
        tv_cancel.setOnClickListener {
            finish()
        }
    }


    override fun setMoneyInfo(moneyInfo: String) {
        headView.text = moneyInfo
    }

    override fun setRecordDate(date: List<Record>) {
        if (date.isNotEmpty()) {
            if (_tvSearchBlank != null) {
                tvSearchBlank.visibility = View.GONE
            }
            adapter.setData(date)
            rv_search_record.visibility = View.VISIBLE
        } else {
            rv_search_record.visibility = View.GONE
            tvSearchBlank.visibility = View.VISIBLE
        }
    }

    override fun updateData(index: Int, entity: Record) {
        adapter.updateData(index, entity)
    }

    override fun removeData(index: Int) {
        adapter.removeData(index)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}