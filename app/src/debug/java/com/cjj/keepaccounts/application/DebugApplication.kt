package com.cjj.keepaccounts.application

import android.os.Build
import android.os.StrictMode
import android.util.Log
import com.cjj.httplogger.HttpLogger
import com.cjj.keepaccounts.utils.LogUtils
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.lzy.okgo.OkGo
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


/**
 * Created by CJJ on 2017/11/10 10:10.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
@Suppress("unused")
class DebugApplication : MyApplication() {
    companion object {
        lateinit var refWatcher: RefWatcher
    }

    override fun onCreate() {
        super.onCreate()


        refWatcher = LeakCanary.install(this)



        val builder = OkHttpClient.Builder()

        //初始化Stetho
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build())
        builder.addNetworkInterceptor(StethoInterceptor())
        val httpLogger = HttpLogger.Builder()
                .setLevel(Log.INFO)
                .setTag("TAG")
                .build()
        builder.addInterceptor(httpLogger)


        //设置连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        //设置写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        //设置读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)

        //初始化OkGo
        OkGo.getInstance().init(this).okHttpClient = builder.build()


        //开启严格模式
        //线程策略
        val strictModeThreadPolicyBuilder = StrictMode.ThreadPolicy.Builder(StrictMode.getThreadPolicy())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            strictModeThreadPolicyBuilder.detectResourceMismatches()//资源类型不匹配 android 23增加
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            strictModeThreadPolicyBuilder.detectUnbufferedIo()//未使用缓冲区
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            strictModeThreadPolicyBuilder.penaltyListener({

            }, {
                LogUtils.exception(it)
            })
        }
        strictModeThreadPolicyBuilder.detectCustomSlowCalls()// 自定义耗时操作
                .detectNetwork()//检查网络
//                .detectDiskReads()//磁盘读取操作
//                .detectDiskWrites()//磁盘写入操作
                .penaltyDialog()//弹出违规提示框
                .penaltyFlashScreen()
                .penaltyDropBox()
                .penaltyDeathOnNetwork()
                .penaltyDeath()
                .penaltyLog()//在Logcat中打印违规日志
        StrictMode.setThreadPolicy(strictModeThreadPolicyBuilder.build())
        //虚拟机策略
        val strictModeVmPolicyBuilder = StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy())
                .detectActivityLeaks()// 检测Activity的泄露
                .detectFileUriExposure()// 检测file://或者是content://
                .detectLeakedClosableObjects()// 检查为管理的Closable对象
                .detectLeakedRegistrationObjects()// 检测需要注册类型是否解注
                .detectLeakedSqlLiteObjects()// 检测sqlite对象，如cursors
                .penaltyDeath()
                .penaltyLog()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            strictModeVmPolicyBuilder.detectCleartextNetwork()// 检测明文的网络 不允许GET请求

        }
        StrictMode.setVmPolicy(strictModeVmPolicyBuilder.build())
    }
}