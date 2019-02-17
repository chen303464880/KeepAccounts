package com.cjj.keepaccounts.base

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.inSpans
import butterknife.OnClick
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.bill.ClassifyDetailsActivity
import com.cjj.keepaccounts.activity.bill.NewBillRecordActivity
import com.cjj.keepaccounts.activity.statement.SelectDateIntervalActivity
import com.cjj.keepaccounts.adapter.BillClassifyAdapter
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.ActivityInfoBean
import com.cjj.keepaccounts.bean.DateIntervalBean
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.bean.StatementDetailBean
import com.cjj.keepaccounts.bean.event.SyncOverEvent
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.utils.*
import com.cjj.keepaccounts.view.PieChartHeadView
import com.cjj.keepaccounts.view.PieChartView
import kotlinx.android.synthetic.main.fragment_classify.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Subscription
import rx.functions.Action1
import java.math.BigDecimal
import java.util.*

/**
 * @author CJJ
 * Created by CJJ on 2018/5/16 10:08.
 */
abstract class BaseTimeFragment : BaseFragment<EmptyPresenter>() {
    private var startYear = TimeUtils.year
    private var startMonth = TimeUtils.month - 1
    private var startDay = 1
    private var endYear = TimeUtils.year
    private var endMonth = TimeUtils.month - 1
    private var endDay = TimeUtils.getMonthMax(endYear, endMonth)
    protected var startDate = startYear * 10000 + startMonth * 100 + startDay
    protected var endDate = endYear * 10000 + endMonth * 100 + endDay
    protected var isIncoming = 0


    protected lateinit var adapter: BillClassifyAdapter
    private lateinit var headView: PieChartHeadView

    protected var subscribe: Subscription? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_classify, container, false)
    }

    override fun initView() {
        EventBusUtils.register(this, presenter)
        RecordTool.addOnDaoChangeListener(daoChangeListener, presenter)
        rv_classify.layoutManager = LinearLayoutManager(context!!)
        adapter = BillClassifyAdapter()
        headView = PieChartHeadView(context!!)
        adapter.addHeaderView(headView.headView)

        rv_classify.adapter = adapter
        rv_classify.addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e7)))
        setTimeInterval()

        val span = SpannableStringBuilder("报表空空的")
        span.inSpans(ForegroundColorSpan(Utils.getColor(R.color.AppThemeColor))) {
            append("\n点击\"这里\"添加一些新的记录")
        }
        tv_blank.text = span
    }


    override fun initListener() {
        super.initListener()
        headView.setOnChangeListener {
            isIncoming = if (isIncoming == 0) {
                1
            } else {
                0
            }
            Utils.unsubscribe(subscribe)
            subscribe = getData()
        }
        adapter.setOnItemClickListener { _, _, itemInfo ->
            val activityInfo = ActivityInfoBean(getString(R.string.xx_classify_details, itemInfo.dsec), getString(R.string.statement), itemInfo.color)
            ActivityTool.skipActivity<ClassifyDetailsActivity>(itemInfo, activityInfo = activityInfo)
        }

        tv_time_interval.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("startDate", startDate)
            bundle.putInt("endDate", endDate)
            ActivityTool.skipActivity<SelectDateIntervalActivity>(bundle)
        }

        tv_blank.setOnClickListener {
            ActivityTool.skipActivity<NewBillRecordActivity>()
        }
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
        setTimeInterval()
    }


    private fun setTimeInterval() {
        startDate = startYear * 10000 + startMonth * 100 + startDay
        endDate = endYear * 10000 + endMonth * 100 + endDay
        if (startYear != endYear) {
            tv_time_interval.text = Utils.context.getString(R.string.year_time_interval,
                    startYear, startMonth + 1, startDay,
                    endYear, endMonth + 1, endDay)
        } else {
            if (startMonth != endMonth || startYear != TimeUtils.year) {
                tv_time_interval.text = Utils.context.getString(R.string.month_time_interval,
                        startYear, startMonth + 1, startDay,
                        endMonth + 1, endDay)
            } else {
                tv_time_interval.text = Utils.context.getString(R.string.day_time_interval,
                        startMonth + 1, startDay,
                        endDay)
            }
        }
        if (startYear == endYear && startMonth == endMonth && startDay == 1 && endDay == TimeUtils.getMonthMax(endYear, endMonth)) {
            iv_right.visibility = View.VISIBLE
            iv_left.visibility = View.VISIBLE
        } else {
            iv_right.visibility = View.GONE
            iv_left.visibility = View.GONE
        }

        Utils.unsubscribe(subscribe)
        subscribe = getData()
    }

    abstract fun getData(): Subscription?

    private val spannable = AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size18))

    protected val onNext = Action1<Pair<BigDecimal, ArrayList<StatementDetailBean>>> {
        tv_blank.visibility = View.GONE
        adapter.setData(it.second)
        adapter.notifyDataSetChanged()
        val span = SpannableStringBuilder(Utils.getString(if (isIncoming == 0) R.string.total_expend else R.string.total_income))
        span.inSpans(spannable) {
            span.append("\n")
            span.append(it.first.toDouble().toMoney())
        }
        headView.money = span

        val info = ArrayList<PieChartView.PieChartItem>()
        it.second.forEach { item ->
            info.add(PieChartView.PieChartItem(item.ratio, item.color))
        }
        headView.pieChartInfo = info

    }


    protected val onError = Action1<Throwable> {
        tv_blank.visibility = View.VISIBLE
    }


    private val daoChangeListener = object : OnDaoChangeListener<Record> {
        override fun onInsertEntity(entity: Record) {
            if (entity.theDate in startDate..endDate && entity.recordType.isIncoming == isIncoming) {
                Utils.unsubscribe(subscribe)
                subscribe = getData()
            }
        }

        override fun onUpdateEntity(oldEntity: Record, newEntity: Record) {
            if ((oldEntity.theDate in startDate..endDate && oldEntity.recordType.isIncoming == isIncoming)
                    || (newEntity.theDate in startDate..endDate && newEntity.recordType.isIncoming == isIncoming)) {
                Utils.unsubscribe(subscribe)
                subscribe = getData()
            }
        }

        override fun onDeleteEntity(entity: Record) {
            if (entity.theDate in startDate..endDate && entity.recordType.isIncoming == isIncoming) {
                Utils.unsubscribe(subscribe)
                subscribe = getData()
            }
        }
    }

    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun setDateInterval(bean: DateIntervalBean) {
        startYear = bean.startYear
        startMonth = bean.startMonth
        startDay = bean.startDay
        endYear = bean.endYear
        endMonth = bean.endMonth
        endDay = bean.endDay
        setTimeInterval()
    }

    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun syncOver(@Suppress("UNUSED_PARAMETER") event: SyncOverEvent) {
        Utils.unsubscribe(subscribe)
        subscribe = getData()
    }

    override fun onDestroy() {
        super.onDestroy()
        Utils.unsubscribe(subscribe)
    }
}