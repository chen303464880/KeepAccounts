package com.cjj.keepaccounts.manager

import com.cjj.keepaccounts.bean.*

/**
 * @author chenjunjie
 * Created by CJJ on 2017/11/29 14:08.
 */
object DaoManager {
    lateinit var daoSession: DaoSession
    fun init(daoSession: DaoSession) {
        this.daoSession = daoSession
    }

    fun getAccountDao(): AccountDao {
        return daoSession.accountDao
    }

    fun getAccountTypeDao(): AccountTypeDao {
        return daoSession.accountTypeDao
    }

    fun getRecordDao(): RecordDao {
        return daoSession.recordDao
    }

    fun getRecordTypeDao(): RecordTypeDao {
        return daoSession.recordTypeDao
    }

    fun getRecordTagDao(): RecordTagDao {
        return daoSession.recordTagDao
    }

    fun getRecordToTagDao(): RecordToTagDao {
        return daoSession.recordToTagDao
    }

    fun getCreditDao(): CreditDao {
        return daoSession.creditDao
    }


    fun getListBookDao(): ListBookDao {
        return daoSession.listBookDao
    }

    fun getAppConfigDao(): AppConfigDao {
        return daoSession.appConfigDao
    }

    fun getGlobalUserDao(): GlobalUserDao {
        return daoSession.globalUserDao
    }

    fun getGlobalConfigDao(): GlobalConfigDao {
        return daoSession.globalConfigDao
    }

    fun getSameCityMainTypeDao(): SameCityMainTypeDao {
        return daoSession.sameCityMainTypeDao
    }

    fun getBudgetDao(): BudgetDao {
        return daoSession.budgetDao
    }


}