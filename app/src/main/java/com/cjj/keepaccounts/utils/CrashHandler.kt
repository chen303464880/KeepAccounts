package com.cjj.keepaccounts.utils

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import com.cjj.keepaccounts.manager.ActivityTool
import java.io.PrintWriter
import java.io.StringWriter


/**
 * Created by CJJ on 2017/11/10 10:40.
 * Copyright © 2015-2017 CJJ All rights reserved.
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {
    private val mInfos = HashMap<String, String>()

    companion object {
        val INSTANCE: CrashHandler = CrashHandler()
    }

    private lateinit var mDefaultHandler: Thread.UncaughtExceptionHandler


    fun init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        handleException(e)
        ActivityTool.finishAllActivity()
        System.exit(0)
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        //收集设备参数信息
        collectDeviceInfo()
        //保存日志文件
        saveCrashInfo2File(ex)
        //        if (crashfilename!=null && !crashfilename.isEmpty()) {
        //            openFile(new File(crashfilename));
        //        }

//        ActivityTool.finishAllActivity()
//        System.exit(0)
        return false
    }


    /**
     * 收集设备参数信息
     */
    private fun collectDeviceInfo() {

        val versionName = Utils.getAppVersionName()
        val versionCode = Utils.getAppVersionCode().toString()
        mInfos["versionName"] = versionName
        mInfos["versionCode"] = versionCode


        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                mInfos[field.name] = field.get(null).toString()
                Log.d(TAG, field.name + " : " + field.get(null))
            } catch (e: Exception) {
                Log.e(TAG, "an error occured when collect crash info", e)
            }

        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex 异常
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private fun saveCrashInfo2File(ex: Throwable): String? {

        val sb = StringBuilder()
        mInfos.forEach {
            sb.append(it.key)
            sb.append("=")
            sb.append(it.value)
            sb.append("\n")
        }

        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        LogUtils.e(sb.toString())
        return null
    }
}