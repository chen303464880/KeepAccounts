package com.cjj.keepaccounts.dagger

import com.cjj.keepaccounts.application.MyApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule

/**
 * @author CJJ
 * Created by CJJ on 2018/8/22 14:30.
 */
@Component(modules = [AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    AllActivitiesModule::class,
    AllFragmentsModule::class])
interface MyAppComponent {
    fun inject(application: MyApplication)
}