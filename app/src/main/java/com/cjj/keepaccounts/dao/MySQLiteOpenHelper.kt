package com.cjj.keepaccounts.dao

import android.content.Context
import com.cjj.keepaccounts.bean.DaoMaster
import org.greenrobot.greendao.database.Database

/**
 * @author chenjunjie
 * Created by CJJ on 2017/11/28 9:26.
 */
class MySQLiteOpenHelper(context: Context?, name: String?) : DaoMaster.OpenHelper(context, name) {

    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
        super.onUpgrade(db, oldVersion, newVersion)
//        if (oldVersion != newVersion) {
//            MigrationHelper.migrate(db, object : MigrationHelper.ReCreateAllTableListener {
//                override fun onDropAllTables(db: Database?, ifExists: Boolean) {
//                    DaoMaster.dropAllTables(db, ifExists)
//                }
//
//                override fun onCreateAllTables(db: Database?, ifNotExists: Boolean) {
//                    DaoMaster.createAllTables(db, ifNotExists)
//
//                }
//            }, AccountDao::class.java, AccountTypeDao::class.java)
//        }
    }
}