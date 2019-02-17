package com.cjj.keepaccounts.mvp.activity.setting.setting

import com.cjj.keepaccounts.activity.setting.SettingActivity
import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.bean.GlobalUser
import com.cjj.keepaccounts.dao.GlobalUserTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import javax.inject.Inject

/**
 * @author CJJ
 * Created by CJJ on 2018/9/27 15:09.
 */
class PSetting @Inject constructor(view: SettingActivity, model: BaseModel) : CSetting.Presenter(view, model) {


    override fun presenter() {
        if (GlobalUserTool.globalUser.is_login == 1) {
            view.setUserInfo(GlobalUserTool.globalUser)
        }
    }


    override fun onCreate() {
        super.onCreate()
        GlobalUserTool.addOnDaoChangeListener(userDaoChangeListener, this)
    }

    private val userDaoChangeListener = object : OnDaoChangeListener<GlobalUser> {
        override fun onInsertEntity(entity: GlobalUser) {

        }

        override fun onUpdateEntity(oldEntity: GlobalUser, newEntity: GlobalUser) {
            view.setUserInfo(newEntity)
        }

        override fun onDeleteEntity(entity: GlobalUser) {

        }
    }


}