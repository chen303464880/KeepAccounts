package com.cjj.keepaccounts.dao


import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseDaoTool
import com.cjj.keepaccounts.bean.ListBook
import com.cjj.keepaccounts.bean.ListBookDao
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.TimeUtils
import com.cjj.keepaccounts.utils.Utils
import java.math.BigDecimal
import java.util.*

/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/10 10:10.
 */
object ListBookTool : BaseDaoTool<ListBook>() {

    @Suppress("ObjectPropertyName")
    private var _listBook: ListBook? = null
    private val listBook: ListBook
        get() {
            if (_listBook == null) {
                _listBook = DaoManager.getListBookDao().queryBuilder()
                        .where(ListBookDao.Properties.IsActivated.eq(1))
                        .unique()
            }
            return _listBook!!
        }

    override fun insert(entity: ListBook, isNotify: Boolean) {
        DaoManager.getListBookDao().insert(entity)
        if (isNotify) {
            notifyDaoInsertChange(entity)
        }
    }

    override fun update(oldEntity: ListBook, newEntity: ListBook, isNotify: Boolean) {
        DaoManager.getListBookDao().update(newEntity)
        if (newEntity.isDefault == 1) {
            _listBook = newEntity
        }
        if (isNotify) {
            notifyDaoUpdateChange(oldEntity, newEntity)
        }
    }

    override fun delete(entity: ListBook, isNotify: Boolean) {
        entity.isDeleted = 1
        DaoManager.getListBookDao().update(entity)
        if (isNotify) {
            notifyDaoDeleteChange(entity)
        }
    }

    fun getBudgetModeText(entity: ListBook): String = when (entity.listBudgetShow) {
        "month" -> Utils.getString(R.string.month_budget)
        "week" -> Utils.getString(R.string.week_budget)
        else -> Utils.getString(R.string.day_budget)
    }

    fun getBudgetMode(modeStr: String): String = when (modeStr) {
        Utils.getString(R.string.month_budget) -> "month"
        Utils.getString(R.string.week_budget) -> "week"
        else -> "day"
    }

    fun getBudget(): ListBook {
        return listBook
    }

    fun getMonthBudget(year: Int, month: Int, day: Int): Pair<Double, Double> {
        var monthExpend = 0.0
        val startDate = TimeUtils.getTheDate(year, month, 1)
        val endDate = TimeUtils.getTheDate(year, month, day)
        val sql = "select sum(r.rate_money) from record as r , com_qeeniao_mobile_kdjz_Models_RecordType as type where r.typeId > 0 and r.is_deleted = 0 and r.typeId = type.uuid and type.isIncoming=0 and r.the_date between ? and ?"
        val rawQuery = DaoManager.daoSession.database.rawQuery(sql, arrayOf(startDate.toString(), endDate.toString()))
        if (rawQuery.moveToNext()) {
            monthExpend = rawQuery.getDouble(0)
        }
        rawQuery.close()
        return Pair(getBudget().listBudget, monthExpend)
    }

    fun getWeekBudget(year: Int, month: Int, day: Int): Pair<Double, Double> {
        val sql = "select sum(r.rate_money) from record as r , com_qeeniao_mobile_kdjz_Models_RecordType as type where r.typeId > 0 and r.is_deleted = 0 and r.typeId = type.uuid and type.isIncoming=0 and r.the_date between ? and ?"
        var expend = 0.0
        var weekExpend = 0.0
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        val weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH)
        if (weekOfMonth != 1) {//获取本周之前的支出
            calendar.set(Calendar.WEEK_OF_MONTH, weekOfMonth - 1)
            calendar.set(Calendar.DAY_OF_WEEK, 7)

            val startDate = TimeUtils.getTheDate(year, month, 1)
            val endDate = TimeUtils.getTheDate(year, month, calendar.get(Calendar.DAY_OF_MONTH))
            val rawQuery = DaoManager.daoSession.database.rawQuery(sql, arrayOf(startDate.toString(), endDate.toString()))
            if (rawQuery.moveToNext()) {
                expend = rawQuery.getDouble(0)
            }
            rawQuery.close()
        }

        calendar.set(Calendar.WEEK_OF_MONTH, weekOfMonth)
        calendar.set(Calendar.DAY_OF_WEEK, 1)
        var startDate = TimeUtils.getTheDate(year, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        if (startDate > TimeUtils.getTheDate(year, month, 1)) {
            startDate = TimeUtils.getTheDate(year, month, 1)
        }
        calendar.set(Calendar.DAY_OF_WEEK, 7)
        var endDate = TimeUtils.getTheDate(year, month, calendar.get(Calendar.DAY_OF_MONTH))
        if (endDate > TimeUtils.getTheDate(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))) {
            endDate = TimeUtils.getTheDate(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        val rawQuery = DaoManager.daoSession.database.rawQuery(sql, arrayOf(startDate.toString(), endDate.toString()))
        if (rawQuery.moveToNext()) {
            weekExpend = rawQuery.getDouble(0)
        }
        rawQuery.close()

        val weekMaxNum = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)
        val weekBudget = (BigDecimal(getBudget().listBudget) - BigDecimal(expend)).divide(BigDecimal(weekMaxNum - weekOfMonth + 1), 2, BigDecimal.ROUND_HALF_UP).toDouble()
        return Pair(weekBudget, weekExpend)
    }

    fun getDayBudget(year: Int, month: Int, day: Int): Pair<Double, Double> {
        val sql = "select sum(r.rate_money) from record as r , com_qeeniao_mobile_kdjz_Models_RecordType as type where r.typeId > 0 and r.is_deleted = 0 and r.typeId = type.uuid and type.isIncoming=0 and r.the_date between ? and ?"
        var expend = 0.0
        var dayExpend = 0.0
        val calendar = Calendar.getInstance()
        if (day != 1) {
            val startDate = TimeUtils.getTheDate(year, month, 1)
            val endDate = TimeUtils.getTheDate(year, month, day - 1)
            val rawQuery = DaoManager.daoSession.database.rawQuery(sql, arrayOf(startDate.toString(), endDate.toString()))
            if (rawQuery.moveToNext()) {
                expend = rawQuery.getDouble(0)
            }
            rawQuery.close()
        }
        val date = TimeUtils.getTheDate(year, month, day)
        val sql1 = "select sum(r.rate_money) from record as r , com_qeeniao_mobile_kdjz_Models_RecordType as type where r.typeId > 0 and r.is_deleted = 0 and r.typeId = type.uuid and type.isIncoming=0 and r.the_date = ?"

        val rawQuery = DaoManager.daoSession.database.rawQuery(sql1, arrayOf(date.toString()))
        if (rawQuery.moveToNext()) {
            dayExpend = rawQuery.getDouble(0)
        }
        rawQuery.close()

        val dayMaxNum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dayBudget = (BigDecimal(getBudget().listBudget) - BigDecimal(expend)).divide(BigDecimal(dayMaxNum - day + 1), 2, BigDecimal.ROUND_HALF_UP).toDouble()
        return Pair(dayBudget, dayExpend)

    }
}

