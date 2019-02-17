package com.cjj.keepaccounts.mvp.activity.setting.setting

import com.cjj.keepaccounts.activity.setting.SettingActivity
import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.bean.GlobalUser

/**
 * @author CJJ
 * Created by CJJ on 2018/9/27 15:06.
 */
interface CSetting {
    abstract class Presenter(view: SettingActivity, model: BaseModel) : BasePresenter<SettingActivity, BaseModel>(view, model)

    interface View : IView {
        fun setUserInfo(userInfo: GlobalUser)
    }

}