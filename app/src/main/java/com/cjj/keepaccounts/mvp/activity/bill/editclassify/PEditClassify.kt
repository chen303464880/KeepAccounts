package com.cjj.keepaccounts.mvp.activity.bill.editclassify

import android.os.Bundle
import com.cjj.keepaccounts.activity.bill.EditClassifyActivity
import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.bean.RecordTypeDao
import com.cjj.keepaccounts.dao.RecordTypeTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.interpolationSearch
import javax.inject.Inject

/**
 * Created by CJJ on 2019/2/1 16:31.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class PEditClassify @Inject constructor(view: EditClassifyActivity, model: BaseModel) : CEditClassify.Presenter(view, model) {
    private val bundle: Bundle by extra()
    override var type = 0
    private lateinit var recordTypes: ArrayList<RecordType>

    override fun presenter() {
        super.presenter()
        type = bundle.getInt("type")

        recordTypes = DaoManager.getRecordTypeDao().queryBuilder()
                .where(RecordTypeDao.Properties.ListId.eq(1))
                .where(RecordTypeDao.Properties.IsIncoming.eq(type))
                .where(RecordTypeDao.Properties.IsDeleted.eq(0))
                .where(RecordTypeDao.Properties.ImgSrcId.ge(0))
                .orderAsc(RecordTypeDao.Properties.OrderIndex)
                .list() as ArrayList<RecordType>
        RecordTypeTool.addOnDaoChangeListener(daoChangeListener, this)
    }

    override fun getNewRecordType(): RecordType {
        val recordType = RecordType()
        recordType.isIncoming = type
        recordType.orderIndex = recordTypes.last().orderIndex + 1
        recordType.uuid = System.currentTimeMillis() * 10000
        recordType.mTime = System.currentTimeMillis()
        recordType.listId = 1
        recordType.isDeleted = 0
        return recordType
    }

    private val daoChangeListener = object : OnDaoChangeListener<RecordType> {
        override fun onInsertEntity(entity: RecordType) {
            recordTypes.add(entity)
            view.insertData(entity)
        }

        override fun onUpdateEntity(oldEntity: RecordType, newEntity: RecordType) {
            val index = recordTypes.interpolationSearch(newEntity.orderIndex) { it.orderIndex }
            if (index != -1) {
                recordTypes[index] = newEntity
                view.updateData(index, newEntity)
            }
        }

        override fun onDeleteEntity(entity: RecordType) {
            recordTypes.remove(entity)
            view.removeData(entity)
        }
    }
}