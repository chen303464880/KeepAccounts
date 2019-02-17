package com.cjj.keepaccounts.activity.account

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.SelectAccountTypeAdapter
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.AccountType
import com.cjj.keepaccounts.bean.AccountTypeDao
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.RecyclerSpace
import com.cjj.keepaccounts.utils.Utils
import org.jetbrains.anko.recyclerview.v7.recyclerView

class SelectAccountTypeActivity : WhiteActivity<EmptyPresenter>() {

    lateinit var adapter: SelectAccountTypeAdapter
    lateinit var lvSelectAccountType: RecyclerView
    override fun getContentView(): View {
        lvSelectAccountType = recyclerView {
            overScrollMode = View.OVER_SCROLL_NEVER
        }
        return lvSelectAccountType
    }


    override fun initView() {

        setActivityTitleText(getString(R.string.select_account_type))
        setActivityBackText(getString(R.string.property))
        showTitleLine()


        val dao = DaoManager.getAccountTypeDao()
        val list = dao.queryBuilder().orderAsc(AccountTypeDao.Properties.IndexNum).list()
        val propertyList = arrayListOf<AccountType>()
        val liabilitiesList = arrayListOf<AccountType>()
        list.forEach {
            val indexNum = it.indexNum
            when (indexNum) {
                1, 2, 4, 5, 6, 7, 8, 9, 12 -> {//资产账户
                    propertyList.add(it)
                }
                3, 10, 11, 13 -> {//负债账户
                    liabilitiesList.add(it)
                }
                else -> {
                }
            }
        }
        adapter = SelectAccountTypeAdapter(propertyList)
        adapter.setData(liabilitiesList)
        lvSelectAccountType.adapter = adapter
        lvSelectAccountType.layoutManager = LinearLayoutManager(this)
        lvSelectAccountType.addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e7)))
    }

    override fun initListener() {
        super.initListener()
        adapter.setOnItemClickListener { _, _, itemInfo ->
            if (itemInfo.typeId != 2 && itemInfo.typeId != 3) {
                ActivityTool.skipActivity<AddAccountActivity>("accountType" to itemInfo)
            } else {
                ActivityTool.skipActivity<SelectBankActivity>("accountType" to itemInfo)
            }
        }
    }
}
