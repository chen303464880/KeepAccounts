package com.cjj.keepaccounts.dao

import com.cjj.keepaccounts.base.BaseDaoTool
import com.cjj.keepaccounts.bean.GlobalUser
import com.cjj.keepaccounts.manager.DaoManager

/**
 * @author CJJ
 * Created by CJJ on 2018/6/27 11:11.
 */
object GlobalUserTool : BaseDaoTool<GlobalUser>() {

    val globalUser = DaoManager.getGlobalUserDao()
            .queryBuilder()
            .unique()!!

    override fun insert(entity: GlobalUser, isNotify: Boolean) {

    }

    override fun update(oldEntity: GlobalUser, newEntity: GlobalUser, isNotify: Boolean) {
        DaoManager.getGlobalUserDao().update(newEntity)
        if (isNotify) {
            notifyDaoUpdateChange(oldEntity, newEntity)
        }
        globalUser.refresh()
    }

    override fun delete(entity: GlobalUser, isNotify: Boolean) {

    }
}