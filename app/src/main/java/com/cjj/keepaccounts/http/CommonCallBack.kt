package com.cjj.keepaccounts.http

import com.cjj.keepaccounts.utils.LogUtils
import com.cjj.keepaccounts.utils.TimeUtils
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 * @author chenjunjie
 * Created by CJJ on 2018/2/2 12:55.
 */
abstract class CommonCallBack<in T> {
    private var type: Type = (this.javaClass
            .genericSuperclass as ParameterizedType).actualTypeArguments.first()


    fun getDataType(): Type {
        return type
    }

    /**
     * 开始
     */
    open fun onStart() {}

    /**
     * 成功
     */
    abstract fun onSuccess(result: T)


    /**
     * 异常
     */
    open fun onError(ex: Throwable) {
        LogUtils.exception(ex)
    }

    open fun onActionError(code: Int, serverTime: Long, msg: String) {
        LogUtils.w("code$code\tserverTime:${TimeUtils.longTurnTime(serverTime * 1000)}\tmsg:$msg")
    }

    /**
     * 所有的操作执行完成,执行此方法
     */
    open fun onComplete() {}
}