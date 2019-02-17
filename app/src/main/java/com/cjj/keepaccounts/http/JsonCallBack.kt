package com.cjj.keepaccounts.http

import com.cjj.keepaccounts.bean.ResultBase
import com.lzy.okgo.callback.AbsCallback

/**
 * Created by CJJ on 2018/6/25 22:03.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
abstract class JsonCallBack : AbsCallback<ResultBase>() {
    companion object {
        val converter=com.cjj.keepaccounts.http.ResultBaseConverter()
    }

    override fun convertResponse(response: okhttp3.Response?): ResultBase {
        return converter.convertResponse(response)
    }

}