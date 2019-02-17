package com.cjj.keepaccounts.dagger

import com.cjj.keepaccounts.fragment.bill.BillFragment
import com.cjj.keepaccounts.fragment.bill.BillTransferAccountsFragment
import com.cjj.keepaccounts.fragment.bill.NewBillRecordFragment
import com.cjj.keepaccounts.fragment.property.PropertyFragment
import com.cjj.keepaccounts.fragment.statement.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author CJJ
 * Created by CJJ on 2018/8/22 14:32.
 */

@Suppress("unused")
@Module
abstract class AllFragmentsModule {

    @FragmentScoped
    @ContributesAndroidInjector()
    abstract fun contributeContrastFragmentInjector(): ContrastFragment


    @FragmentScoped
    @ContributesAndroidInjector()
    abstract fun contributeTrendFragmentInjector(): TrendFragment

    @FragmentScoped
    @ContributesAndroidInjector()
    abstract fun contributePropertyFragmentInjector(): PropertyFragment

    @FragmentScoped
    @ContributesAndroidInjector()
    abstract fun contributeClassifyFragmentInjector(): ClassifyFragment

    @FragmentScoped
    @ContributesAndroidInjector()
    abstract fun contributeMemberFragmentInjector(): MemberFragment

    @FragmentScoped
    @ContributesAndroidInjector()
    abstract fun contributeStatementFragmentInjector(): StatementFragment

    @FragmentScoped
    @ContributesAndroidInjector()
    abstract fun contributeBillFragmentInjector(): BillFragment

    @FragmentScoped
    @ContributesAndroidInjector()
    abstract fun contributeNewBillRecordFragmentInjector(): NewBillRecordFragment

    @FragmentScoped
    @ContributesAndroidInjector()
    abstract fun contributeBillTransferAccountsFragmentInjector(): BillTransferAccountsFragment

}