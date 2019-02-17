package com.cjj.keepaccounts.activity.account

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.BorrowMoneyRecordAdapter
import com.cjj.keepaccounts.base.TitleActivity
import com.cjj.keepaccounts.bean.ActivityInfoBean
import com.cjj.keepaccounts.bean.Credit
import com.cjj.keepaccounts.dao.CreditTool
import com.cjj.keepaccounts.dialog.MsgDialog
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.mvp.activity.account.borrowmoneyrecord.CBorrowMoneyRecord
import com.cjj.keepaccounts.mvp.activity.account.borrowmoneyrecord.PBorrowMoneyRecord
import com.cjj.keepaccounts.utils.RecyclerSpace
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.utils.toMoney
import kotlinx.android.synthetic.main.activity_borrow_money_record.*
import kotlinx.android.synthetic.main.view_black_page.*
import org.jetbrains.anko.backgroundColor


/**
 * @author CJJ
 * 显示借钱明细的页面
 */
class BorrowMoneyRecordActivity : TitleActivity<PBorrowMoneyRecord>(), CBorrowMoneyRecord.View<Credit> {


    override val adapter: BorrowMoneyRecordAdapter = BorrowMoneyRecordAdapter()

    private val deleteDialog: MsgDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = MsgDialog(this)
        dialog.setTitle(Utils.getString(R.string.delete_record_hint))
        dialog.setNegativeListener(null)
        dialog.message = Utils.getString(R.string.delete_record_affirm_hint)
        dialog
    }

    override fun getContentView(): View = Utils.inflate(R.layout.activity_borrow_money_record, this)

    private var isBorrow = true

    override fun initView() {

        tv_money.text = presenter.account.money.toMoney()
        isBorrow = presenter.account.accountType.indexNum == 12
        tv_desc.text = getString(if (isBorrow) R.string.loans_total else R.string.borrow_money_total)

        val linearLayoutManager = LinearLayoutManager(this)
        rv_borrow_money.addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e4e5e4)))
        rv_borrow_money.layoutManager = linearLayoutManager

        rv_borrow_money.adapter = adapter

        presenter.presenter()

    }

    override fun setAdapterData(data: List<Credit>) {
        if (data.isNotEmpty()) {
            adapter.setData(data)
            tv_interval.text = TimeUtils.longTurnTime(data[0].mTime, getString(R.string.yyyy_MM_dd_format))
        }
    }

    override fun setIntervalTime(time: Long) {
        tv_interval.text = TimeUtils.longTurnTime(time, getString(R.string.yyyy_MM_dd_format))
    }

    override fun setMoney(money: String) {
        tv_money.text = money
    }

    override fun setActivityTitleColor(color: Int) {
        super.setActivityTitleColor(color)
        tv_desc.backgroundColor = color
        tv_money.backgroundColor = color
    }


    var mSuspensionHeight = 0
    var mCurrentPosition = 0
    override fun initListener() {
        super.initListener()
        rv_borrow_money.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val layoutManager = rv_borrow_money.layoutManager as LinearLayoutManager
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (adapter.itemCount > mCurrentPosition + 1) {

                    if (adapter.getItem(mCurrentPosition + 1).isNode) {
                        val view = layoutManager.findViewByPosition(mCurrentPosition + 1)
                        if (view != null) {
                            if (view.top <= tv_interval.height) {
                                tv_interval.y = -(tv_interval.height - view.top).toFloat()
                            } else {
                                tv_interval.y = 0F
                            }
                        }
                    }


                    if (mCurrentPosition != layoutManager.findFirstVisibleItemPosition()) {
                        mCurrentPosition = layoutManager.findFirstVisibleItemPosition()
                        tv_interval.y = 0F
                        tv_interval.text = TimeUtils.longTurnTime(adapter.getItem(mCurrentPosition).mTime, "yyyy年MM月dd日")
                    }
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mSuspensionHeight = tv_interval.height
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && adapter.isOpen()) {
                    adapter.close()
                }
            }
        })
        tv_add.setOnClickListener {
            val activityInfoBean = ActivityInfoBean(getString(R.string.new_text), tv_desc.text, next = getString(R.string.ok))
            ActivityTool.skipActivity<AddBorrowMoneyActivity>(presenter.account, activityInfoBean)
        }
        adapter.setOnItemClickListener { _, _, itemInfo ->
            itemInfo.__setDaoSession(DaoManager.daoSession)
            val isBorrow = itemInfo.records.first().typeId != -106L
            val activityInfo = ActivityInfoBean(Utils.getString(if (isBorrow) R.string.loans else R.string.borrow),
                    Utils.getString(if (isBorrow) R.string.loans_total else R.string.borrow_money_total),
                    mPageColor,
                    next = Utils.getString(R.string.edit))
            ActivityTool.skipActivity<BorrowMoneyDetailsActivity>("info" to itemInfo, "account" to presenter.account, activityInfo = activityInfo)
        }
        adapter.setOnDeleteListener { _, itemInfo ->
            deleteDialog.setPositiveListener {
                CreditTool.delete(itemInfo)
            }.show()
        }
    }

    override fun refreshView(itemCount: Int) {
        if (itemCount == 0) {
            fl_details_kong.visibility = View.VISIBLE
            tv_interval.visibility = View.GONE
            rv_borrow_money.visibility = View.GONE
        } else {
            fl_details_kong.visibility = View.GONE
            tv_interval.visibility = View.VISIBLE
            rv_borrow_money.visibility = View.VISIBLE
        }
    }


    override fun onNext() {
        super.onNext()
        ActivityTool.skipActivity<AddAccountActivity>(
                "accountType" to presenter.account.accountType
                , "account" to presenter.account)
    }
}

