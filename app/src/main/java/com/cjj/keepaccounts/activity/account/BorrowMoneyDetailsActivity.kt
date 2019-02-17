package com.cjj.keepaccounts.activity.account

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.annotation.ColorInt
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.BorrowMoneyDetailsAdapter
import com.cjj.keepaccounts.base.TitleActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.Account
import com.cjj.keepaccounts.bean.ActivityInfoBean
import com.cjj.keepaccounts.bean.Credit
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.dao.CreditTool
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.dialog.MsgDialog
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.LogUtils
import com.cjj.keepaccounts.utils.RecyclerSpace
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.BorrowMoneyDetailsHeadView
import com.cjj.keepaccounts.view.clearItemAnimator
import kotlinx.android.synthetic.main.activity_borrow_money_details.*
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * @author CJJ
 *
 */
class BorrowMoneyDetailsActivity : TitleActivity<EmptyPresenter>() {

    private var creditInfo: Credit by extra()
    private lateinit var records: List<Record>
    private  var account by extra<Account>("account")

    private var isMoney = false

    private lateinit var headView: BorrowMoneyDetailsHeadView
    private lateinit var detailAdapter: BorrowMoneyDetailsAdapter

    private val color = Color.parseColor("#9FFFFFFF")
    /**
     * 剩余欠款
     */
    private var debt = 0.0
    /**
     * 利息
     */
    private var interest = 0.0

    private var allDebt = 0.0

    /**
     * 是否借别人的钱
     */
    private var isLend = false

    private val deleteDialog: MsgDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = MsgDialog(this)
        dialog.setTitle(Utils.getString(R.string.delete_record_hint))
        dialog.setNegativeListener(null)
        dialog.message = Utils.getString(R.string.delete_record_affirm_hint)
        dialog
    }

    override fun getContentView(): View = Utils.inflate(R.layout.activity_borrow_money_details, this)

    override fun initView() {
        RecordTool.addOnDaoChangeListener(daoChangeListener, presenter)
        CreditTool.addOnDaoChangeListener(creditDaoChangeListener, presenter)
        headView = BorrowMoneyDetailsHeadView(this)
        headView.backgroundColor = mPageColor
        records = creditInfo.repayRecord
        isLend = creditInfo.isLend
        allDebt = creditInfo.money
        debt = allDebt - creditInfo.repayMoney
        interest = creditInfo.interestMoney
        isMoney = debt == 0.0

        headView.apply {
            nameKey = getString(if (isLend) R.string.borrow_money_out_name else R.string.borrow_money_in_name)
            timeKey = getString(if (isLend) R.string.borrow_money_out_time else R.string.borrow_money_in_time)
            accountKey = getString(if (isLend) R.string.borrow_money_out_account else R.string.borrow_money_in_account)
        }


        val allDebtStr = SpannableString(getString(R.string.xx_all_debt, Math.abs(allDebt)))
        allDebtStr.setSpan(ForegroundColorSpan(color), allDebtStr.length - 3, allDebtStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        allDebtStr.setSpan(AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12)), allDebtStr.length - 3, allDebtStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        headView.allDebt = allDebtStr

        val debtStr = SpannableString(getString(R.string.xx_debt, Math.abs(debt)))
        debtStr.setSpan(ForegroundColorSpan(color), debtStr.length - 4, debtStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        debtStr.setSpan(AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12)), debtStr.length - 4, debtStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        headView.debt = debtStr

        val interestStr = SpannableString(getString(if (isLend) R.string.xx_interest_in else R.string.xx_interest_out, Math.abs(interest)))
        interestStr.setSpan(ForegroundColorSpan(color), interestStr.length - 4, interestStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        interestStr.setSpan(AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12)), interestStr.length - 4, interestStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        headView.interest = interestStr



        headView.name = creditInfo.dcUName
        headView.time = TimeUtils.longTurnTime(creditInfo.mTime)
        headView.account = creditInfo.borrowRecord.account.name
        headView.content = creditInfo.content


        lv_borrow_money.layoutManager = LinearLayoutManager(this)


        if (records.isEmpty()) {
            //显示空白信息
            headView.tvDetailsKong.visibility = View.VISIBLE
        } else {
            headView.tvDetailsKong.visibility = View.GONE
            //添加分割线
            lv_borrow_money.addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e4e5e4)))
        }
        detailAdapter = BorrowMoneyDetailsAdapter()
        detailAdapter.setData(records)
        detailAdapter.addHeaderView(headView.headView)
        lv_borrow_money.adapter = detailAdapter
        lv_borrow_money.clearItemAnimator()

        tv_collect_money.background = getBtnBg(mPageColor)
        if (isMoney) {
            ll_collect_money.visibility = View.GONE
        } else {
            tv_collect_money.text = getString(if (isLend) R.string.collect_money else R.string.refund_money)
        }
    }

    override fun initListener() {
        super.initListener()
        tv_collect_money.setOnClickListener {
            val activityInfoBean = ActivityInfoBean(Utils.getString(if (isLend) R.string.collect_money else R.string.refund_money), Utils.getString(if (isLend) R.string.loans else R.string.borrow), next = Utils.getString(R.string.accomplish))
            ActivityTool.skipActivity<ReceiptBorrowMoneyActivity>("borrowAccount" to account, Pair("credit", creditInfo), activityInfo = activityInfoBean)
        }
        lv_borrow_money.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && detailAdapter.isOpen()) {
                    detailAdapter.close()
                }
            }
        })

        detailAdapter.setOnItemClickListener { _, _, itemInfo ->
            val activityInfoBean = ActivityInfoBean(Utils.getString(if (isLend) R.string.collect_money else R.string.refund_money), Utils.getString(if (isLend) R.string.loans else R.string.borrow), next = Utils.getString(R.string.accomplish))
            ActivityTool.skipActivity<ReceiptBorrowMoneyActivity>(Pair("borrowAccount", account), Pair("credit", creditInfo), Pair("record", itemInfo), activityInfo = activityInfoBean)
        }
        detailAdapter.setOnDeleteListener { _, itemInfo ->
            deleteDialog.setPositiveListener {
                RecordTool.delete(itemInfo)
            }.show()
        }
    }

    override fun onNext() {
        super.onNext()
//        ToastUtils.shortToast("删除")
        val activityInfoBean = ActivityInfoBean(getString(R.string.new_text), getActivityTitleText(), next = getString(R.string.ok))

        ActivityTool.skipActivity<AddBorrowMoneyActivity>(Pair("credit", creditInfo), Pair("info", account), activityInfo = activityInfoBean)
    }


    private val daoChangeListener = object : OnDaoChangeListener<Record> {
        override fun onInsertEntity(entity: Record) {
            if (((entity.typeId == creditInfo.repayPropertyId) ||
                            (entity.typeId == creditInfo.interestId)) && entity.rateMoney != 0.0) {

                val data = detailAdapter.data
                var index = 0
                run {
                    data.forEachWithIndex { i, record ->
                        if (record.theDate <= entity.theDate && record.uuid < entity.uuid) {
                            index = i
                        }
                    }
                }
                detailAdapter.insertData(index, entity)
                creditInfo.repayRecord.add(index, entity)
                if (entity.typeId == creditInfo.interestId) {
                    refreshInterest(Math.abs(entity.rateMoney))
                } else {
                    refreshDebt(Math.abs(entity.rateMoney))
                }
                if (detailAdapter.data.size != 0) {
                    headView.tvDetailsKong.visibility = View.GONE
                }
            }
        }

        override fun onUpdateEntity(oldEntity: Record, newEntity: Record) {
            if ((newEntity.typeId == creditInfo.interestId) || (newEntity.typeId == creditInfo.repayPropertyId)) {
                if (newEntity.rateMoney != 0.0 || (newEntity.typeId == creditInfo.repayPropertyId)) {
                    if (oldEntity.theDate == newEntity.theDate) {
                        detailAdapter.data.forEachWithIndex { i, record ->
                            if (record.uuid == newEntity.uuid) {
                                creditInfo.repayRecord[i] = newEntity
                                detailAdapter.updateData(i, newEntity)
                                val money = newEntity.rateMoney - oldEntity.rateMoney
                                refreshDebt(money)

                                return
                            }
                        }
                    } else {
                        onDeleteEntity(oldEntity)
                        onInsertEntity(newEntity)
                    }
                } else {
                    onDeleteEntity(newEntity)
                }
            }
        }

        override fun onDeleteEntity(entity: Record) {
            if ((entity.typeId == creditInfo.repayPropertyId) ||
                    (entity.typeId == creditInfo.interestId)) {
                detailAdapter.data.forEachWithIndex { i, record ->
                    if (record.uuid == entity.uuid) {
                        detailAdapter.removeData(i)
                        creditInfo.repayRecord.removeAt(i)
                        if (entity.typeId == creditInfo.interestId) {
                            refreshInterest(-Math.abs(entity.rateMoney))
                        } else {
                            refreshDebt(-Math.abs(entity.rateMoney))
                        }
                        if (detailAdapter.data.size == 0) {
                            headView.tvDetailsKong.visibility = View.VISIBLE
                        }
                        return
                    }
                }

            }
        }
    }

    private fun refreshInterest(money: Double) {
        interest -= money
        creditInfo.interestMoney -= money
        val interestStr = SpannableString(getString(if (isLend) R.string.xx_interest_in else R.string.xx_interest_out, Math.abs(interest)))
        interestStr.setSpan(ForegroundColorSpan(color), interestStr.length - 4, interestStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        interestStr.setSpan(AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12)), interestStr.length - 4, interestStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        headView.interest = interestStr
    }

    private fun refreshDebt(money: Double) {
        debt -= money
        creditInfo.repayMoney += money
        val debtStr = SpannableString(getString(R.string.xx_debt, Math.abs(debt)))
        debtStr.setSpan(ForegroundColorSpan(color), debtStr.length - 4, debtStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        debtStr.setSpan(AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12)), debtStr.length - 4, debtStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        headView.debt = debtStr
        isMoney = debt == 0.0
        if (isMoney) {
            ll_collect_money.visibility = View.GONE
        } else {
            ll_collect_money.visibility = View.VISIBLE
        }
    }


    private val creditDaoChangeListener = object : OnDaoChangeListener<Credit> {
        override fun onInsertEntity(entity: Credit) {

        }

        override fun onUpdateEntity(oldEntity: Credit, newEntity: Credit) {
            if (creditInfo.dcUName != newEntity.dcUName) {
                headView.name = newEntity.dcUName
            }
            if (creditInfo.mTime != newEntity.mTime) {
                headView.time = TimeUtils.longTurnTime(newEntity.mTime)
            }
            newEntity.__setDaoSession(DaoManager.daoSession)
            if (creditInfo.records.first().accountId != newEntity.records.first().accountId) {
                headView.account = newEntity.records.first().account.name
            }
            if (creditInfo.money != newEntity.money) {
                val allDebtStr = SpannableString(getString(R.string.xx_all_debt, Math.abs(newEntity.money)))
                allDebtStr.setSpan(ForegroundColorSpan(color), allDebtStr.length - 3, allDebtStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                allDebtStr.setSpan(AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12)), allDebtStr.length - 3, allDebtStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                headView.allDebt = allDebtStr


                allDebt = newEntity.money
                LogUtils.i("allDebt:$allDebt")
                isMoney = newEntity.money == newEntity.repayMoney
                debt = newEntity.money - newEntity.repayMoney
                val debtStr = SpannableString(getString(R.string.xx_debt, Math.abs(debt)))
                debtStr.setSpan(ForegroundColorSpan(color), debtStr.length - 4, debtStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                debtStr.setSpan(AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12)), debtStr.length - 4, debtStr.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                headView.debt = debtStr

            }

            creditInfo = newEntity
        }

        override fun onDeleteEntity(entity: Credit) {

        }
    }


    private fun getBtnBg(@ColorInt colorInt: Int): Drawable {
        val drawable = GradientDrawable()
        drawable.setColor(colorInt)
        drawable.cornerRadius = Utils.dip2px(5F).toFloat()
        return drawable
    }

}
