package com.cjj.keepaccounts.mvp.activity.setting.login

import com.cjj.keepaccounts.bean.http.UserInfoBean
import com.cjj.keepaccounts.http.OkGoHttpUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.request.base.Request
import javax.inject.Inject

/**
 * @author CJJ
 * Created by CJJ on 2018/9/27 14:57.
 */
class MLogin @Inject constructor() : CLogin.Model {
    override fun login(name: String, password: String): Request<String, *> {
        return OkGo.post<String>("")
                .params("platform", "Android")
                .params("app_client_version", "3.7.8")
                .params("os_version", "5.1.1")
                .params("hardware", "HUAWEIP7-L00")
                .params("device_key", "b69a74aa-7961-495d-9c44-a43bb10d04e5")
                .params("is_new_user", false)
                .params("username", name)
                .params("password", password)
                .params("merge", 1)
                .params("client_id", "qeeniao_android")
                .params("client_secret", "qeeniao2015")
                .params("requestIndex", 13)
                .params("grant_type", "password")
    }

    override fun userInfo(accessToken: String): OkGoHttpUtils<UserInfoBean> {
        return OkGoHttpUtils.get<UserInfoBean>("")
                .params("access_token", accessToken)
    }
}