package com.cjj.keepaccounts.activity.bill

import android.support.annotation.DrawableRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.ClassifyLogoAdapter
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.RecordType
import com.cjj.keepaccounts.bean.RecordTypeDao
import com.cjj.keepaccounts.dao.RecordTypeTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.manager.LogoManager
import com.cjj.keepaccounts.utils.ToastUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.drawableStart
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/8 14:50.
 *
 * 编辑支入支出的分类的详情的页面
 */
class EditClassifyDetailsActivity : WhiteActivity<EmptyPresenter>() {

    private lateinit var llContent: LinearLayout
    private lateinit var etClassify: EditText
    private lateinit var rvClassify: RecyclerView
    private val adapter: ClassifyLogoAdapter = ClassifyLogoAdapter()

    /**
     * 分类类型
     */
    private val recordType: RecordType by extra()


    override fun getContentView(): View {
        llContent = linearLayout {
            orientation = LinearLayout.VERTICAL
            etClassify = editText {
                backgroundResource = R.drawable.shape_bottom_e7_line
                setPadding(Utils.getDimension(R.dimen.start), 0, Utils.getDimension(R.dimen.start), 0)
                compoundDrawablePadding = Utils.getDimension(R.dimen.start)
                gravity = Gravity.CENTER_VERTICAL
                textColorResource = R.color.text_color_655f5f
                maxLines = 1
                backgroundColorResource = R.color.white
                hint = getString(R.string.input_classify_name)
                hintTextColor = Utils.getColor(R.color.text_color_b2afaf)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getDimension(R.dimen.text_size16).toFloat())
            }.lparams(width = LinearLayout.LayoutParams.MATCH_PARENT, height = Utils.dip2px(60F))

            rvClassify = recyclerView {
                overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                backgroundColorResource = R.color.bg_color_f7
                adapter = this@EditClassifyDetailsActivity.adapter
                layoutManager = GridLayoutManager(this@EditClassifyDetailsActivity, 5)
            }.lparams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        }
        return llContent
    }


    override fun initView() {
        showTitleLine()

        adapter.setData(LogoManager.Logo.logo)



        if (recordType.typeDesc.isNullOrBlank()) {
            val logoInfo = LogoManager.Logo.logo.first()
            setClassifyLogo(logoInfo.imgRes)
            recordType.color = logoInfo.color
            recordType.imgSrcId = logoInfo.imgIndex
        } else {
            setClassifyLogo(LogoManager.getTypeLogo(recordType.imgSrcId))
            etClassify.setText(recordType.typeDesc)
            etClassify.setSelection(0, etClassify.length())
        }
    }

    override fun initListener() {
        super.initListener()
        adapter.setOnItemClickListener { _, _, itemInfo ->
            recordType.color = itemInfo.color
            recordType.imgSrcId = itemInfo.imgIndex
            setClassifyLogo(itemInfo.imgRes)
        }
    }

    private fun setClassifyLogo(@DrawableRes logoRes: Int) {
        etClassify.drawableStart = Utils.getDrawable(logoRes, dip(42))
    }

    override fun onNext() {
        super.onNext()
        val name = etClassify.text.toString()
        if (name.isBlank()) {
            ToastUtils.shortToast("请输入名称")
            return
        }
        if (!checkType(name)) {
            ToastUtils.shortToast("已存在该分类")
            return
        }
        if (recordType.typeDesc.isNullOrBlank()) {
            recordType.typeDesc = name
            RecordTypeTool.insert(recordType)
        } else {
            val newRecordType = recordType.clone()
            newRecordType.typeDesc = name
            RecordTypeTool.update(recordType, newRecordType)
        }
        finish()

    }

    private fun checkType(name: String): Boolean {
        return DaoManager.getRecordTypeDao().queryBuilder()
                .where(RecordTypeDao.Properties.TypeDesc.eq(name))
                .where(RecordTypeDao.Properties.IsDeleted.eq(0))
                .unique() == null
    }


}