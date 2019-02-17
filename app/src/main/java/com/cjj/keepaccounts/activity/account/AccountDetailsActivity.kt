package com.cjj.keepaccounts.activity.account

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.widget.AbsListView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.bill.NewBillRecordActivity
import com.cjj.keepaccounts.activity.tool.EditTextActivity
import com.cjj.keepaccounts.adapter.AccountDetailsAdapter
import com.cjj.keepaccounts.base.TitleActivity
import com.cjj.keepaccounts.bean.*
import com.cjj.keepaccounts.dao.AccountDaoTool
import com.cjj.keepaccounts.enum.InputType
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.mvp.activity.account.accountdetails.CAccountDetails
import com.cjj.keepaccounts.mvp.activity.account.accountdetails.PAccountDetails
import com.cjj.keepaccounts.utils.ToastUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.utils.toMoney
import com.cjj.keepaccounts.view.AccountDetailsHeadView
import kotlinx.android.synthetic.main.activity_account_details.*
import java.math.BigDecimal

/**
 * @author chenjunjie
 * Created by CJJ on 2017/12/15 16:49.
 * 显示账户消费详情的页面
 */
class AccountDetailsActivity : TitleActivity<PAccountDetails>(), CAccountDetails.View {
    var account: Account by extra()
    private lateinit var adapter: AccountDetailsAdapter

    private lateinit var headView: AccountDetailsHeadView


    override fun getContentView() = Utils.inflate(R.layout.activity_account_details, this)


    override fun initView() {

        val possess = when (account.accountType.indexNum) {
            3, 10, 11 -> false
            else -> true
        }

        headView = AccountDetailsHeadView(this,possess)
        headView.backgroundColor = Color.parseColor(account.color)
        elv_details.addHeaderView(headView.headView)

        adapter = AccountDetailsAdapter(possess)
        elv_details.setAdapter(adapter)

        presenter.presenter()
    }

    override fun initListener() {
        super.initListener()
        headView.setOnBalanceClickListener {
            val info = ActivityJumpInfo(1, Activity.RESULT_OK, InputType.MONEY.type, getActivityTitleText().toString(), "编辑账户金额", account.money.toMoney())
            EditTextActivity.openActivity(info)
        }
        elv_details.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
//                Log.i("TAG", "$scrollState")

                if (AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState
                        && adapter.isOpen()) {
                    adapter.close()
                }
            }
        })
        adapter.setOnChildClickListener { _, _, item ->
            if (item.typeId != -2L) {
                val activityInfo = ActivityInfoBean(Utils.getString(R.string.details), Utils.getString(R.string.property), item.recordType.color)
                ActivityTool.skipActivity<BillDetailsActivity>(item, activityInfo)
            } else {
                ToastUtils.shortToast("账户变更没有详情")
            }
        }
        tv_add.setOnClickListener {
            ActivityTool.skipActivity<NewBillRecordActivity>(kotlin.Pair("account", account))
            overridePendingTransition(R.anim.activity_in_bottom, 0)
        }
    }

    override fun setYears(years: ArrayList<Int>) {
        headView.setYears(years)
        headView.setOnChangeListener {
            calculateData(it)
        }
        calculateData(years.first())
    }

    private fun calculateData(year: Int) {
        presenter.calculateData(account.uuid, year)
        //查询当前年份的数据
        headView.setYearText(year.toString())
    }

    override fun setData(arrayList: java.util.ArrayList<kotlin.Pair<AccountMonthDetails, java.util.ArrayList<Record>>>) {
        adapter.setDatas(arrayList)
        if (arrayList.size > 0) {
            elv_details.expandGroup(0)
        }
    }


    override fun setActivityTitleColor(color: Int) {
        super.setActivityTitleColor(color)
        if (this::headView.isInitialized) {
            headView.backgroundColor = color
        }
    }

    override fun detailsKongVisibility(visibility: Int) {
        headView.tvDetailsKong.visibility = visibility
    }

    override fun setBalanceHintText(hint: String) {
        headView.setBalanceHintText(hint)
    }

    override fun setType(type: Int) {
        headView.type = type
    }

    override fun setDividerHeight(height: Int) {
        elv_details.dividerHeight = height
    }


    override fun setBalance(money: Double) {
        headView.setBalance(money)
    }

    override fun setIncomeText(money: String) {
        headView.setIncomeText(money)
    }

    override fun setExpenditureText(money: String) {
        headView.setExpenditureText(money)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK && data == null) {
            return
        }
        when (requestCode) {
            1 -> {
                var money = data!!.getStringExtra("contentStr").toDouble()
                money = when (account.accountType.indexNum) {
                    3, 10, 11 -> {
                        -money
                    }
                    else -> {
                        money
                    }
                }
                val priceSpread = (BigDecimal(money) - BigDecimal(account.money)).toDouble()
                AccountDaoTool.changeAmount(account, priceSpread)

            }
        }
    }

    override fun onNext() {
        super.onNext()
        val intent = Intent(this, AddAccountActivity::class.java)
        intent.putExtra("accountType", account.accountType)
        intent.putExtra("account", account)
        startActivity(intent)
    }

}