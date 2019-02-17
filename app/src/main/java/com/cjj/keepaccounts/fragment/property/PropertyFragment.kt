package com.cjj.keepaccounts.fragment.property

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.PropertyAdapter
import com.cjj.keepaccounts.base.BaseFragment
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.Account
import com.cjj.keepaccounts.bean.AccountDao
import com.cjj.keepaccounts.bean.event.SyncOverEvent
import com.cjj.keepaccounts.dao.AccountDaoTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.*
import com.cjj.keepaccounts.view.setRadioGroup
import kotlinx.android.synthetic.main.fragment_property.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @author CJJ
 */
class PropertyFragment : BaseFragment<EmptyPresenter>() {
    private val decoration = ItemDecoration()
    private val accountList = arrayListOf<Account>()

    private lateinit var liabilitiesAdapter: PropertyAdapter
    private lateinit var propertyAdapter: PropertyAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_property, container, false)


    override fun initView() {
        EventBusUtils.register(this, presenter)
        AccountDaoTool.addOnDaoChangeListener(daoChangeListener, presenter)
        initData()
    }

    private fun initData() {
        val dao = DaoManager.getAccountDao()
        accountList.addAll(dao.queryBuilder()
                .where(AccountDao.Properties.IsDeleted.eq(0))
                .orderAsc(AccountDao.Properties.OrderIndex)
                .list())

        val propertyList = arrayListOf<Account>()
        val liabilitiesList = arrayListOf<Account>()
        accountList.forEach {
            val indexNum = it.accountType.indexNum
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

        val recyclerViewList = arrayListOf<RecyclerView>()
        for (i in 0..1) {
            val view = Utils.inflate(R.layout.view_property_recycle_view, activity!!)
            val lv = view.findViewById<RecyclerView>(R.id.lv_property)
            lv.layoutManager = LinearLayoutManager(context)
            if (i == 0) {
                propertyAdapter = PropertyAdapter(accountList, i == 0)
                propertyAdapter.setData(propertyList)
                lv.adapter = propertyAdapter

                //创建SimpleItemTouchHelperCallback
                val callback = SimpleItemTouchHelperCallback(propertyAdapter)
                //用Callback构造ItemtouchHelper
                val touchHelper = ItemTouchHelper(callback)
                //调用ItemTouchHelper的attachToRecyclerView方法建立联系
                touchHelper.attachToRecyclerView(lv)

            } else {
                liabilitiesAdapter = PropertyAdapter(accountList, i == 0)
                liabilitiesAdapter.setData(liabilitiesList)
                lv.adapter = liabilitiesAdapter

                //创建SimpleItemTouchHelperCallback
                val callback = SimpleItemTouchHelperCallback(liabilitiesAdapter)
                //用Callback构造ItemtouchHelper
                val touchHelper = ItemTouchHelper(callback)
                //调用ItemTouchHelper的attachToRecyclerView方法建立联系
                touchHelper.attachToRecyclerView(lv)
            }
            lv.addItemDecoration(decoration)
            recyclerViewList.add(lv)
        }
        vp_property.adapter = LvViewPager(recyclerViewList)
        vp_property.setRadioGroup(rg_property)
    }

    private val daoChangeListener = object : OnDaoChangeListener<Account> {
        override fun onInsertEntity(entity: Account) {
            val indexNum = entity.accountType.indexNum
            accountList.add(entity)
            when (indexNum) {
                1, 2, 4, 5, 6, 7, 8, 9, 12 -> {//资产账户
                    propertyAdapter.insertData(entity)
                }
                3, 10, 11, 13 -> {//负债账户2
                    liabilitiesAdapter.insertData(entity)
                    liabilitiesAdapter.notifyItemChanged(0)
                }
                else -> {

                }
            }
            propertyAdapter.notifyItemChanged(0)
        }

        override fun onUpdateEntity(oldEntity: Account, newEntity: Account) {
            var index = -1
            for (i in 0 until accountList.size) {
                if (accountList[i].uuid == newEntity.uuid) {
                    index = i
                    break
                }
            }
            if (index != -1) {

                val indexNum = newEntity.accountType.indexNum
                when (indexNum) {
                    1, 2, 4, 5, 6, 7, 8, 9, 12 -> {//资产账户
                        val i = propertyAdapter.data.binarySearch(newEntity.orderIndex) { it.orderIndex }
                        if (i != -1) {
                            propertyAdapter.getItem(i).refresh()
                            propertyAdapter.notifyItemChanged(i + 1)
                        } else {
                            return
                        }
                    }
                    3, 10, 11, 13 -> {//负债账户
                        val i = liabilitiesAdapter.data.binarySearch(newEntity.orderIndex) { it.orderIndex }
                        if (i != -1) {
                            liabilitiesAdapter.getItem(i).refresh()
                            liabilitiesAdapter.notifyItemChanged(i + 1)
                            liabilitiesAdapter.notifyItemChanged(0)
                        } else {
                            return
                        }
                    }
                    else -> {

                    }
                }

                accountList[index] = newEntity

                propertyAdapter.notifyItemChanged(0)
            }
        }


        override fun onDeleteEntity(entity: Account) {
            val indexNum = entity.accountType.indexNum
            val interpolationSearch = accountList.interpolationSearch(entity.orderIndex.toInt()) { it.orderIndex.toInt() }
            if (interpolationSearch != -1) {
                accountList.removeAt(interpolationSearch)
            }
            when (indexNum) {
                1, 2, 4, 5, 6, 7, 8, 9, 12 -> {//资产账户
                    val i = propertyAdapter.data.binarySearch(entity.orderIndex) { it.orderIndex }
                    if (i != -1) {
                        propertyAdapter.removeData(i)
                    }
                }
                3, 10, 11, 13 -> {//负债账户
                    val i = liabilitiesAdapter.data.binarySearch(entity.orderIndex) { it.orderIndex }
                    if (i != -1) {
                        liabilitiesAdapter.removeData(i)
                        liabilitiesAdapter.notifyItemChanged(0)
                    }
                }
                else -> {

                }
            }
            propertyAdapter.notifyItemChanged(0)

        }
    }

    class LvViewPager(private val recyclerViewList: List<RecyclerView>) : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = recyclerViewList[position]
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//            super.destroyItem(container, position, `object`)
            container.removeView(recyclerViewList[position])
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

        override fun getCount(): Int = 2

    }

    class ItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: android.support.v7.widget.RecyclerView, state: android.support.v7.widget.RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom = Utils.dip2px(10F)
            outRect.top = Utils.dip2px(0F)
            outRect.left = Utils.dip2px(8F)
            outRect.right = Utils.dip2px(8F)
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = Utils.dip2px(10F)
            }

        }
    }


    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun syncOver(@Suppress("UNUSED_PARAMETER") event: SyncOverEvent) {
        accountList.clear()
        accountList.addAll(DaoManager.getAccountDao().queryBuilder()
                .where(AccountDao.Properties.IsDeleted.eq(0))
                .orderAsc(AccountDao.Properties.OrderIndex)
                .list())

        val propertyList = arrayListOf<Account>()
        val liabilitiesList = arrayListOf<Account>()
        accountList.forEach {
            val indexNum = it.accountType.indexNum
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
        propertyAdapter.setData(propertyList)
        liabilitiesAdapter.setData(liabilitiesList)
    }
}
