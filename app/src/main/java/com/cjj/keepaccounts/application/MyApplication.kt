package com.cjj.keepaccounts.application

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Handler
import com.cjj.keepaccounts.bean.DaoMaster
import com.cjj.keepaccounts.dagger.DaggerMyAppComponent
import com.cjj.keepaccounts.dao.MySQLiteOpenHelper
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.CrashHandler
import com.cjj.keepaccounts.utils.TimeMarkUtils
import com.cjj.keepaccounts.utils.ioToMain
import com.lzy.okgo.BuildConfig
import com.lzy.okgo.OkGo
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import okhttp3.OkHttpClient
import org.greenrobot.greendao.query.QueryBuilder
import rx.Observable
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by CJJ on 2017/11/10 10:05.
 * Copyright © 2015-2017 CJJ All rights reserved.
 */
@SuppressLint("Registered")
open class MyApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    companion object {
        @JvmStatic
        val mark = TimeMarkUtils.mark()
        @JvmStatic
        lateinit var CONTEXT: Application
        //主线程handler
        @JvmStatic
        val HANDLER: Handler by lazy(LazyThreadSafetyMode.NONE) {
            Handler(CONTEXT.mainLooper)
        }

        private var isDatabaseInit = false
        private var initCallbacks: (() -> Unit)? = null

        fun registerDatabaseInitCallbacks(callback: () -> Unit) {
            if (isDatabaseInit) {
                callback.invoke()
            } else {
                initCallbacks = callback
            }
        }

        @JvmStatic
        fun init(application: MyApplication) {
            //保存application对象
            CONTEXT = application

            //dagger注入
            DaggerMyAppComponent.create().inject(application)

            //初始化数据库
            Observable.defer {
                //异常抓取
                CrashHandler.INSTANCE.init()
                val databasePath = CONTEXT.getDatabasePath("qeeniao.db")
                if (!databasePath.exists()) {
                    databasePath.parentFile.mkdirs()
                    databasePath.createNewFile()
                    val inputStream = CONTEXT.resources.assets.open("qeeniao.db")
                    val bis = BufferedInputStream(inputStream)
                    val bos = BufferedOutputStream(FileOutputStream(databasePath))
                    val b = ByteArray(8 * 1024)
                    var n = bis.read(b)
                    while (n != -1) {
                        bos.write(b, 0, n)
                        n = bis.read(b)
                    }
                    bis.close()
                    bos.close()
                }

                //初始化数据库
                QueryBuilder.LOG_SQL = BuildConfig.DEBUG
                QueryBuilder.LOG_VALUES = BuildConfig.DEBUG
                DaoManager.init(DaoMaster(MySQLiteOpenHelper(CONTEXT, "qeeniao.db").writableDb).newSession())
                Observable.just(true)
            }.ioToMain()
                    .subscribe {
                        isDatabaseInit = it
                        initCallbacks?.invoke()
                        //置空,避免内存泄漏
                        initCallbacks = null
                    }
        }


    }

    override fun onCreate() {
        super.onCreate()
        init(this)

        val builder = OkHttpClient.Builder()
        //设置连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        //设置写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
        //设置读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)

        //初始化OkGo
        OkGo.getInstance().init(CONTEXT).okHttpClient = builder.build()
    }

}