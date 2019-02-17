package com.cjj.keepaccounts.activity.bill

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.EditClassifyAdapter
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.bean.ActivityInfoBean
import com.cjj.keepaccounts.dao.RecordTypeTool
import com.cjj.keepaccounts.dialog.MsgDialog
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.mvp.activity.bill.editclassify.CEditClassify
import com.cjj.keepaccounts.mvp.activity.bill.editclassify.PEditClassify
import com.cjj.keepaccounts.utils.RecyclerSpace
import com.cjj.keepaccounts.utils.Utils
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/8 14:55.
 *
 * 编辑支入支出分类的页面
 *
 */
class EditClassifyActivity : WhiteActivity<PEditClassify>(), CEditClassify.View {


    private lateinit var recyclerView: RecyclerView


    override val adapter: EditClassifyAdapter = EditClassifyAdapter()

    private val deleteDialog: MsgDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = MsgDialog(this)
        dialog.setTitle(Utils.getString(R.string.delete_record_hint))
        dialog.setNegativeListener(null)
        dialog.message = Utils.getString(R.string.delete_record_affirm_hint)
        dialog
    }


    override fun getContentView(): View {
        recyclerView = recyclerView {
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            backgroundColorResource = R.color.white
            layoutManager = LinearLayoutManager(this@EditClassifyActivity)
            addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e7)))
            adapter = this@EditClassifyActivity.adapter
        }
        return recyclerView
    }

    override fun initView() {
        showTitleLine()
    }

    override fun initListener() {
        super.initListener()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState && adapter.isOpen()) {
                    adapter.close()
                }
            }
        })
        adapter.setOnItemClickListener { _, _, itemInfo ->
            val activityInfoBean = ActivityInfoBean(Utils.getString(R.string.edit_category),
                    Utils.getString(if (presenter.type == 0) R.string.edit_income_category else R.string.edit_expend_category),
                    next = Utils.getString(R.string.ok))
            ActivityTool.skipActivity<EditClassifyDetailsActivity>(itemInfo, activityInfoBean)
        }
        adapter.setOnDeleteListener { _, itemInfo ->
            deleteDialog.setPositiveListener {
                RecordTypeTool.delete(itemInfo)
            }.show()
        }
    }


    override fun onNext() {
        super.onNext()
        val activityInfoBean = ActivityInfoBean(getString(R.string.new_category), getString(R.string.edit_classify), next = getString(R.string.ok))
        val recordType = presenter.getNewRecordType()
        ActivityTool.skipActivity<EditClassifyDetailsActivity>(recordType, activityInfoBean)
    }

}
