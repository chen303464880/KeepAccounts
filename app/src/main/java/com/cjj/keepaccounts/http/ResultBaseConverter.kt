package com.cjj.keepaccounts.http

import com.cjj.keepaccounts.bean.ResultBase
import com.google.gson.Gson
import com.lzy.okgo.convert.Converter
import com.lzy.okgo.exception.HttpException
import okhttp3.Response

/**
 * Created by CJJ on 2018/6/25 22:05.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
val gson = Gson()

class ResultBaseConverter : Converter<ResultBase> {
    override fun convertResponse(response: Response?): ResultBase {
        val body = response?.body()
        return if (body != null) {
            gson.fromJson(body.charStream(), ResultBase::class.java)
        } else {
            throw HttpException("response.body == null!")
        }
    }
}