package com.cjj.keepaccounts.base.empty

import com.cjj.keepaccounts.base.BaseModel
import com.cjj.keepaccounts.base.BasePresenter
import javax.inject.Inject

/**
 * @author CJJ
 * Created by CJJ on 2018/8/9 16:55.
 */
class EmptyPresenter @Inject constructor(view: EmptyView, model: BaseModel) : BasePresenter<EmptyView, BaseModel>(view, model)