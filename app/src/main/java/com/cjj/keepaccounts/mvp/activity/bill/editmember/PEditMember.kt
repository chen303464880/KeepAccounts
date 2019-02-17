package com.cjj.keepaccounts.mvp.activity.bill.editmember

import com.cjj.keepaccounts.activity.bill.EditMemberActivity
import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.bean.RecordTag
import com.cjj.keepaccounts.bean.RecordTagDao
import com.cjj.keepaccounts.dao.RecordTagTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.manager.DaoManager
import org.jetbrains.anko.collections.forEachWithIndex
import javax.inject.Inject

/**
 * Created by CJJ on 2019/2/1 14:52.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class PEditMember @Inject constructor(view: EditMemberActivity, model: BaseModel) : CEditMember.Presenter(view, model), OnDaoChangeListener<RecordTag> {

    private lateinit var recordTags: ArrayList<RecordTag>

    override fun onCreate() {
        super.onCreate()
        RecordTagTool.addOnDaoChangeListener(this, this)
    }

    override fun presenter() {
        super.presenter()
        recordTags = DaoManager.getRecordTagDao().queryBuilder()
                .where(RecordTagDao.Properties.IsDeleted.eq(0))
                .list() as ArrayList<RecordTag>
        view.setAdapterData(recordTags)
    }


    override fun insertMember(name: String) {
        if (name.isBlank()) {
            view.nameIsEmpty()
            return
        }
        if (!checkType(name)) {
            view.memberRepetition()
            return
        }
        val recordTag = RecordTag()
        recordTag.createTime = System.currentTimeMillis()
        recordTag.uuid = System.currentTimeMillis()
        recordTag.isDeleted = 0
        recordTag.listId = 1
        recordTag.tagName = name
        RecordTagTool.insert(recordTag)
    }

    override fun deleteMember(member: RecordTag) {
        RecordTagTool.delete(member)
    }

    override fun onInsertEntity(entity: RecordTag) {
        recordTags.add(entity)
        view.insertData(entity)
    }

    override fun onUpdateEntity(oldEntity: RecordTag, newEntity: RecordTag) {
        recordTags.forEachWithIndex { i, recordTag ->
            if (recordTag.uuid == newEntity.uuid) {
                recordTags[i] = recordTag
                view.updateData(i, newEntity)
                return
            }
        }
    }


    override fun onDeleteEntity(entity: RecordTag) {
        recordTags.remove(entity)
        view.removeData(entity)
    }

    override fun checkType(name: String): Boolean {
        return DaoManager.getRecordTagDao().queryBuilder()
                .where(RecordTagDao.Properties.TagName.eq(name))
                .where(RecordTagDao.Properties.IsDeleted.eq(0))
                .unique() == null
    }
}