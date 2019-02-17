package com.cjj.keepaccounts.activity.setting

import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.text.inSpans
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.bean.GlobalUser
import com.cjj.keepaccounts.dao.GlobalUserTool
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.mvp.activity.setting.setting.CSetting
import com.cjj.keepaccounts.mvp.activity.setting.setting.PSetting
import com.cjj.keepaccounts.utils.CircleTransformation
import com.cjj.keepaccounts.utils.Utils
import kotlinx.android.synthetic.main.activity_setting.*


/**
 * @author CJJ
 * Created by CJJ on 2018/6/26 16:34.
 */
class SettingActivity : WhiteActivity<PSetting>(), CSetting.View {

    override fun getContentView(): View = Utils.inflate(R.layout.activity_setting, this)

    override fun initView() {
        setActivityTitleText(getString(R.string.settings))
        setActivityBackText(getString(R.string.bill))
        presenter.presenter()
        showTitleLine()
    }

    override fun initListener() {
        super.initListener()
        rl_user_info.setOnClickListener {
            if (GlobalUserTool.globalUser.is_login == 0) {
                ActivityTool.skipActivity<LoginActivity>()
            }
        }
    }

    override fun setUserInfo(userInfo: GlobalUser) {
        Glide.with(this)
                .load(userInfo.imgHead)
                .apply(RequestOptions().transform(CircleTransformation()))
                .into(iv_head_img)
        tv_username.text = userInfo.nick
        val day = (System.currentTimeMillis() / 1000 - userInfo.ctime) / (60 * 60 * 24)
        val span = SpannableStringBuilder("你已坚持记账")
        span.inSpans(ForegroundColorSpan(Utils.getColor(R.color.AppThemeColor))) {
            append(day.toString())
        }
        span.append("天")
        tv_desc.text = span
    }

}