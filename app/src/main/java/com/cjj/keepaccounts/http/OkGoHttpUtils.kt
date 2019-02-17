package com.cjj.keepaccounts.http

import com.cjj.httplogger.CodeUtils
import com.cjj.keepaccounts.BuildConfig
import com.cjj.keepaccounts.bean.ResultBase
import com.cjj.keepaccounts.utils.Utils
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.BodyRequest
import com.lzy.okgo.request.base.Request
import java.lang.reflect.Type


/**
 * Created by CJJ on 2018/6/25 20:36.
 * * Copyright © 2015-2019 CJJ All rights reserved.
 */


class OkGoHttpUtils<T> constructor(private val request: Request<ResultBase, *>, private val type: Type) {

    companion object {
        private var requestIndex = 1
        inline fun <reified T> get(url: String): OkGoHttpUtils<T> {
            return OkGoHttpUtils(OkGo.get<ResultBase>(url).tag(url), T::class.java)
        }

        inline fun <reified T> post(url: String): OkGoHttpUtils<T> {
            return OkGoHttpUtils(OkGo.post<ResultBase>(url).tag(url), T::class.java)
        }

        inline fun <reified T> put(url: String): OkGoHttpUtils<T> {
            return OkGoHttpUtils(OkGo.put<ResultBase>(url).tag(url), T::class.java)
        }

        inline fun <reified T> delete(url: String): OkGoHttpUtils<T> {
            return OkGoHttpUtils(OkGo.delete<ResultBase>(url).tag(url), T::class.java)
        }
    }

    fun params(key: String, value: String, isReplace: Boolean = false): OkGoHttpUtils<T> {
        request.params(key, value, isReplace)
        return this
    }

    fun params(key: String, value: Int, isReplace: Boolean = false): OkGoHttpUtils<T> {
        request.params(key, value, isReplace)
        return this
    }

    fun params(key: String, value: Long, isReplace: Boolean = false): OkGoHttpUtils<T> {
        request.params(key, value, isReplace)
        return this
    }

    fun params(key: String, value: Float, isReplace: Boolean = false): OkGoHttpUtils<T> {
        request.params(key, value, isReplace)
        return this
    }

    fun params(key: String, value: Double, isReplace: Boolean = false): OkGoHttpUtils<T> {
        request.params(key, value, isReplace)
        return this
    }

    fun params(key: String, value: Char, isReplace: Boolean = false): OkGoHttpUtils<T> {
        request.params(key, value, isReplace)
        return this
    }

    fun params(key: String, value: Boolean, isReplace: Boolean = false): OkGoHttpUtils<T> {
        request.params(key, value, isReplace)
        return this
    }

    fun params(params: Map<String, String>, isReplace: Boolean = false): OkGoHttpUtils<T> {
        request.params(params, isReplace)
        return this
    }


    fun upJson(`object`: Any): OkGoHttpUtils<T> {
        return upJson(gson.toJson(`object`))
    }

    fun upJson(jsonStr: String): OkGoHttpUtils<T> {
        if (request is BodyRequest<ResultBase, *>) {
            request.upJson(jsonStr)
        }
        return this
    }

    fun removeAllParams() {
        request.removeAllParams()
    }

    fun execute(success: (result: T) -> Unit): OkGoHttpUtils<T> {
        return execute(success, null)
    }

    fun execute(success: (result: T) -> Unit, error: ((ex: Throwable) -> Unit)? = null): OkGoHttpUtils<T> {
        return execute(object : CommonCallBack<T>() {
            override fun onSuccess(result: T) {
                success.invoke(result)
            }

            override fun onError(ex: Throwable) {
                super.onError(ex)
                error?.invoke(ex)
            }
        })
    }

    fun execute(callBack: CommonCallBack<T>): OkGoHttpUtils<T> {
        request.params("platform", "Android")
                .params("app_client_version", "3.7.3")
                .params("os_version", Utils.getAndroidVersion())
                .params("hardware", Utils.getPhoneModel())
                .params("device_key", "5a34967d-5c56-4c74-bf38-c9f2f543617e")
                .params("is_new_user", true)
                .params("requestIndex", requestIndex)
        if (BuildConfig.DEBUG) {
            request.headers("location", CodeUtils.getCodeLine(this.javaClass))
        }

        request.execute(object : JsonCallBack() {
            override fun onStart(request: Request<ResultBase, out Request<Any, Request<*, *>>>?) {
                super.onStart(request)
                callBack.onStart()
            }

            override fun onSuccess(response: Response<ResultBase>) {
                requestIndex++
                val resultBase = response.body()
                if (resultBase.isSuccess) {
                    val t = gson.fromJson<T>(response.body().data, type)
                    callBack.onSuccess(t)
                } else {
                    callBack.onActionError(resultBase.code, resultBase.server_time, resultBase.message)
                }
                callBack.onComplete()
            }

            override fun onError(response: Response<ResultBase>) {
                super.onError(response)
                callBack.onError(response.exception)
                callBack.onComplete()
            }
        })
        return this
    }

    fun execute(): okhttp3.Response {
        request.params("platform", "Android")
                .params("app_client_version", "3.7.3")
                .params("os_version", Utils.getAndroidVersion())
                .params("hardware", Utils.getPhoneModel())
                .params("device_key", "5a34967d-5c56-4c74-bf38-c9f2f543617e")
                .params("requestIndex", requestIndex)
        if (BuildConfig.DEBUG) {
            request.headers("location", CodeUtils.getCodeLine(this.javaClass))
        }
        return request.execute()
    }
}

