package com.cjj.keepaccounts.mvp.activity.statement.settingshowclassify

import com.cjj.keepaccounts.activity.statement.SettingShowClassifyActivity
import com.cjj.keepaccounts.bean.AppConfig
import com.cjj.keepaccounts.bean.AppConfigDao
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.dao.AppConfigTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.TimeUtils
import javax.inject.Inject

/**
 * @author CJJ
 * Created by CJJ on 2018/8/9 15:00.
 */
class PSettingShowClassify @Inject constructor(view: SettingShowClassifyActivity, model: MSettingShowClassify) : CSettingShowClassify.Presenter(view, model) {

    override fun presenter() {
        model.getData().subscribe {
            it.first.forEach { (l, appConfig) ->
                model.checkTypeMap[l] = appConfig
            }
            view.setData(it.second)
        }
    }

    override fun update(data: ArrayList<RecordType>) {
        val addList = arrayListOf<AppConfig>()
        val updateOldList = arrayListOf<AppConfig>()
        val updateList = arrayListOf<AppConfig>()
        data.forEach {
            if (it.isCheck) {
                val remove = model.checkTypeMap.remove(it.showId)
                if (remove == null) {
                    val oldAppConfig = DaoManager.getAppConfigDao().queryBuilder()
                            .where(AppConfigDao.Properties.Uuid.eq(it.showId))
                            .unique()
                    if (oldAppConfig == null) {
                        val appConfig = AppConfig()
                        appConfig.cTime = TimeUtils.second
                        appConfig.confVal2 = ""
                        appConfig.confVal3 = ""
                        appConfig.confVal = "1"
                        appConfig.isDeleted = 0
                        appConfig.userId = ""
                        appConfig.deviceId = ""
                        appConfig.mTime = TimeUtils.timeOfSecond
                        appConfig.uuid = it.showId
                        appConfig.confKey = "record_type.is_show:list_id=1&uuid=${it.showId}"
                        addList.add(appConfig)
                    } else {
                        val appConfig = oldAppConfig.clone()
                        updateOldList.add(oldAppConfig)
                        appConfig.confVal = "1"
                        updateList.add(appConfig)
                    }
                }
            }
        }
        if (addList.size > 0) {
            AppConfigTool.rangeInsert(addList)
        }
        if (updateList.size > 0) {
            AppConfigTool.rangeUpdate(updateOldList, updateList)
        }
        if (model.checkTypeMap.size > 0) {
            val removeList = arrayListOf<AppConfig>()
            model.checkTypeMap.forEach { (_, u) ->
                u.mTime = TimeUtils.timeOfSecond
                removeList.add(u)
            }
            AppConfigTool.rangeDelete(removeList)
        }
    }

}