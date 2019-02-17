package com.cjj.keepaccounts.mvp.activity.bill.editclassify

import com.cjj.keepaccounts.activity.bill.EditClassifyActivity
import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.base.empty.AdapterView
import com.cjj.keepaccounts.bean.RecordType

/**
 * Created by CJJ on 2019/2/1 16:30.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
interface CEditClassify {

    abstract class Presenter(view: EditClassifyActivity, model: BaseModel) : BasePresenter<EditClassifyActivity, BaseModel>(view, model) {
        abstract var type: Int
        abstract fun getNewRecordType(): RecordType
    }

    interface View : IView, AdapterView<RecordType>
}