package com.cjj.keepaccounts.dao

import com.cjj.keepaccounts.base.BaseDaoTool
import com.cjj.keepaccounts.bean.Account
import com.cjj.keepaccounts.bean.AccountDao
import com.cjj.keepaccounts.bean.GlobalConfig
import com.cjj.keepaccounts.manager.DaoManager

/**
 * @author CJJ
 * Created by CJJ on 2018/6/27 11:56.
 */
object GlobalConfigTool : BaseDaoTool<GlobalConfig>() {
    val globalConfig = DaoManager.getGlobalConfigDao()
            .queryBuilder()
            .unique()!!

    override fun insert(entity: GlobalConfig, isNotify: Boolean) {

    }

    override fun update(oldEntity: GlobalConfig, newEntity: GlobalConfig, isNotify: Boolean) {
        DaoManager.getGlobalConfigDao().update(newEntity)
        if (isNotify) {
            notifyDaoUpdateChange(oldEntity, newEntity)
        }
        globalConfig.refresh()
    }

    override fun delete(entity: GlobalConfig, isNotify: Boolean) {

    }

    fun getLastAccount(): Account? {
        return try {
            DaoManager.getAccountDao().queryBuilder()
                    .where(AccountDao.Properties.IsDeleted.eq(0))
                    .where(AccountDao.Properties.Uuid.eq(globalConfig.jiyibiLastAccountid)).uniqueOrThrow()
        } catch (ex: Exception) {
            null
        }
    }

    fun updateLastAccountId(id: Long) {
        globalConfig.jiyibiLastAccountid = id
        DaoManager.getGlobalConfigDao().update(globalConfig)
    }

    fun getAccessToken(): String {
        return globalConfig.accessToken
    }

    fun getRefreshToken(): String {
        return globalConfig.refreshToken
    }
}