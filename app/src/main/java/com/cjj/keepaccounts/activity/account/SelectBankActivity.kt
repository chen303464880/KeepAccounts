package com.cjj.keepaccounts.activity.account

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.SelectBackAdapter
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.bean.AccountType
import com.cjj.keepaccounts.bean.BankInfoBean
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.mvp.activity.account.selectbank.CSelectBank
import com.cjj.keepaccounts.mvp.activity.account.selectbank.PSelectBank
import com.cjj.keepaccounts.utils.RecyclerSpace
import com.cjj.keepaccounts.utils.Utils
import kotlinx.android.synthetic.main.activity_select_bank.*

/**
 * Created by CJJ on 2018/4/10 20:22.
 * * Copyright © 2015-2019 CJJ All rights reserved.
 */
class SelectBankActivity : WhiteActivity<PSelectBank>(), CSelectBank.View {

    private val accountType by extra<AccountType>("accountType")

    override val adapter: SelectBackAdapter = SelectBackAdapter()

    private var isEdit = false

    override fun getContentView(): View = Utils.inflate(R.layout.activity_select_bank, this)


    override fun initView() {
        setActivityTitleText(getString(R.string.select_bank))
        setActivityBackText(getString(R.string.back))
        showTitleLine()

        isEdit = intent.getBooleanExtra("isEdit", false)


        rv_bank.layoutManager = LinearLayoutManager(this)
        rv_bank.addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e7)))
        rv_bank.adapter = adapter
        initData("")
    }

    private fun initData(key: String) {
        presenter.searchBank(key)
    }

    override fun setAdapterData(data: List<BankInfoBean>) {
        super.setAdapterData(data)
        if (data.isNotEmpty()) {
            tv_interval.text = data.first().nodeName.toString()
        }
    }


    var mSuspensionHeight = 0
    var mCurrentPosition = 0

    override fun initListener() {
        super.initListener()
        adapter.setOnItemClickListener { _, _, itemInfo ->
            //            Log.i("TAG", itemInfo.logoRes.toString())
            if (isEdit) {
                val intent = Intent()
                intent.putExtra("bankInfo", itemInfo)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                ActivityTool.skipActivity<AddAccountActivity>(Pair("accountType", accountType), Pair("bankInfo", itemInfo))
            }
        }

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                initData(et_search.text.toString())
            }
        })

        rv_bank.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val layoutManager = rv_bank.layoutManager as LinearLayoutManager
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mCurrentPosition + 1 >= adapter.itemCount) {
                    return
                }
                val item = adapter.getItem(mCurrentPosition + 1)

                if (item.isNode) {
                    val view = layoutManager.findViewByPosition(mCurrentPosition + 1)
                    if (view != null) {
                        if (view.top <= tv_interval.height) {
                            tv_interval.y = -(tv_interval.height - view.top).toFloat()
                        } else {
                            tv_interval.y = 0F
                        }
                    }
                }
//                Log.i("TAG", "mCurrentPosition:$mCurrentPosition")
//                Log.i("TAG", "FirstVisibleItemPosition():${layoutManager.findFirstVisibleItemPosition()}")
                if (mCurrentPosition != layoutManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = layoutManager.findFirstVisibleItemPosition()
                    tv_interval.y = 0F
//                    Log.i("TAG", item.toString())
                    tv_interval.text = item.nodeName.toString()

                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mSuspensionHeight = tv_interval.height
            }
        })
    }

}