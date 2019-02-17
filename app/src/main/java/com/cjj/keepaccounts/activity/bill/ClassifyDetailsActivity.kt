package com.cjj.keepaccounts.activity.bill

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.OnClick
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.account.BillDetailsActivity
import com.cjj.keepaccounts.adapter.ClassifyDetailsAdapter
import com.cjj.keepaccounts.base.TitleActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.*
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.dialog.MsgDialog
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.*
import kotlinx.android.synthetic.main.activity_classify_details.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.collections.forEachWithIndex
import rx.Observable
import rx.functions.Action1
import rx.functions.Func0
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.math.BigDecimal

/**
 * Created by CJJ on 2018/5/14 20:42.
 * * Copyright © 2015-2019 CJJ All rights reserved.
 */
class ClassifyDetailsActivity : TitleActivity<EmptyPresenter>() {

    private var startYear = TimeUtils.year
    private var startMonth = TimeUtils.month - 1
    private var startDay = 1
    private var endYear = TimeUtils.year
    private var endMonth = TimeUtils.month - 1
    private var endDay = TimeUtils.getMonthMax(endYear, endMonth)

    private var startDate = 0
    private var endDate = 0

    private val info: StatementDetailBean by extra()

    private val adapter: ClassifyDetailsAdapter = ClassifyDetailsAdapter()

    private lateinit var layoutManager: LinearLayoutManager

    private val deleteDialog: MsgDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = MsgDialog(ActivityTool.currentActivity())
        dialog.setTitle(Utils.getString(R.string.delete_record_hint))
        dialog.setNegativeListener(null)
        dialog.message = Utils.getString(R.string.delete_record_affirm_hint)
        dialog
    }

    override fun getContentView(): View = Utils.inflate(R.layout.activity_classify_details, this)

    override fun initView() {
        RecordTool.addOnDaoChangeListener(daoChangeListener, presenter)

        ll_head_view.backgroundColor = mPageColor
        tv_type.text = if (info.isIncoming == 0) getString(R.string.expend) else getString(R.string.income)
        tv_money.text = info.money.toMoney()

        startYear = info.startTime / 10000
        startMonth = (info.startTime % 10000) / 100
        startDay = info.startTime % 100

        endYear = info.endTime / 10000
        endMonth = (info.endTime % 10000) / 100
        endDay = info.endTime % 100

        startDate = startYear * 10000 + startMonth * 100 + startDay
        endDate = endYear * 10000 + endMonth * 100 + endDay

        layoutManager = LinearLayoutManager(this)
        rv_classify_details.layoutManager = layoutManager
        rv_classify_details.adapter = adapter
        rv_classify_details.addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e7)))
        setTimeInterval()
    }

    @OnClick(R.id.iv_left, R.id.iv_right)
    fun onClick(view: View) {
        when (view.id) {
            R.id.iv_left -> {
                if (startMonth == 0) {
                    startMonth = 11
                    endMonth = 11
                    startYear -= 1
                    endYear -= 1
                } else {
                    startMonth -= 1
                    endMonth -= 1
                }

            }
            R.id.iv_right -> {
                if (endYear == TimeUtils.year && endMonth == TimeUtils.month - 1) {
                    return
                }
                if (startMonth == 11) {
                    startMonth = 0
                    endMonth = 0
                    startYear += 1
                    endYear += 1
                } else {
                    startMonth += 1
                    endMonth += 1
                }
            }
            else -> {

            }
        }
        startDay = 1
        endDay = TimeUtils.getMonthMax(endYear, endMonth)
        startDate = startYear * 10000 + startMonth * 100 + startDay
        endDate = endYear * 10000 + endMonth * 100 + endDay
        setTimeInterval()
    }

    private fun setTimeInterval() {
        tv_time_interval.text = if (startYear != endYear) {
            getString(R.string.year_time_interval,
                    startYear, startMonth + 1, startDay,
                    endYear, endMonth + 1, endDay)
        } else {
            if (startMonth != endMonth || startYear != TimeUtils.year) {
                getString(R.string.month_time_interval,
                        startYear, startMonth + 1, startDay,
                        endMonth + 1, endDay)
            } else {
                getString(R.string.day_time_interval,
                        startMonth + 1, startDay,
                        endDay)
            }
        }

        getData()
    }


    var mSuspensionHeight = 0
    var mCurrentPosition = 0
    override fun initListener() {
        super.initListener()
        adapter.setOnItemClickListener { _, _, itemInfo ->
            val activityInfo = ActivityInfoBean(Utils.getString(R.string.details), getActivityTitleText(), itemInfo.recordType.color)
            ActivityTool.skipActivity<BillDetailsActivity>(itemInfo, activityInfo = activityInfo)
        }
        adapter.setOnDeleteListener { _, itemInfo ->
            deleteDialog.setPositiveListener {
                RecordTool.delete(itemInfo)
            }.show()
        }
        adapter.setOnDeleteListener { _, itemInfo ->
            deleteDialog.setPositiveListener {
                RecordTool.delete(itemInfo)
            }.show()
        }
        rv_classify_details.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val data = adapter.data
                if (data.size > mCurrentPosition + 1) {

                    if (data[mCurrentPosition + 1].isNode) {
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
                        tv_interval.text = TimeUtils.longTurnTime(data[mCurrentPosition].rTime, "yyyy年MM月dd日")
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
    }


    private fun getData() {
        Observable.defer(if (info.type == 0) classifyObservable else memberObservable)
                .subscribeOnIo()
                .observeOn(Schedulers.computation())
                .map(map)
                .observeOnMain()
                .bindToLifecycle(presenter)
                .subscribe(onNext)
    }

    private val classifyObservable = Func0<Observable<List<Record>>> {

        val list = DaoManager.getRecordDao().queryBuilder()
                .where(RecordDao.Properties.IsDeleted.eq(0))
                .where(RecordDao.Properties.TypeId.eq(info.uuid))
                .where(RecordDao.Properties.TheDate.between(startDate, endDate))
                .orderDesc(RecordDao.Properties.RTime)
                .orderDesc(RecordDao.Properties.CreateTime)
                .list()
        Observable.just(list)
    }

    private val memberObservable = Func0<Observable<List<Record>>> {
        val make = TimeMarkUtils.mark()
        val queryBuilder = DaoManager.getRecordDao().queryBuilder()
                .where(RecordDao.Properties.IsDeleted.eq(0))
                .where(RecordDao.Properties.TypeId.gt(0))
                .where(RecordDao.Properties.TheDate.between(startDate, endDate))
        queryBuilder.join(RecordDao.Properties.TypeId, RecordType::class.java, RecordTypeDao.Properties.Uuid)
                .where(RecordTypeDao.Properties.IsIncoming.eq(info.isIncoming))
        if (info.uuid != -1L) {
            queryBuilder.join(RecordDao.Properties.Uuid, RecordToTag::class.java, RecordToTagDao.Properties.RecordId)
                    .where(RecordToTagDao.Properties.TagId.eq(info.uuid))
                    .where(RecordToTagDao.Properties.IsDeleted.eq(0))
        }


        var list = queryBuilder.orderDesc(RecordDao.Properties.RTime)
                .orderDesc(RecordDao.Properties.CreateTime)
                .list()

        if (info.uuid != -1L) {
            list.forEach {
                val memberCount = it.memberCount
                if (memberCount != 0) {
                    it.money = (BigDecimal(it.rateMoney).divide(BigDecimal(memberCount), 2, BigDecimal.ROUND_HALF_UP)).toDouble()
                }
            }
        } else {
            list = list.filter { it.memberCount == 0 }
        }
        make.printTime()
        Observable.just(list)
    }

    private val map = Func1<List<Record>, Pair<BigDecimal, ArrayList<Record>>> { list ->
        val recordList = ArrayList<Record>()
        var totalBigDecimal = BigDecimal.ZERO
        if (list.isNotEmpty()) {
            var node = Record()
            node.theDate = 0
            list.forEachWithIndex { i, it ->
                if (it.theDate != node.theDate || i == 0) {
                    node = Record()
                    node.isNode = true
                    node.rTime = it.rTime
                    node.setDate(it.year, it.month, it.day)
                    recordList.add(node)
                }
                recordList.add(it)
                totalBigDecimal += BigDecimal(Math.abs(it.money))
            }
        }
        Pair(totalBigDecimal, recordList)
    }

    private val onNext = Action1<Pair<BigDecimal, ArrayList<Record>>> {
        tv_money.text = it.first.toDouble().toMoney()
        adapter.setData(it.second)
        if (it.second.isNotEmpty()) {
            tv_interval.text = TimeUtils.longTurnTime(it.second.first().rTime, "yyyy年MM月dd日")
        }
    }

    private val daoChangeListener = object : OnDaoChangeListener<Record> {
        override fun onInsertEntity(entity: Record) {
            if (entity.typeId > 0L && entity.theDate in startDate..endDate) {
                val data = adapter.data
                //日节点下标
                var nodeIndex = data.binaryReversedSearch(entity.theDate) { it.theDate }
                //是否是新的一天
                var isNewNode = false
                if (nodeIndex >= 0) {
                    var node = data[nodeIndex]
                    while (!node.isNode) {
                        node = data[--nodeIndex]
                    }
                } else {
                    isNewNode = true
                    run {
                        data.forEachWithIndex { i, record ->
                            if (entity.theDate > record.theDate) {
                                nodeIndex = i
                                return@run
                            }
                        }
                        nodeIndex = data.size
                    }
                }
                if (isNewNode) {
                    val node = Record()
                    node.rTime = entity.rTime
                    node.isNode = true
                    adapter.insertData(nodeIndex, node)
                }

                if (info.type == 1) {
                    val memberCount = entity.memberCount
                    if (memberCount != 0) {
                        entity.money = (BigDecimal(entity.rateMoney).divide(BigDecimal(memberCount), 2, BigDecimal.ROUND_HALF_UP)).toDouble()
                    }
                }


                adapter.insertData(nodeIndex + 1, entity)


                info.money += Math.abs(entity.money)
                tv_money.text = info.money.toMoney()
                if (data.isNotEmpty()) {
                    tv_interval.text = TimeUtils.longTurnTime(data[layoutManager.findFirstVisibleItemPosition()].rTime, "yyyy年MM月dd日")
                }
            }
        }

        override fun onUpdateEntity(oldEntity: Record, newEntity: Record) {
            if (oldEntity.typeId <= 0) {
                return
            }
            if (info.type == 0 && oldEntity.typeId != newEntity.typeId) {
                onDeleteEntity(oldEntity)
                return
            }
            if (newEntity.recordType.isIncoming != oldEntity.recordType.isIncoming) {
                onDeleteEntity(oldEntity)
                return
            }
            if (oldEntity.theDate != newEntity.theDate) {
                onDeleteEntity(oldEntity)
                onInsertEntity(newEntity)
                return
            }

            val data = adapter.data
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

                if (info.type == 1) {
                    val memberCount = newEntity.memberCount
                    if (memberCount != 0) {
                        newEntity.money = (BigDecimal(newEntity.rateMoney).divide(BigDecimal(memberCount), 2, BigDecimal.ROUND_HALF_UP)).toDouble()
                    }
                }
                info.money += (Math.abs(newEntity.money) - Math.abs(oldEntity.money))
                tv_money.text = info.money.toMoney()


                adapter.updateData(dayIndex, newEntity)

            }
        }

        override fun onDeleteEntity(entity: Record) {
            val data = adapter.data
            if (entity.typeId > 0L && entity.theDate in startDate..endDate) {
                var nodeIndex = data.binaryReversedSearch(entity.theDate) { it.theDate }
                if (nodeIndex != -1) {
                    var node = data[nodeIndex]
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


                    info.money -= Math.abs(entity.money)
                    tv_money.text = info.money.toMoney()


                    if (count == 1) {
                        adapter.removeData(nodeIndex, 2)
                    } else {
                        adapter.removeData(dayIndex)
                    }

                }

            }
        }
    }


}