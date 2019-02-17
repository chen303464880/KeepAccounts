package com.cjj.keepaccounts.activity.bill

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.text.inSpans
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.account.BillDetailsActivity
import com.cjj.keepaccounts.adapter.CalendarRecordAdapter
import com.cjj.keepaccounts.adapter.RecordAdapter
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.*
import com.cjj.keepaccounts.dao.ListBookTool
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.dialog.MsgDialog
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.*
import com.cjj.keepaccounts.view.clearItemAnimator
import kotlinx.android.synthetic.main.activity_calendar_record.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.collections.forEachWithIndex
import java.math.BigDecimal
import java.util.*

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/21 10:51.
 */
class CalendarRecordActivity : WhiteActivity<EmptyPresenter>() {

    private val monthBudget = ListBookTool.getBudget().listBudget


    private val recordAdapter: RecordAdapter = RecordAdapter()
    private val calendarRecordAdapter: CalendarRecordAdapter = CalendarRecordAdapter()

    private lateinit var dayRecordMap: HashMap<Int, List<Record>>

    private var year: Int = TimeUtils.year
    private var month: Int = TimeUtils.month
    private var day: Int = TimeUtils.day

    private val deleteDialog: MsgDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = MsgDialog(this)
        dialog.setTitle(Utils.getString(R.string.delete_record_hint))
        dialog.setNegativeListener(null)
        dialog.message = Utils.getString(R.string.delete_record_affirm_hint)
        dialog
    }


    override fun getContentView(): View = Utils.inflate(R.layout.activity_calendar_record, this)

    override fun initView() {
        EventBusUtils.register(this, presenter)
        RecordTool.addOnDaoChangeListener(recordChangeListener, presenter)
        setActivityBackText(getString(R.string.bill))
        setActivityTitleText(getString(R.string.yyyy_MM, TimeUtils.year, TimeUtils.month))
        setNextText(getString(R.string.add))
        tv_date.text = getString(R.string.MM_dd, TimeUtils.month, TimeUtils.day)

        rv_calendar.layoutManager = GridLayoutManager(this, 7)
        rv_calendar.addItemDecoration(RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, Utils.getColor(R.color.divider_color_e4e5e4)))
        rv_calendar.clearItemAnimator()
        rv_calendar.adapter = calendarRecordAdapter

        rv_record.layoutManager = LinearLayoutManager(this)
        rv_record.addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e7)))
        rv_record.adapter = recordAdapter

        refreshCalendarData(TimeUtils.year, TimeUtils.month)
    }


    private fun refreshCalendarData(year: Int, month: Int) {
        this.year = year
        this.month = month
        setActivityTitleText(getString(R.string.yyyy_MM, year, month)) {
            ActivityTool.skipActivity<AnnualRecordActivity>()
        }
        DaoManager.getRecordDao().queryBuilder()
                .where(RecordDao.Properties.Year.eq(year))
                .where(RecordDao.Properties.Month.eq(month - 1))
                .where(RecordDao.Properties.IsDeleted.eq(0))
                .where(RecordDao.Properties.TypeId.ge(0))
                .orderDesc(RecordDao.Properties.TheDate)
                .orderDesc(RecordDao.Properties.RTime)
                .orderDesc(RecordDao.Properties.CreateTime)
                .rx().list()
                .observeOnMain()
                .bindToLifecycle(presenter)
                .subscribe { list ->

                    dayRecordMap = list.groupBy { it.day } as HashMap<Int, List<Record>>


                    val calendar = Calendar.getInstance(Locale.CHINA)
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month - 1)
                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                    val monthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                    val start = 2 - dayOfWeek
                    val calendarRecords = arrayListOf<CalendarRecordBean>()
                    var dayCheck = -1
                    for (i in start..monthDays) {
                        if (i > 0) {

                            val bean = CalendarRecordBean(year, month, i, 0.0, 0.0, false, false, false, false, false)

                            if (year == TimeUtils.year && month == TimeUtils.month && i == TimeUtils.day) {
                                dayCheck = i - start
                                bean.isChecked = true
                                bean.isToday = true
                            } else if (year >= TimeUtils.year && month >= TimeUtils.month && i > TimeUtils.day) {
                                bean.isFuture = true
                            }

                            val dayRecords = dayRecordMap[i]
                            if (dayRecords != null) {
                                var income = BigDecimal(0.0)
                                var expend = BigDecimal(0.0)
                                dayRecords.forEach {
                                    //判断收入与支出
                                    val type = it.recordType
                                    if (type.isIncoming == 0) {
                                        expend += BigDecimal(Math.abs(it.rateMoney))
                                    } else {
                                        income += BigDecimal(Math.abs(it.rateMoney))
                                    }
                                }
                                bean.income = income.toDouble()
                                bean.expend = expend.toDouble()

                                bean.isOverproof = monthBudget / monthDays.toFloat() < bean.expend

                            }
                            calendarRecords.add(bean)
                        } else {
                            calendarRecords.add(CalendarRecordBean(year, month, 0, 0.0, 0.0, false, false, false, false, true))
                        }
                    }

                    calendarRecordAdapter.setData(calendarRecords)
                    calendarRecordAdapter.refreshChecked(if (dayCheck != -1) dayCheck else (0 - start + 1))

                    refreshRecordData(month, if (month == TimeUtils.month) TimeUtils.day else 1)
                }

    }

    override fun initListener() {
        super.initListener()
        rv_record.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && recordAdapter.isOpen()) {
                    recordAdapter.close()
                }
            }
        })
        recordAdapter.setOnItemClickListener { _, _, itemInfo ->
            val activityInfo = ActivityInfoBean(Utils.getString(R.string.details), Utils.getString(R.string.calendar), itemInfo.recordType.color)
            ActivityTool.skipActivity<BillDetailsActivity>(itemInfo, activityInfo)
        }
        recordAdapter.setOnDeleteListener { _, itemInfo ->
            deleteDialog.setPositiveListener {
                RecordTool.delete(itemInfo)
            }.show()
        }
        calendarRecordAdapter.setOnItemCheckedListener { _, calendarMonth, day ->
            refreshRecordData(calendarMonth, day)
        }
        tv_black.setOnClickListener {
            onNext()
        }
    }


    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun selectData(bean: AnnalRecordMonthBean) {
        if (year != bean.year || month != bean.month) {
            refreshCalendarData(bean.year, bean.month)
        }
    }

    private fun refreshRecordData(month: Int, day: Int) {
        this.day = day
        tv_date.text = getString(R.string.MM_dd, month, day)

        val list = dayRecordMap[day]
        if (list != null && list.isNotEmpty()) {
            recordAdapter.setData(list as ArrayList<Record>)
            tv_black.visibility = View.GONE
            rv_record.visibility = View.VISIBLE
        } else {
            recordAdapter.clear()
            tv_black.visibility = View.VISIBLE
            rv_record.visibility = View.GONE
            val span = SpannableStringBuilder(getString(R.string.black_record_hint, year, month, day))
            span.inSpans(ForegroundColorSpan(Utils.getColor(R.color.AppThemeColor))) {
                append("\n点击加号记一笔!")
            }
            tv_black.text = span
        }
    }

    private val recordChangeListener = object : OnDaoChangeListener<Record> {
        override fun onInsertEntity(entity: Record) {
            if (entity.typeId > 0L && entity.year == year && entity.month == month - 1) {
                var dayList = dayRecordMap[entity.day]
                var dayIndex = 0
                if (dayList != null) {
                    val size = dayList.size - 1
                    dayIndex = dayList.size
                    for (i in 0..size) {
                        if (entity.rTime > dayList[i].rTime || (entity.rTime == dayList[i].rTime && entity.createTime > dayList[i].createTime)) {
                            dayIndex = i
                        }
                    }
                } else {
                    dayList = arrayListOf()
                    dayRecordMap[entity.day] = dayList
                }
                (dayList as ArrayList).add(dayIndex, entity)
                if (entity.day == day) {
                    if (tv_black.visibility == View.VISIBLE) {
                        tv_black.visibility = View.GONE
                        rv_record.visibility = View.VISIBLE
                    }
                    recordAdapter.insertData(dayIndex, entity)
                }

                val data = calendarRecordAdapter.data
                for (i in 0 until data.size) {
                    val record = data[i]
                    if (record.year == entity.year && record.month - 1 == entity.month && record.day == entity.day) {
                        if (entity.recordType.isIncoming == 0) {//支出
                            record.expend += entity.rateMoney
                            record.isOverproof = monthBudget / TimeUtils.getMonthMax(record.year, record.month) < record.expend
                        } else {
                            record.income += entity.rateMoney
                        }
                        calendarRecordAdapter.updateData(i, record)
                        break
                    }
                }
            }
        }

        override fun onUpdateEntity(oldEntity: Record, newEntity: Record) {
            if (oldEntity.theDate == newEntity.theDate) {
                val dayList = dayRecordMap[oldEntity.day]
                run {
                    dayList?.forEachWithIndex { i, record ->
                        if (record.uuid == oldEntity.uuid) {
                            (dayList as ArrayList)[i] = newEntity
                            recordAdapter.updateData(i, newEntity)
                            return@run
                        }
                    }
                }


                val data = calendarRecordAdapter.data
                run {
                    data.forEachWithIndex { i, it ->
                        if (it.day == oldEntity.day) {
                            if (oldEntity.recordType.isIncoming != newEntity.recordType.isIncoming) {
                                if (oldEntity.recordType.isIncoming == 0) {//之前为支出,现在为收入
                                    it.expend = it.expend - oldEntity.rateMoney//减去旧的支出
                                    it.income = it.income + newEntity.rateMoney//加上新的收入
                                } else {////之前为收入,现在为支出
                                    it.income = it.income - oldEntity.rateMoney//减去旧的收入
                                    it.expend = it.expend + newEntity.rateMoney//加上新的支出

                                }
                            } else {
                                if (oldEntity.recordType.isIncoming == 0) {//之前为支出
                                    it.expend = it.expend - oldEntity.rateMoney + newEntity.rateMoney
                                } else {//之前为收入
                                    it.income = it.income - oldEntity.rateMoney + newEntity.rateMoney
                                }
                            }
                            calendarRecordAdapter.updateData(i, it)
                            return@run
                        }
                    }
                }

            } else {
                onDeleteEntity(oldEntity)
                onInsertEntity(newEntity)
            }
        }

        override fun onDeleteEntity(entity: Record) {
            if (entity.typeId > 0L && entity.year == year && entity.month == month - 1) {
                val dayList = dayRecordMap[entity.day]
                if (dayList != null) {
                    val size = dayList.size - 1
                    for (i in 0..size) {
                        if (dayList[i].uuid == entity.uuid) {
                            recordAdapter.removeData(i)
                            (dayList as ArrayList).removeAt(i)
                            break
                        }
                    }
                }
                val data = calendarRecordAdapter.data
                for (i in 0 until data.size) {
                    val record = data[i]
                    if (record.year == entity.year && record.month - 1 == entity.month && record.day == entity.day) {
                        if (entity.recordType.isIncoming == 0) {//支出
                            record.expend -= entity.rateMoney
                            record.isOverproof = monthBudget / TimeUtils.getMonthMax(record.year, record.month) < record.expend
                        } else {
                            record.income -= entity.rateMoney
                        }
                        calendarRecordAdapter.updateData(i, record)
                        break
                    }
                }
                if (entity.day == day && recordAdapter.itemCount == 0 && tv_black.visibility != View.VISIBLE) {
                    tv_black.visibility = View.VISIBLE
                    val span = SpannableStringBuilder(getString(R.string.black_record_hint, year, month, day))
                    span.inSpans(ForegroundColorSpan(Utils.getColor(R.color.AppThemeColor))) {
                        append("\n点击加号记一笔!")
                    }
                    tv_black.text = span
                    rv_record.visibility = View.GONE
                }
            }
        }
    }


    override fun onNext() {
        super.onNext()
        ActivityTool.skipActivity<NewBillRecordActivity>()
        overridePendingTransition(R.anim.activity_in_bottom, 0)
    }

}