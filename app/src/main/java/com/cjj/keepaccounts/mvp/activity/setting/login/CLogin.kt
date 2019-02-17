package com.cjj.keepaccounts.mvp.activity.setting.login

import com.cjj.keepaccounts.activity.setting.LoginActivity
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IModel
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.bean.http.UserInfoBean
import com.cjj.keepaccounts.http.OkGoHttpUtils
import com.lzy.okgo.request.base.Request

/**
 * @author CJJ
 * Created by CJJ on 2018/9/27 14:41.
 */
interface CLogin {
    abstract class Presenter(view: LoginActivity, model: MLogin) : BasePresenter<LoginActivity, MLogin>(view, model) {
        abstract fun login(name: String, password: String)
        abstract fun getUserInfo(accessToken: String)
    }

    interface View : IView {
        fun loginSuccess()
        fun loginError(errorCode: Int)
    }

    interface Model : IModel {
        fun login(name: String, password: String): Request<String, *>
        fun userInfo(accessToken: String): OkGoHttpUtils<UserInfoBean>
    }
}