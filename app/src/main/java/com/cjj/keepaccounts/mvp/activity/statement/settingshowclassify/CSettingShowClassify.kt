package com.cjj.keepaccounts.mvp.activity.statement.settingshowclassify

import com.cjj.keepaccounts.activity.statement.SettingShowClassifyActivity
import com.cjj.keepaccounts.base.BasePresenter
import com.cjj.keepaccounts.base.IModel
import com.cjj.keepaccounts.base.IView
import com.cjj.keepaccounts.bean.AppConfig
import com.cjj.keepaccounts.bean.RecordType
import rx.Observable

/**
 * @author CJJ
 * Created by CJJ on 2018/8/9 15:00.
 */
interface CSettingShowClassify {
    abstract class Presenter(view: SettingShowClassifyActivity, model: MSettingShowClassify) : BasePresenter<SettingShowClassifyActivity, MSettingShowClassify>(view, model) {
        abstract fun update(data: ArrayList<RecordType>)
    }

    interface View : IView {
        fun setData(data: List<RecordType>)
    }

    interface Model : IModel {
        fun getData(): Observable<Pair<HashMap<Long, AppConfig>, List<RecordType>>>
        val checkTypeMap: HashMap<Long, AppConfig>
    }
}