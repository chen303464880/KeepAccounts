package com.cjj.keepaccounts.activity.statement

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.SettingShowClassifyAdapter
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.mvp.activity.statement.settingshowclassify.CSettingShowClassify
import com.cjj.keepaccounts.mvp.activity.statement.settingshowclassify.PSettingShowClassify
import com.cjj.keepaccounts.utils.RecyclerSpace
import com.cjj.keepaccounts.utils.Utils
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * @author CJJ
 * Created by CJJ on 2018/5/21 10:15.
 */
class SettingShowClassifyActivity : WhiteActivity<PSettingShowClassify>(), CSettingShowClassify.View {

    private lateinit var recyclerView: RecyclerView

    private val adapter = SettingShowClassifyAdapter()


    override fun getContentView(): View {
        recyclerView = recyclerView {
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            backgroundColorResource = R.color.white
        }
        return recyclerView
    }


    override fun initView() {
        showTitleLine()
        setActivityTitleText(Utils.getString(R.string.add_show_classify))
        setNextText(Utils.getString(R.string.ok))
        setActivityBackText(Utils.getString(R.string.cancel))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e7)))
        recyclerView.adapter = adapter

        presenter.presenter()
    }

    override fun setData(data: List<RecordType>) {
        adapter.insertData(data)
    }

    override fun onNext() {
        super.onNext()
        presenter.update(adapter.data)
        finish()
    }
}