package com.cjj.keepaccounts.mvp.activity.bill.editmember

import com.cjj.keepaccounts.activity.bill.EditMemberActivity
import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.base.empty.AdapterView
import com.cjj.keepaccounts.bean.RecordTag

/**
 * Created by CJJ on 2019/2/1 14:45.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
interface CEditMember {

    abstract class Presenter(view: EditMemberActivity, model: BaseModel) : BasePresenter<EditMemberActivity, BaseModel>(view, model) {
        abstract fun insertMember(name: String)
        abstract fun deleteMember(member: RecordTag)
        abstract fun checkType(name: String): Boolean
    }

    interface View : IView, AdapterView<RecordTag> {
        fun nameIsEmpty()
        fun memberRepetition()
    }


}