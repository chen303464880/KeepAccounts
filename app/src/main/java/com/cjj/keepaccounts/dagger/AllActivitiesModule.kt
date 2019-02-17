package com.cjj.keepaccounts.dagger

import com.cjj.keepaccounts.MainActivity
import com.cjj.keepaccounts.activity.account.*
import com.cjj.keepaccounts.activity.bill.*
import com.cjj.keepaccounts.activity.bill.budget.*
import com.cjj.keepaccounts.activity.setting.LoginActivity
import com.cjj.keepaccounts.activity.setting.SettingActivity
import com.cjj.keepaccounts.activity.statement.SelectDateIntervalActivity
import com.cjj.keepaccounts.activity.statement.SettingShowClassifyActivity
import com.cjj.keepaccounts.activity.tool.EditBillRecordContentActivity
import com.cjj.keepaccounts.activity.tool.EditTextActivity
import com.cjj.keepaccounts.activity.tool.SelectColorActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author CJJ
 * Created by CJJ on 2018/8/22 14:32.
 */

@Suppress("unused")
@Module
abstract class AllActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeAccountDetailsActivityInjector(): AccountDetailsActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeAddAccountActivityInjector(): AddAccountActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeAddBorrowMoneyActivityInjector(): AddBorrowMoneyActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeBillDetailsActivityInjector(): BillDetailsActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeBorrowMoneyDetailsActivityInjector(): BorrowMoneyDetailsActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeBorrowMoneyRecordActivityInjector(): BorrowMoneyRecordActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeReceiptBorrowMoneyActivityInjector(): ReceiptBorrowMoneyActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeSelectAccountTypeActivityInjector(): SelectAccountTypeActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeSelectBankActivityInjector(): SelectBankActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeAddBudgetChildClassifyActivityInjector(): AddBudgetChildClassifyActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeBudgetActivityInjector(): BudgetActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeBudgetClassifyEditActivityInjector(): BudgetClassifyEditActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeBudgetSettingActivityInjector(): BudgetSettingActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeEditChildClassifyBudgetActivityInjector(): EditChildClassifyBudgetActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeAnnualRecordActivityInjector(): AnnualRecordActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeCalendarRecordActivityInjector(): CalendarRecordActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeCalendarSelectActivityInjector(): CalendarSelectActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeClassifyDetailsActivityInjector(): ClassifyDetailsActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeEditClassifyActivityInjector(): EditClassifyActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeEditClassifyDetailsActivityInjector(): EditClassifyDetailsActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeEditMemberActivityInjector(): EditMemberActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeNewBillRecordActivityInjector(): NewBillRecordActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeSearchRecordActivityInjector(): SearchRecordActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeLoginActivityInjector(): LoginActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeSettingActivityInjector(): SettingActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeSelectDateIntervalActivityInjector(): SelectDateIntervalActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeSettingShowClassifyActivityInjector(): SettingShowClassifyActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeEditBillRecordContentActivityInjector(): EditBillRecordContentActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeEditTextActivityInjector(): EditTextActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeSelectColorActivityInjector(): SelectColorActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeMainActivityInjector(): MainActivity
}