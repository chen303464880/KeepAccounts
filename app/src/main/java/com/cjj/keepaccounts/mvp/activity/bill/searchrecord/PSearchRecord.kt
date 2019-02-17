package com.cjj.keepaccounts.mvp.activity.bill.searchrecord

import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.bill.SearchRecordActivity
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.utils.LogUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.utils.bindToLifecycle
import org.jetbrains.anko.collections.forEachWithIndex
import java.math.BigDecimal
import javax.inject.Inject

/**
 * @author CJJ
 * Created by CJJ on 2018/9/28 15:31.
 */
class PSearchRecord @Inject constructor(view: SearchRecordActivity, model: MSearchRecord) : CSearchRecord.Presenter(view, model) {

    private lateinit var data: ArrayList<Record>

    override fun search(key: String) {
        model.getRecordDate("%$key%")
                .bindToLifecycle(this)
                .subscribe {
                    LogUtils.list(it)
                    data = it as ArrayList<Record>
                    view.setRecordDate(it)
                    setMoneyInfo()
                }
    }

    private fun setMoneyInfo() {
        val stringBuilder = StringBuilder()
        if (model.income != BigDecimal.ZERO) {
            stringBuilder.append(Utils.context.getString(R.string.income_xx, model.income.toDouble()))
            stringBuilder.append("    ")
        }
        if (model.expend != BigDecimal.ZERO) {
            stringBuilder.append(Utils.context.getString(R.string.expend_xx, model.expend.toDouble()))
        }
        view.setMoneyInfo(stringBuilder.toString())
    }

    override fun onCreate() {
        super.onCreate()
        RecordTool.addOnDaoChangeListener(recordDaoChangeListener, this)
    }

    private val recordDaoChangeListener = object : OnDaoChangeListener<Record> {
        override fun onInsertEntity(entity: Record) {

        }

        override fun onUpdateEntity(oldEntity: Record, newEntity: Record) {
            data.forEachWithIndex { i, record ->
                if (record.uuid == oldEntity.uuid) {
                    data[i] = newEntity
                    view.updateData(i, newEntity)
                    if (oldEntity.recordType.isIncoming != newEntity.recordType.isIncoming) {
                        if (oldEntity.recordType.isIncoming == 0) {//之前为支出,现在为收入
                            model.expend -= oldEntity.rateMoney.toBigDecimal()//减去旧的支出
                            model.income += newEntity.rateMoney.toBigDecimal()//加上新的收入

                        } else {////之前为收入,现在为支出
                            model.income -= oldEntity.rateMoney.toBigDecimal()//减去旧的收入
                            model.expend += newEntity.rateMoney.toBigDecimal()//加上新的支出
                        }
                    } else {
                        if (oldEntity.recordType.isIncoming == 0) {//之前为支出
                            model.expend = model.expend - oldEntity.rateMoney.toBigDecimal() + newEntity.rateMoney.toBigDecimal()
                        } else {//之前为收入
                            model.income = model.income - oldEntity.rateMoney.toBigDecimal() + newEntity.rateMoney.toBigDecimal()
                        }
                    }
                    setMoneyInfo()
                    return
                }
            }
        }

        override fun onDeleteEntity(entity: Record) {
            data.forEachWithIndex { i, record ->
                if (record.uuid == entity.uuid) {
                    data.removeAt(i)
                    view.removeData(i)
                    if (entity.recordType.isIncoming == 0) {
                        model.expend -= entity.rateMoney.toBigDecimal()
                    } else {
                        model.income -= entity.rateMoney.toBigDecimal()
                    }
                    setMoneyInfo()
                    return
                }
            }
        }
    }


}