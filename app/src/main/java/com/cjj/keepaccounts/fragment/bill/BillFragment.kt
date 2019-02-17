package com.cjj.keepaccounts.fragment.bill

import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.bill.CalendarRecordActivity
import com.cjj.keepaccounts.activity.bill.NewBillRecordActivity
import com.cjj.keepaccounts.activity.bill.SearchRecordActivity
import com.cjj.keepaccounts.activity.bill.budget.BudgetActivity
import com.cjj.keepaccounts.activity.setting.SettingActivity
import com.cjj.keepaccounts.adapter.BillRecordAdapter
import com.cjj.keepaccounts.base.BaseFragment
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.ListBook
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.bean.RecordDao
import com.cjj.keepaccounts.bean.event.SyncErrorEvent
import com.cjj.keepaccounts.bean.event.SyncOverEvent
import com.cjj.keepaccounts.bean.event.SyncProgressEvent
import com.cjj.keepaccounts.bean.event.SyncStartEvent
import com.cjj.keepaccounts.dao.ListBookTool
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.dao.RecordTool.getMonthRecord
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.*
import com.cjj.keepaccounts.view.SpringBackLayout
import com.cjj.keepaccounts.view.clearItemAnimator
import kotlinx.android.synthetic.main.fragment_bill.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.textColorResource
import java.util.*

/**
 * @author CJJ
 * Created by CJJ on 2017/11/10 16:42.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 * 用于记账的页面
 */
class BillFragment : BaseFragment<EmptyPresenter>() {

    private var state = 0
    private val initial = 0
    private val sync = 1
    private val search = 2
    private val animatorState = 3

    private val thisDate = TimeUtils.year * 100 + TimeUtils.month - 1
    private var browseDate = thisDate
//    private val recordInfoMap = hashMapOf<Int, MonthRecordInfo>()

    //预算
    private var monthBudget = 0.0
    //当月支出
    private var expend = 0.0
    //预算模式
    private var budgetMode = ListBookTool.getBudget().listBudgetShow


    private var insertSize = 0
        set(value) {
            field = value
            offset = page * pageSize + value
        }

    private var page = 0
        set(value) {
            field = value
            offset = value * pageSize + insertSize
        }

    private val pageSize = 100
    private var offset = page * pageSize + insertSize
    private var isLoading = false

    private var initialWidth = Utils.dip2px(90)
    private var maxWidth = Utils.widthPixels - Utils.getDimension(R.dimen.start) - Utils.getDimension(R.dimen.end)
    private var isSearchText = false
    private var searchValue = Utils.dip2px(90)
        set(value) {
            field = value
            state = when (value) {
                initialWidth -> {
                    initial
                }
                maxWidth -> {
                    search
                }
                else -> {
                    animatorState
                }
            }
            pb_sync.layoutParams.width = value
            pb_sync.requestLayout()
            val alpha = 1 - (value.toFloat() - initialWidth) / (maxWidth.toFloat() - initialWidth - Utils.dip2px(50))
            iv_setting.alpha = alpha
            tv_day.alpha = alpha

//            Log.i("TAG", "oldValue:${tv_search.alpha}")
            if (alpha > 0.5F) {
                if (!isSearchText) {
                    isSearchText = true
                    tv_search.text = "默认账本"
                    tv_search.setCompoundDrawables(null, null, Utils.getDrawable(R.mipmap.sanjiaojiantou_xia), null)
                }
                tv_search.alpha = 1 - (1 - alpha) * 2
            } else {
                if (isSearchText) {
                    isSearchText = false
                    tv_search.text = "请输入关键字"
                    tv_search.setCompoundDrawables(Utils.getDrawable(R.mipmap.ic_search), null, null, null)
                }
                tv_search.alpha = (0.5F - alpha) * 2
            }
//            Log.i("TAG", "newValue:${tv_search.alpha}")
        }
    private val valueAnimator: ValueAnimator by lazy(LazyThreadSafetyMode.NONE) {
        val valueAnimator = ValueAnimator.ofInt()
        valueAnimator!!.addUpdateListener {
            searchValue = it.animatedValue as Int
        }
        valueAnimator.duration = 200
        valueAnimator
    }
    /**
     * 是否还有数据
     */
    private var isEnd = false

    private var tvBlank: TextView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bill, container, false)
    }

    private lateinit var billRecordAdapter: BillRecordAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun initView() {
        EventBusUtils.register(this, presenter)
        ListBookTool.addOnDaoChangeListener(listBookDaoListener, presenter)
        RecordTool.addOnDaoChangeListener(recordChangeListener, presenter)
        sbl.friction = 2
        tv_day.text = TimeUtils.day.toString()
        billRecordAdapter = BillRecordAdapter(context!!)
        rv_bill_record.adapter = billRecordAdapter
        layoutManager = LinearLayoutManager(context)
        rv_bill_record.layoutManager = layoutManager
        rv_bill_record.clearItemAnimator()
        initTitle()
        getRecord()
    }

    private fun initTitle() {
        val monthRecord = getMonthRecord(TimeUtils.year, TimeUtils.month - 1)

        tv_month_income.text = getString(R.string.month_income, TimeUtils.month, monthRecord.income)
        tv_month_expend.text = getString(R.string.month_expend, TimeUtils.month, monthRecord.expend)

        val budget = getBudget(ListBookTool.getBudget().listBudgetShow)
        monthBudget = budget.first
        setThisExpend(monthBudget, budget.second)
    }


    override fun initListener() {
        super.initListener()

        pb_sync.setOnClickListener {
            ActivityTool.skipActivity<SearchRecordActivity>()
            activity!!.overridePendingTransition(0, 0)
        }

        //删除某条记录
        billRecordAdapter.setOnDeleteListener { item ->
            RecordTool.delete(item)
        }

        //编辑某条记录
        billRecordAdapter.setOnEditListener { _, _, item ->
            ActivityTool.skipActivity<NewBillRecordActivity>(item)
            activity!!.overridePendingTransition(R.anim.activity_in_bottom, 0)
        }

        //进入设置页面
        iv_setting.setOnClickListener {
            ActivityTool.skipActivity<SettingActivity>()
        }

        //进入预算页面
        fl_budget.setOnClickListener {
            ActivityTool.skipActivity<BudgetActivity>()
        }

        //进入当月详情页面
        tv_day.setOnClickListener {
            ActivityTool.skipActivity<CalendarRecordActivity>()
        }

        //监听RecyclerView,加载数据
        rv_bill_record.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var isUP = false

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (sbl.scrollY < -300 || state == sync) {
                    return
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !valueAnimator.isStarted) {
                    if ((!isUP && pb_sync.layoutParams.width != initialWidth) || (isUP && pb_sync.layoutParams.width != maxWidth)) {
                        valueAnimator.setIntValues(pb_sync.layoutParams.width, if (isUP) initialWidth else maxWidth)
                        valueAnimator.start()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                isUP = dy < 0
                if (sbl.scrollY > -300 && state != sync) {
                    val value = pb_sync.layoutParams.width + dy
                    if (value in initialWidth..maxWidth) {
                        searchValue = value
                    }
                }

                if (billRecordAdapter.itemCount > 0) {

                    val item = billRecordAdapter.getItem(layoutManager.findFirstVisibleItemPosition())
                    val i = item.year * 100 + item.month
                    if (i != browseDate) {
                        browseDate = i
                        val monthRecord = getMonthRecord(item.year, item.month)
                        tv_month_income.text = getString(R.string.month_income, (item.month + 1), monthRecord.income)
                        tv_month_expend.text = getString(R.string.month_expend, (item.month + 1), monthRecord.expend)
                    }

                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (!isLoading && lastPosition >= billRecordAdapter.itemCount - 20) {
                        isLoading = true
                        getRecord()
                    }
                }

            }
        })

        sbl.setOnScrollChangeListener { _: View, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (state != initial) {
                return@setOnScrollChangeListener
            }
            when (scrollY) {
                in 0 downTo -100 -> {
                    if (oldScrollY !in 0 downTo -100) {
                        tv_search.text = "默认账本"
                        tv_search.setCompoundDrawables(null, null, Utils.getDrawable(R.mipmap.sanjiaojiantou_xia), null)
                    }
                }
                in -100 downTo -300 -> {
                    if (oldScrollY !in -100 downTo -300) {
                        tv_search.text = "下拉同步"
                        tv_search.setCompoundDrawables(null, null, null, null)
                    }
                }
                in -300 downTo -1000 -> {
                    if (oldScrollY !in -300 downTo -1000) {
                        tv_search.text = "松开同步"
                        tv_search.setCompoundDrawables(null, null, null, null)
                    }
                }
            }
        }
        sbl.setOnStartSpringBackListener(object : SpringBackLayout.OnSpringBackListener {
            override fun onTopSpringBack() {
                if (state == sync) {
                    return
                }
                if (state != initial || sbl.scrollY !in -300 downTo -1000) {
                    tv_search.text = "默认账本"
                    tv_search.setCompoundDrawables(null, null, Utils.getDrawable(R.mipmap.sanjiaojiantou_xia), null)
                    return
                }
//                state = sync
//                SyncService.startSyncService(context!!)



            }

            override fun onBottomSpringBack() {

            }
        })
    }

    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var node: Record
    private fun getRecord() {
        DaoManager.getRecordDao().queryBuilder()
                .where(RecordDao.Properties.TypeId.ge(0))
                .where(RecordDao.Properties.TypeId.notEq(10000))
                .where(RecordDao.Properties.IsDeleted.eq(0))
                .orderDesc(RecordDao.Properties.TheDate)
                .orderDesc(RecordDao.Properties.RTime)
                .orderDesc(RecordDao.Properties.CreateTime)
                .limit(pageSize)
                .offset(offset)
                .rx().list()
                .observeOnMain()
                .subscribe { list ->
                    //                    LogUtils.list(list)
                    if (list.isNotEmpty()) {
                        if (page == 0) {
                            if (tvBlank != null && tvBlank!!.visibility == View.VISIBLE) {
                                tvBlank!!.visibility = View.GONE
                            }
                        }
                        val billRecords = ArrayList<Record>()
                        if (page == 0) {
                            node = Record()
                            node.isNode = true
                            year = list.first().year
                            month = list.first().month
                            day = list.first().day
                            node.setDate(year, month, day)
                            billRecords.add(node)
                        }
                        list.forEach {
                            if (day != it.day || month != it.month || year != it.year) {
                                node = Record()
                                node.isNode = true
                                billRecords.add(node)
                                day = it.day
                                if (month != it.month) {
                                    month = it.month
                                }
                                if (year != it.year) {
                                    year = it.year
                                }
                                node.setDate(year, month, day)
                            }
                            val type = it.recordType
                            if (type.isIncoming == 0) {
                                node.expend += Math.abs(it.rateMoney)
                            } else {
                                node.income += Math.abs(it.rateMoney)
                            }
                            billRecords.add(it)
                        }
                        billRecordAdapter.insertData(billRecords)

                        page++
                    } else {
                        isEnd = true
                        if (page == 0) {
                            if (tvBlank == null) {
                                tvBlank = vs_blank.inflate() as TextView
                            }
                        }
                    }
                    isLoading = false
                }
    }

    private fun refreshBudget(mode: String) {
        budgetMode = mode
        val budget = getBudget(mode)
        setThisExpend(budget.first, budget.second)
    }

    private fun getBudget(mode: String): Pair<Double, Double> {
        return when (mode) {
            "week" -> {
                tv_budget_mode.text = getText(R.string.week_budget)
                ListBookTool.getWeekBudget(TimeUtils.year, TimeUtils.month - 1, TimeUtils.day)
            }
            "day" -> {
                tv_budget_mode.text = getText(R.string.day_budget)
                ListBookTool.getDayBudget(TimeUtils.year, TimeUtils.month - 1, TimeUtils.day)
            }
            else -> {
                tv_budget_mode.text = getText(R.string.month_budget)
                ListBookTool.getMonthBudget(TimeUtils.year, TimeUtils.month - 1, TimeUtils.day)
            }
        }
    }

    /**
     * 设置当前月的支出
     */
    private fun setThisExpend(budget: Double, expend: Double) {

        if (budget == monthBudget && (expend == this.expend && expend != 0.0)) {
            return
        }
        //修改之前旧的剩余预算
        val budgetStartValue = (this.monthBudget - this.expend).toFloat()
        //新的剩余预算
        val budgetEndValue = (budget - expend).toFloat()

        this.monthBudget = budget
        this.expend = expend


        val waveStartValue = wv.waveLevel
        var waveEndValue = (budgetEndValue / monthBudget).toFloat()
        if (waveEndValue < 0F) {
            waveEndValue = 0F
        }

        val mAnimator = ValueAnimator.ofFloat(0F, 1F)
        mAnimator.interpolator = LinearInterpolator()
        mAnimator.addUpdateListener { animation ->
            val animatorValue = animation.animatedValue as Float
            wv.waveLevel = Utils.evaluate(animatorValue, waveStartValue, waveEndValue)
            tv_budget.text = Utils.evaluate(animatorValue, budgetStartValue, budgetEndValue).toMoney()
        }
        mAnimator.interpolator = DecelerateInterpolator()
        mAnimator.duration = 2000
        mAnimator.start()
    }


    /**
     * 修改某个月的支出收入情况
     */
    private fun setMonthRecordInfo(year: Int, month: Int, income: Double, expend: Double) {

        val monthRecordInfo = RecordTool.getMonthRecord(year, month)
        val key = year * 100 + month
        if (income != 0.0) {
            monthRecordInfo.income += income
            if (key == browseDate) {
                tv_month_income.text = getString(R.string.month_income, (month + 1), monthRecordInfo.income)
            }
        }
        if (expend != 0.0) {
            monthRecordInfo.expend += expend
            if (key == browseDate) {
                tv_month_expend.text = getString(R.string.month_expend, (month + 1), monthRecordInfo.expend)
            }

            //当月,修改剩余预算
            if (key == thisDate) {
                setThisExpend(monthBudget, monthRecordInfo.expend)
            }
        }


    }

    private val recordChangeListener = object : OnDaoChangeListener<Record> {
        override fun onInsertEntity(entity: Record) {
            if (entity.typeId < 0) {
                return
            }
            //日节点下标
            var nodeIndex = 0
            //是否是新的一天
            var isNewNode = false

            val data = billRecordAdapter.data
            if (data.isNotEmpty()) {
                if (entity.theDate >= data.last().theDate) {//
                    if (entity.theDate <= data.first().theDate) {//在已加载的数据的日期中
                        nodeIndex = data.binaryReversedSearch(entity.theDate) { it.theDate }
                        if (nodeIndex >= 0) {//已有当天的数据
                            var node = data[nodeIndex]
                            //找日节点
                            while (!node.isNode) {
                                node = data[--nodeIndex]
                            }


                        } else {//没有当天数据
                            isNewNode = true
                            run {
                                data.forEachWithIndex { i, record ->
                                    if (entity.theDate < record.theDate) {
                                        nodeIndex = i
                                        return@run
                                    }
                                }
                                nodeIndex = data.size
                            }
                        }
                    } else {
                        isNewNode = true
                    }
                } else {//不在已加载的数据中,在上拉时加载
                    if (isEnd || (isEnd && billRecordAdapter.itemCount < pageSize)) {//数据加载完毕,不能再上拉
                        nodeIndex = billRecordAdapter.itemCount
                        isNewNode = true
                    }
                }
            } else {
                isNewNode = true
            }

            if (isNewNode) {
                val node = Record()
                node.isNode = true
                if (entity.recordType.isIncoming == 0) {
                    node.expend = entity.rateMoney
                } else {
                    node.income = entity.rateMoney
                }
                node.setDate(entity.year, entity.month, entity.day)
                billRecordAdapter.insertData(nodeIndex, node)
            } else {
                val node = billRecordAdapter.getItem(nodeIndex)
                if (entity.recordType.isIncoming == 0) {
                    node.expend += entity.rateMoney
                } else {
                    node.income += entity.rateMoney
                }
                billRecordAdapter.updateData(nodeIndex)
            }

            if (entity.recordType.isIncoming == 0) {
                setMonthRecordInfo(entity.year, entity.month, 0.0, entity.rateMoney)
            } else {
                setMonthRecordInfo(entity.year, entity.month, entity.rateMoney, 0.0)
            }

            billRecordAdapter.insertData(nodeIndex + 1, entity)
            rv_bill_record.scrollToPosition(nodeIndex)
            insertSize++

            if (billRecordAdapter.itemCount != 0) {
                if (tvBlank != null && tvBlank!!.visibility == View.VISIBLE) {
                    tvBlank!!.visibility = View.GONE
                }
            }

        }

        override fun onUpdateEntity(oldEntity: Record, newEntity: Record) {
            val data = billRecordAdapter.data
            if (data.isNotEmpty() && oldEntity.typeId > 0) {
                if (oldEntity.theDate >= data.last().theDate) {
                    if (oldEntity.theDate == newEntity.theDate) {
                        var nodeIndex = data.binaryReversedSearch(oldEntity.theDate) { it.theDate }
                        if (nodeIndex >= 0) {
                            var node = data[nodeIndex]
                            //找日节点
                            while (!node.isNode) {
                                node = data[--nodeIndex]
                            }
                            var dayIndex = nodeIndex + 1 //要删除的记录的下标
                            var day = data[dayIndex] //适配器中的记录
                            while (day.uuid != oldEntity.uuid) { //day不是要删除的记录,则查找下一条
                                val index = ++dayIndex
                                if (index >= data.size) { // 查找的下标超过数组,列表中不存在,return
                                    return
                                }
                                day = data[index]
                                if (day.theDate != oldEntity.theDate) {//找到另一天的数据,return
                                    return
                                }
                            }

                            if (oldEntity.recordType.isIncoming != newEntity.recordType.isIncoming) {
                                if (oldEntity.recordType.isIncoming == 0) {//之前为支出,现在为收入
                                    node.expend = node.expend - oldEntity.rateMoney//减去旧的支出
                                    node.income = node.income + newEntity.rateMoney//加上新的收入
                                    setMonthRecordInfo(newEntity.year, newEntity.month, newEntity.rateMoney, -oldEntity.rateMoney)
                                } else {////之前为收入,现在为支出
                                    node.income = node.income - oldEntity.rateMoney//减去旧的收入
                                    node.expend = node.expend + newEntity.rateMoney//加上新的支出
                                    setMonthRecordInfo(newEntity.year, newEntity.month, -newEntity.rateMoney, oldEntity.rateMoney)
                                }
                            } else {
                                if (oldEntity.recordType.isIncoming == 0) {//之前为支出
                                    node.expend = node.expend - oldEntity.rateMoney + newEntity.rateMoney
                                    setMonthRecordInfo(newEntity.year, newEntity.month, 0.0, newEntity.rateMoney - oldEntity.rateMoney)
                                } else {//之前为收入
                                    node.income = node.income - oldEntity.rateMoney + newEntity.rateMoney
                                    setMonthRecordInfo(newEntity.year, newEntity.month, newEntity.rateMoney - oldEntity.rateMoney, 0.0)
                                }
                            }

                            billRecordAdapter.updateData(nodeIndex)
                            billRecordAdapter.updateData(dayIndex, newEntity)

                        }
                    } else {
                        onDeleteEntity(oldEntity)
                        onInsertEntity(newEntity)
                    }
                }
            }
        }

        override fun onDeleteEntity(entity: Record) {
            val data = billRecordAdapter.data
            if (data.isNotEmpty()) {
                if (entity.typeId > 0 && entity.theDate >= data.last().theDate) {
                    var nodeIndex = data.binaryReversedSearch(entity.theDate) { it.theDate }
                    if (nodeIndex >= 0) {
                        var node = data[nodeIndex]
                        //找日节点
                        while (!node.isNode) {
                            node = data[--nodeIndex]
                        }

                        var dayIndex = nodeIndex + 1 //要删除的记录的下标
                        var count = 1 //当日的记录总数
                        var day = data[dayIndex] //适配器中的记录
                        while (day.uuid != entity.uuid) { //day不是要删除的记录,则查找下一条
                            val index = ++dayIndex
                            if (index >= data.size) { // 查找的下标超过数组,列表中不存在,return
                                return
                            }
                            day = data[index]
                            if (day.theDate != entity.theDate) {//找到另一天的数据,return
                                return
                            }
                            count++ //当日的记录加1
                        }

                        if (count == 1) {//count==1时,说明node下一条数据就是要删除的数据,
                            //查看当日是否还有其它记录
                            if (data.size > dayIndex + 1 && data[dayIndex + 1].theDate == entity.theDate) {
                                count++
                            }
                        }

                        if (entity.recordType.isIncoming == 0) {
                            node.expend -= entity.rateMoney
                            setMonthRecordInfo(entity.year, entity.month, 0.0, -entity.rateMoney)
                        } else {
                            node.income -= entity.rateMoney
                            setMonthRecordInfo(entity.year, entity.month, -entity.rateMoney, 0.0)
                        }



                        if (count == 1) {//当日只有一条记录,则连node一起删除
                            billRecordAdapter.removeData(nodeIndex, 2)
                        } else {
                            billRecordAdapter.removeData(dayIndex)
                            billRecordAdapter.notifyItemChanged(nodeIndex)
                        }

                        if (billRecordAdapter.itemCount == 0) {
                            if (tvBlank == null) {
                                tvBlank = vs_blank.inflate() as TextView
                            } else {
                                tvBlank!!.visibility = View.VISIBLE
                            }
                        }

                    }
                }
            }
        }
    }

    private val listBookDaoListener = object : OnDaoChangeListener<ListBook> {
        override fun onInsertEntity(entity: ListBook) {

        }

        override fun onUpdateEntity(oldEntity: ListBook, newEntity: ListBook) {
            if (newEntity.isDefault == 1) {
                if (newEntity.listBudgetShow != budgetMode) {
                    refreshBudget(newEntity.listBudgetShow)
                    return
                }
                if (monthBudget != newEntity.listBudget) {
                    setThisExpend(newEntity.listBudget, expend)
                }

            }
        }

        override fun onDeleteEntity(entity: ListBook) {

        }
    }

    @Suppress("unused")
    @Subscribe()
    fun syncProgress(sync: SyncProgressEvent) {
        var process = sync.progress
        if (process.toFloat() / pb_sync.max * pb_sync.width < pb_sync.height) {
            process = (pb_sync.height.toFloat() / pb_sync.width.toFloat() * pb_sync.max).toInt()
        }
        pb_sync.progress = process
    }

    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun syncStart(@Suppress("UNUSED_PARAMETER") event: SyncStartEvent) {
        state = sync
        pb_sync.progressDrawable = Utils.getDrawable(R.drawable.shape_progress_bar_sync_drawable)
        tv_search.background = null
        tv_search.text = "正在同步"
        tv_search.setCompoundDrawables(null, null, null, null)
        tv_search.textColorResource = R.color.white
    }


    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun syncOver(@Suppress("UNUSED_PARAMETER") event: SyncOverEvent) {
        initTitle()
        year = 0
        month = 0
        day = 0
        insertSize = 0
        page = 0
        pb_sync.progress = 0
        billRecordAdapter.clear()
        state = initial
        pb_sync.progressDrawable = null
        tv_search.text = "默认账本"
        tv_search.setCompoundDrawables(null, null, Utils.getDrawable(R.mipmap.sanjiaojiantou_xia), null)
        tv_search.textColorResource = R.color.text_color_655f5f
        getRecord()
    }


    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun syncError(error: SyncErrorEvent) {
        ToastUtils.shortToast("同步失败,msg:${error.ex.message}")
        pb_sync.progressDrawable = null
        tv_search.text = "默认账本"
        tv_search.setCompoundDrawables(null, null, Utils.getDrawable(R.mipmap.sanjiaojiantou_xia), null)
        tv_search.textColorResource = R.color.text_color_655f5f
    }

}