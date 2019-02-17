package com.cjj.keepaccounts.activity.bill

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.text.SpannableStringBuilder
import android.view.KeyEvent
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.tool.EditBillRecordContentActivity
import com.cjj.keepaccounts.adapter.ViewPagerAdapter
import com.cjj.keepaccounts.base.TransparencyActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.*
import com.cjj.keepaccounts.dao.GlobalConfigTool
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.dialog.SelectAccountDialog
import com.cjj.keepaccounts.dialog.SelectMemberDialog
import com.cjj.keepaccounts.fragment.bill.BillTransferAccountsFragment
import com.cjj.keepaccounts.fragment.bill.NewBillRecordFragment
import com.cjj.keepaccounts.fragment.bill.RecordTypeFragment
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.*
import com.cjj.keepaccounts.view.CalculatorView
import com.cjj.keepaccounts.view.setRadioGroup
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_new_bill_record.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


/**
 * @author chenjunjie
 * Created by CJJ on 2018/02/28 09:35.
 * 添加单条消费或转账记录的页面
 */
class NewBillRecordActivity : TransparencyActivity<EmptyPresenter>(), HasSupportFragmentInjector {

    private lateinit var account: Account
    private lateinit var record: Record

    private lateinit var inputColorList: ColorStateList
    private lateinit var memberColorList: ColorStateList

    private val memberMap: HashMap<Long, RecordTag> = HashMap<Long, RecordTag>()

    private lateinit var fragmentList: ArrayList<RecordTypeFragment>

    private var isEdit = false
    private var year: Int = TimeUtils.year
    private var month: Int = TimeUtils.month
    private var day: Int = TimeUtils.day


    private val selectAccountDialog: SelectAccountDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = SelectAccountDialog(this, account.name)
        dialog.setOnAccountChangeListener {
            tv_account.text = it.name
            account = it
        }
        dialog
    }


    private val inAnimator: ObjectAnimator by lazy(LazyThreadSafetyMode.NONE) {
        val animator = ObjectAnimator.ofFloat(ll_input, "translationY", 0F, ll_input.height.toFloat())
        animator.duration = 250
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                setNavigationBarColor(Utils.getColor(R.color.bg_color_ebeceb))
            }
        })
        animator
    }

    private val outAnimator: ObjectAnimator by lazy(LazyThreadSafetyMode.NONE) {
        val animator = ObjectAnimator.ofFloat(ll_input, "translationY", ll_input.height.toFloat(), 0F)
        animator.duration = 250
        animator
    }


    private val selectMemberDialog: SelectMemberDialog by lazy(LazyThreadSafetyMode.NONE) {


        val tempMembers = HashMap<Long, RecordTag>()
        record.__setDaoSession(DaoManager.daoSession)
        record.members.forEach {
            val recordTag = it.member
            tempMembers[recordTag.uuid] = recordTag
        }
        val dialog = SelectMemberDialog(this, tempMembers)
        dialog.setOnAccountChangeListener {
            memberMap.clear()
            memberMap.putAll(it)
            if (memberMap.isEmpty()) {
                iv_member.imageTintList = null
            } else {
                iv_member.imageTintList = memberColorList
            }
        }
        dialog
    }

    private lateinit var billExpendFragment: NewBillRecordFragment
    private lateinit var billIncomeFragment: NewBillRecordFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWhiteStatusBar(true)
        setNavigationBarColor(Utils.getColor(R.color.bg_color_65605f))
        setContentView(R.layout.activity_new_bill_record)

        EventBusUtils.register(this, presenter)


        val tempRecord: Record? = intent.getParcelableExtra("info")
        if (tempRecord != null) {
            record = tempRecord
            year = record.year
            month = record.month + 1
            day = record.day
            isEdit = true
        } else {
            record = Record(System.currentTimeMillis())
            record.setDate(year, month - 1, day)
            record.theHour = TimeUtils.hour
            isEdit = false
        }
        @Suppress("DEPRECATION")
        inputColorList = resources.getColorStateList(R.color.selector_new_bill_icon_color)
        @Suppress("DEPRECATION")
        memberColorList = resources.getColorStateList(R.color.selector_new_bill_icon_color)

        frameLayout2.layoutParams.height += Utils.getTitleHeight()
        frameLayout2.requestLayout()
        frameLayout2.setPadding(0, Utils.getTitleHeight(), 0, 0)
        billExpendFragment = NewBillRecordFragment()
        val bundleExpend = Bundle()
        bundleExpend.putInt("type", 0)
        bundleExpend.putParcelable("info", record)
        billExpendFragment.arguments = bundleExpend

        billIncomeFragment = NewBillRecordFragment()
        val bundleIncome = Bundle()
        bundleIncome.putInt("type", 1)
        bundleIncome.putParcelable("info", record)
        billIncomeFragment.arguments = bundleIncome

        val billTransferAccountsFragment = BillTransferAccountsFragment()
        val transferAccounts = Bundle()
        transferAccounts.putParcelable("info", if (isEdit) record else null)
        billTransferAccountsFragment.arguments = transferAccounts
        fragmentList = arrayListOf(
                billIncomeFragment,
                billExpendFragment,
                billTransferAccountsFragment
        )
        vp_new_bill.offscreenPageLimit = fragmentList.size
        vp_new_bill.adapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
        vp_new_bill.setRadioGroup(rg_bill)
        vp_new_bill.currentItem = 1

        account = if (isEdit) {
            record.account
        } else {

            getInfo("account")
                    ?: GlobalConfigTool.getLastAccount()
                    ?: DaoManager.getAccountDao().queryBuilder()
                            .where(AccountDao.Properties.IsDeleted.eq(0))
                            .where(AccountDao.Properties.TypeId.notEq(7))
                            .where(AccountDao.Properties.TypeId.notEq(8))
                            .orderAsc(AccountDao.Properties.OrderIndex)
                            .list().first()
        }
        tv_account.text = account.name
        tv_time.text = getString(R.string.MM_dd, (record.month + 1), record.day)


        //有备注则显示彩色图标
        if (record.content.isNullOrBlank()) {
            iv_input.imageTintList = null
        } else {
            iv_input.imageTintList = inputColorList
        }

        //有成员则显示彩色图标
        record.__setDaoSession(DaoManager.daoSession)
        if (isEdit && record.members.isNotEmpty()) {
            iv_member.imageTintList = memberColorList
        } else {
            iv_member.imageTintList = null
        }


        if (isEdit) {
            val money = record.rateMoney
            if (money != 0.0) {
                calculator_view.setResult(money.toMoney())
            }

            if (record.typeId == -1L) {
                vp_new_bill.currentItem = 2
                if (money != 0.0) {
                    calculator_view.setResult(Math.abs(money).toMoney())
                }
            } else {
                if (record.recordType.isIncoming == 1) {
                    vp_new_bill.currentItem = 0
                } else {
                    vp_new_bill.currentItem = 1
                }

            }


        }

        initListener()
    }


    private fun initListener() {

        iv_close.setOnClickListener {
            ActivityAnimationUtil.circularClose(this, it)
        }

        iv_member.setOnClickListener {
            selectMemberDialog.show()
        }
        tv_account.setOnClickListener {
            selectAccountDialog.show()
        }
        iv_input.setOnClickListener {
            ActivityTool.skipActivity<EditBillRecordContentActivity>(record)
            overridePendingTransition(R.anim.activity_in_alpha, 0)
        }
        tv_time.setOnClickListener {
            //            ActivityOptions.makeClipRevealAnimation()
            ActivityTool.skipActivity<CalendarSelectActivity>(CalendarDayBean(year, month, day, true, true, true, true, true))
        }

        vp_new_bill.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                if (p0 == 2) {
                    tv_account.visibility = View.GONE
                    iv_member.visibility = View.GONE
                    iv_input.visibility = View.GONE
                    if (ll_input.translationY != 0F) {
                        outAnimator.start()
                        setNavigationBarColor(Utils.getColor(R.color.text_color_655f5f))
                    }
                } else {
                    tv_account.visibility = View.VISIBLE
                    iv_member.visibility = View.VISIBLE
                    iv_input.visibility = View.VISIBLE
                }
            }
        })
        calculator_view.listener = object : CalculatorView.OnCalculatorListener {
            override fun calculatorChange(equation: String, nums: ArrayList<String>, result: SpannableStringBuilder) {
                EventBus.getDefault().post(CalculatorEventBean(equation, nums, result))
            }


            override fun result(result: SpannableStringBuilder) {
                EventBus.getDefault().post(CalculatorEventBean("", arrayListOf(), result))
            }

            override fun finish(result: SpannableStringBuilder) {
                val money = result.toString().toDouble()
                if (money == 0.0) {
                    ToastUtils.shortToast("金额不可以为0")
                    return
                }
                if (money < 0.0) {
                    ToastUtils.shortToast("金额不可以小于0")
                    return
                }
                val typeId = fragmentList[vp_new_bill.currentItem].typeId
                if (isEdit) {
                    if (typeId == -1L) {
                        val accountsFragment = fragmentList.last() as BillTransferAccountsFragment
                        val expendAccount = accountsFragment.expendAccount
                        val incomeAccount = accountsFragment.incomeAccount
                        if (expendAccount == null) {
                            ToastUtils.shortToast("请选择转出账户")
                            return
                        }
                        if (incomeAccount == null) {
                            ToastUtils.shortToast("请选择入出账户")
                            return
                        }
                        if (expendAccount.uuid == incomeAccount.uuid) {
                            ToastUtils.shortToast("转入与转出账户不能相同")
                            return
                        }
                        if (record.typeId != -1L) {
                            insertTransferAccounts(money)
                        } else {
                            updateTransferAccounts(money)
                        }
                    } else {
                        if (record.typeId != -1L) {
                            update(money, typeId)
                        } else {

                            //删除转账
                            RecordTool.delete(record)
                            insert(money, typeId)
                        }
                    }
                } else {
                    if (typeId == -1L) {
                        val accountsFragment = fragmentList.last() as BillTransferAccountsFragment
                        val expendAccount = accountsFragment.expendAccount
                        val incomeAccount = accountsFragment.incomeAccount
                        if (expendAccount == null) {
                            ToastUtils.shortToast("请选择转出账户")
                            return
                        }
                        if (incomeAccount == null) {
                            ToastUtils.shortToast("请选择入出账户")
                            return
                        }
                        if (expendAccount.uuid == incomeAccount.uuid) {
                            ToastUtils.shortToast("转入与转出账户不能相同")
                            return
                        }
                        insertTransferAccounts(money)
                    } else {
                        insert(money, typeId)
                    }
                }
                if (typeId != -1L) {
                    GlobalConfigTool.updateLastAccountId(account.uuid)
                }
                finish()
                overridePendingTransition(0, R.anim.bill_record_over_out)

            }
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (!billExpendFragment.onKeyDown(keyCode, event)) {
            if (!billIncomeFragment.onKeyDown(keyCode, event)) {
                return super.onKeyDown(keyCode, event)
            }
        }
        return true
    }


    private fun update(money: Double, typeId: Long) {
        val newRecord = record.clone()
        newRecord.typeId = typeId
        newRecord.rateMoney = money
        newRecord.accountId = account.uuid
        newRecord.setDate(year, month - 1, day)
        if (memberMap.size > 0) {
            val newMembers = arrayListOf<RecordToTag>()
            var uuidLong = System.currentTimeMillis()
            memberMap.forEach { (_, u) ->
                //新建记录
                val recordToTag = RecordToTag()
                recordToTag.deviceId = ""
                recordToTag.isDeleted = 0
                recordToTag.mTime = TimeUtils.timeOfSecond
                recordToTag.uuid = uuidLong++
                recordToTag.createTime = 0L
                recordToTag.listId = 1
                recordToTag.tagId = u.uuid
                recordToTag.recordId = record.uuid
                recordToTag.userId = "0"
                newMembers.add(recordToTag)
            }
            newRecord.members = newMembers
        }
        RecordTool.update(record, newRecord, true)
    }

    private fun updateTransferAccounts(money: Double) {

        val accountsFragment = fragmentList.last() as BillTransferAccountsFragment
        val expendAccount = accountsFragment.expendAccount
        val incomeAccount = accountsFragment.incomeAccount

        if (expendAccount!!.uuid == incomeAccount!!.uuid) {
            ToastUtils.shortToast("转入与转出账户不能相同")
            return
        }

        val oldIncomeRecord: Record
        val oldExpendRecord: Record
        if (record.rateMoney <= 0.0) {
            oldIncomeRecord = DaoManager.getRecordDao().queryBuilder()
                    .where(RecordDao.Properties.ActionId.eq(record.actionId))
                    .where(RecordDao.Properties.AccountId.notEq(record.accountId))
                    .unique()
            oldExpendRecord = record

            oldIncomeRecord.content = record.content
        } else {
            oldIncomeRecord = record
            oldExpendRecord = DaoManager.getRecordDao().queryBuilder()
                    .where(RecordDao.Properties.ActionId.eq(record.actionId))
                    .where(RecordDao.Properties.AccountId.notEq(record.accountId))
                    .unique()
            oldExpendRecord.content = record.content
        }


        val newIncomeRecord = oldIncomeRecord.clone()
        newIncomeRecord.rateMoney = money
        newIncomeRecord.accountId = incomeAccount.uuid
        newIncomeRecord.setDate(year, month - 1, day)
        RecordTool.update(oldIncomeRecord, newIncomeRecord)


        val newExpendRecord = oldExpendRecord.clone()
        newExpendRecord.rateMoney = -money
        newExpendRecord.accountId = expendAccount.uuid
        newExpendRecord.setDate(year, month - 1, day)
        RecordTool.update(oldExpendRecord, newExpendRecord)


    }

    private fun insert(money: Double, typeId: Long) {

        val newRecord = Record(System.currentTimeMillis())

        newRecord.accountId = account.uuid
        newRecord.rateMoney = money
        newRecord.typeId = typeId
        newRecord.setDate(year, month - 1, day)

        if (memberMap.size >= 0) {
            val members = arrayListOf<RecordToTag>()
            var uuidLong = System.currentTimeMillis()
            memberMap.forEach { (_, u) ->
                //新建记录
                val recordToTag = RecordToTag()
                recordToTag.deviceId = ""
                recordToTag.isDeleted = 0
                recordToTag.mTime = TimeUtils.timeOfSecond
                recordToTag.uuid = uuidLong++
                recordToTag.createTime = 0L
                recordToTag.listId = 1
                recordToTag.tagId = u.uuid
                recordToTag.recordId = newRecord.uuid
                recordToTag.userId = "0"
                members.add(recordToTag)
            }
            newRecord.members = members

        }
        RecordTool.insert(newRecord)
    }

    private fun insertTransferAccounts(money: Double) {
        val accountsFragment = fragmentList.last() as BillTransferAccountsFragment
        val expendAccount = accountsFragment.expendAccount
        val incomeAccount = accountsFragment.incomeAccount
        if (expendAccount == null) {
            ToastUtils.shortToast("请选择转出账户")
            return
        }
        if (incomeAccount == null) {
            ToastUtils.shortToast("请选择入出账户")
            return
        }

        if (isEdit) {
            val expendRecord = Record(System.currentTimeMillis())

            expendRecord.content = record.content
            expendRecord.setDate(year, month - 1, day)
            expendRecord.theHour = TimeUtils.hour

            expendRecord.rateMoney = -money


            insertTransferAccounts(expendRecord, expendAccount, incomeAccount)

            RecordTool.delete(record)

        } else {
            record.rateMoney = -money
            insertTransferAccounts(record, expendAccount, incomeAccount)
        }


    }

    private fun insertTransferAccounts(expendRecord: Record, expendAccount: Account, incomeAccount: Account) {

        //创建转账唯一id
        val actionId = System.currentTimeMillis()

        expendRecord.actionId = actionId
        expendRecord.accountId = expendAccount.uuid
        expendRecord.content = record.content
        expendRecord.typeId = -1L

        val incomeRecord = Record(expendRecord.uuid + 1L)

        incomeRecord.setDate(year, month - 1, day)
        incomeRecord.rateMoney = -expendRecord.rateMoney

        incomeRecord.creditID = 0
        incomeRecord.theHour = TimeUtils.hour
        incomeRecord.actionId = actionId
        incomeRecord.createTime = System.currentTimeMillis()
        incomeRecord.rTime = System.currentTimeMillis()

        incomeRecord.typeId = -1L
        incomeRecord.accountId = incomeAccount.uuid


        RecordTool.insert(incomeRecord)
        RecordTool.insert(expendRecord)
    }


    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(str: String) {
        record.content = str
        if (str.isEmpty()) {
            iv_input.imageTintList = null
        } else {
            iv_input.imageTintList = inputColorList
        }
    }

    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showKeyboard(isShow: BooleanEvent) {
//        Log.i("TAG", "收到消息")
        if (isShow.b) {
            if (ll_input.translationY == 0F) {
                inAnimator.start()
            }
        } else {
            if (ll_input.translationY != 0F) {
                outAnimator.start()
                setNavigationBarColor(Utils.getColor(R.color.bg_color_65605f))
            }
//            ll_input.visibility = View.GONE
        }
    }

    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun setDate(dayBean: CalendarDayBean) {
        year = dayBean.year
        month = dayBean.month
        day = dayBean.day
        tv_time.text = getString(R.string.MM_dd, month, day)
    }


    override fun isSupportSwipeBack(): Boolean = false
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, R.anim.bill_record_out)
    }


    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }
}
