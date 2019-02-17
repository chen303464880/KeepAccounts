package com.cjj.keepaccounts.mvp.activity.setting.login

import com.cjj.keepaccounts.activity.setting.LoginActivity
import com.cjj.keepaccounts.bean.http.LoginBean
import com.cjj.keepaccounts.dao.GlobalConfigTool
import com.cjj.keepaccounts.dao.GlobalUserTool
import com.cjj.keepaccounts.http.gson
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import javax.inject.Inject

/**
 * @author CJJ
 * Created by CJJ on 2018/9/27 14:50.
 */
class PLogin @Inject constructor(view: LoginActivity, model: MLogin) : CLogin.Presenter(view, model) {
    override fun login(name: String, password: String) {
        model.login(name, password).execute(object : StringCallback() {
            override fun onSuccess(response: Response<String>) {
                val result = gson.fromJson<LoginBean>(response.body(), LoginBean::class.java)
                if (result.isSuccess) {
                    val globalConfig = GlobalConfigTool.globalConfig.clone()
                    globalConfig.accessToken = result.access_token
                    globalConfig.refreshToken = result.refresh_token
                    GlobalConfigTool.update(GlobalConfigTool.globalConfig, globalConfig)
                    getUserInfo(result.access_token)
                } else {
                    view.loginError(result.code)
                }
            }
        })
    }

    override fun getUserInfo(accessToken: String) {
        model.userInfo(accessToken)
                .execute {
                    val globalUser = GlobalUserTool.globalUser.clone()
                    globalUser.setUserInfo(it)
                    globalUser.is_login = 1
                    GlobalUserTool.update(GlobalUserTool.globalUser, globalUser)
                    view.loginSuccess()
                }
    }
}