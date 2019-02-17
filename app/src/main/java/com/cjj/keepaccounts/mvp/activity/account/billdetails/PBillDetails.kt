package com.cjj.keepaccounts.mvp.activity.account.billdetails

import com.cjj.keepaccounts.activity.account.BillDetailsActivity
import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.dao.RecordTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import javax.inject.Inject

/**
 * Created by CJJ on 2019/1/27 16:01.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class PBillDetails @Inject constructor(view: BillDetailsActivity, model: BaseModel) : CBillDetails.Presenter(view, model) {
    private var record: Record by extra()


    override fun onCreate() {
        super.onCreate()
        RecordTool.addOnDaoChangeListener(recordChangeListener, this)
    }

    override fun presenter() {
        super.presenter()
        view.setData(record)
    }

    override fun deleteRecord() {
        RecordTool.delete(record)
    }

    private val recordChangeListener = object : OnDaoChangeListener<Record> {
        override fun onInsertEntity(entity: Record) {

        }

        override fun onUpdateEntity(oldEntity: Record, newEntity: Record) {
            record = newEntity
            record.resetCredits()
            record.resetMembers()
            view.setData(record)
        }


        override fun onDeleteEntity(entity: Record) {

        }
    }

}