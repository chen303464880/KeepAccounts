package com.cjj.keepaccounts.activity.setting

import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.TitleActivity
import com.cjj.keepaccounts.mvp.activity.setting.login.CLogin
import com.cjj.keepaccounts.mvp.activity.setting.login.PLogin
import com.cjj.keepaccounts.service.SyncService
import com.cjj.keepaccounts.utils.ToastUtils
import com.cjj.keepaccounts.utils.Utils
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by CJJ on 2018/6/26 20:13.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class LoginActivity : TitleActivity<PLogin>(), CLogin.View {

    override fun getContentView(): View = Utils.inflate(R.layout.activity_login, this)

    override fun initView() {

    }

    override fun initListener() {
        super.initListener()
        setActivityTitleColor(Utils.getColor(R.color.bg_color_467fea))
        btn_login.setOnClickListener {
            presenter.login(et_username.text.toString(), et_password.text.toString())
        }
    }

    override fun loginSuccess() {
        SyncService.startSyncService(this)
        finish()
    }

    override fun loginError(errorCode: Int) {
        when (errorCode) {
            200 -> ToastUtils.shortToast("密码错误")
            204 -> ToastUtils.shortToast("用户找不到")
            else -> {
            }
        }
    }
}