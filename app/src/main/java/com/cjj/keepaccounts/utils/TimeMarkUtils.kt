package com.cjj.keepaccounts.utils

/**
 * @author CJJ
 * Created by CJJ on 2018/5/16 17:08.
 */
class TimeMarkUtils {
    private var time: Long = System.nanoTime()
    private var msg: String? = null

    constructor()
    constructor(msg: String) {
        this.msg = msg
    }

    companion object {
        fun mark(): TimeMarkUtils {
            return TimeMarkUtils()
        }

        fun mark(msg: String): TimeMarkUtils {
            return TimeMarkUtils(msg)
        }
    }

    fun printTime() {
        val tempTime = System.nanoTime()
        if (msg == null) {
            LogUtils.i("用时:${(tempTime - time) / 1000000.0}ms", 2)
        } else {
            LogUtils.i("$msg:用时:${(tempTime - time - 4200) / 1000000.0}ms", 2)
        }
        time = tempTime
    }

    fun printTime(msg: String) {
        val tempTime = System.nanoTime()
        LogUtils.i("$msg:用时:${(tempTime - time) / 1000000.0}ms", 2)
        time = tempTime
    }
}