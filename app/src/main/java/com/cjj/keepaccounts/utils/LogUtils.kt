package com.cjj.keepaccounts.utils

import android.text.TextUtils
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*


/**
 * Created by CJJ on 2017/11/10 10:42.
 * Copyright © 2015-2017 CJJ All rights reserved.
 */
object LogUtils {
    private const val TAG: String = "TAG"
    private val isDebug: Boolean = Utils.getAppDebug()
    private const val TOP_BORDER = "╔════════════════════════════════════════════════════" +
//            "════════════════════════════════════════════════════════════════════════════════════" +
            "═════════════════════════════════════════"
    private const val CANTER_BORDER = "╟─────────────────────────────────────────────────" +
//            "────────────────────────────────────────────────────────────────────────────────────" +
            "────────────────────────────────────────────"
    private const val BOTTOM_BORDER = "╚═════════════════════════════════════════════════" +
//            "════════════════════════════════════════════════════════════════════════════════════" +
            "════════════════════════════════════════════"
    private const val LEFT_CHAR = "║"


    fun e(message: String, vararg tier: Int) {
        if (isDebug) {
            log(Log.ERROR, TAG, message, tier)
        }
    }

    fun e(tag: String, message: String, vararg tier: Int) {
        if (isDebug) {
            log(Log.ERROR, tag, message, tier)
        }
    }

    fun w(message: String, vararg tier: Int) {
        if (isDebug) {
            log(Log.WARN, TAG, message, tier)
        }
    }

    fun w(tag: String, message: String, vararg tier: Int) {
        if (isDebug) {
            log(Log.WARN, tag, message, tier)
        }
    }

    fun i(message: String, vararg tier: Int) {

        if (isDebug) {
            log(Log.INFO, TAG, message, tier)
        }
    }

    fun i(tag: String, message: String, vararg tier: Int) {
        if (isDebug) {
            log(Log.INFO, tag, message, tier)
        }
    }

    fun d(message: String, vararg tier: Int) {
        if (isDebug) {
            log(Log.DEBUG, TAG, message, tier)
        }
    }

    fun d(tag: String, message: String, vararg tier: Int) {
        if (isDebug) {
            log(Log.DEBUG, tag, message, tier)
        }
    }

    fun exception(ex: Throwable, vararg tier: Int) {

        val message = getThrowText(ex)
        log(Log.ERROR, TAG, message, tier)

    }

    fun exception(tag: String, ex: Throwable, vararg tier: Int) {

        val message = getThrowText(ex)
        log(Log.ERROR, tag, message, tier)

    }

    fun <T> list(iterable: Iterable<T>, vararg tier: Int) {
        log(Log.INFO, TAG, iterableToString(iterable), tier)
    }

    fun <T> list(tag: String, iterable: Iterable<T>, vararg tier: Int) {
        log(Log.INFO, tag, iterableToString(iterable), tier)
    }

    private fun <T> iterableToString(iterable: Iterable<T>): String {
        val sb = StringBuilder()
        iterable.forEach {
            sb.append(it.toString())
            sb.append("\n")
        }
        if (sb.length > 1) {
            sb.subSequence(0, sb.length - 1)
        }
        return sb.toString()
    }

    fun map(map: Map<*, *>, vararg tier: Int) {
        val sb = StringBuilder()
        for ((k, v) in map) {
            sb.append(k)
            sb.append(" -> ")
            sb.append(v)
            sb.append("\n")
        }
        if (sb.length > 1) {
            sb.subSequence(0, sb.length - 1)
        }
        log(Log.INFO, TAG, sb.toString(), tier)
    }

    fun json(json1: String, vararg tier: Int) {
        var json = json1
        if (TextUtils.isEmpty(json) or !isDebug) {
            return
        }
        try {
            json = json.trim { it <= ' ' }
            var message = ""
            if (json.startsWith("{")) {
                val jsonObject = JSONObject(json)
                message = jsonObject.toString(2)
            } else if (json.startsWith("[")) {
                val jsonArray = JSONArray(json)
                message = jsonArray.toString(2)
            }
            log(Log.INFO, TAG, message, tier)
        } catch (ex: JSONException) {
            LogUtils.exception(ex)
        }
    }

    @Synchronized
    private fun log(logType: Int, tag: String, msg: String, tiers: IntArray) {
        var tier = 1
        if (!tiers.isEmpty()) {
            tier = tiers[0]
        }
        //打印开始分割线
        logTopBorder(logType, tag)
        //打印线程及类名方法名
        logHeaderContent(logType, tag, tier)
        //打印分割线
        logDivider(logType, tag)
        //打印内容
        val split = msg.split("\n")
        split.forEach {
            val chunk = LEFT_CHAR + "\t" + it
            logChunk(logType, tag, chunk)
        }

        //打印结束分割线
        logBottomBorder(logType, tag)


    }

    private fun logHeaderContent(logType: Int, tag: String, tier: Int) {
        val sb = StringBuilder()
        val threadName: String = Thread.currentThread().name
        sb.append("Thread: ")
                .append(threadName)
        val length = sb.length
        val stackTrace = Thread.currentThread().stackTrace
        for (i in 0 until tier) {
            val stackTraceElement = stackTrace[5 + i]
            val className = getSimpleClassName(stackTraceElement.className)
            val methodName = stackTraceElement.methodName
            val fileName = stackTraceElement.fileName
            val lineNumber = stackTraceElement.lineNumber

            if (i != 0) {
                sb.append("\n\t")
                for (j in 0 until length / 4) {
                    sb.append("\t")
                }
                for (j in 0 until i) {
                    sb.append("\t")
                }
            }

            sb.append("\t\t")
                    .append(className)
                    .append(".")
                    .append(methodName)
                    .append("(")
                    .append(fileName)
                    .append(":")
                    .append(lineNumber)
                    .append(")")
        }
        val split = sb.toString().split("\n")
        split.forEach {
            val chunk = LEFT_CHAR + "\t" + it
            logChunk(logType, tag, chunk)
        }
    }

    private fun getThrowText(ex: Throwable): String {
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)

            cause = cause.cause
        }
        printWriter.close()


        return writer.toString()
    }

    private fun logTopBorder(logType: Int, tag: String) {
        logChunk(logType, tag, TOP_BORDER)
    }

    private fun logDivider(logType: Int, tag: String) {
        logChunk(logType, tag, CANTER_BORDER)
    }

    private fun logBottomBorder(logType: Int, tag: String) {
        logChunk(logType, tag, BOTTOM_BORDER)
    }

    private fun getSimpleClassName(name: String): String {
        val lastIndex = name.lastIndexOf(".")
        return name.substring(lastIndex + 1)
    }

    private var tagNum = 0
    private val random = Random()
    /**
     * 输出到控制台
     *
     * @param priority 级别
     * @param tag      tag
     * @param msg      message
     */
    private fun logChunk(priority: Int, tag: String, msg: String) {
        var rNum = 0
        while (rNum == tagNum) {
            rNum = LogUtils.random.nextInt(10)
        }
        tagNum = rNum
        Log.println(priority, tag + tagNum.toString(), msg)
    }
}