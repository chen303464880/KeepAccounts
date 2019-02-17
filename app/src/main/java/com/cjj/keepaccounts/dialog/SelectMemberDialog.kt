package com.cjj.keepaccounts.dialog

import android.content.Context
import android.graphics.drawable.StateListDrawable
import android.support.design.widget.BottomSheetDialog
import android.view.Gravity
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.bill.EditMemberActivity
import com.cjj.keepaccounts.bean.RecordTag
import com.cjj.keepaccounts.bean.RecordTagDao
import com.cjj.keepaccounts.dao.RecordTagTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.utils.Utils.context
import com.cjj.keepaccounts.view.drawableEnd
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.horizontalPadding
import org.jetbrains.anko.textColorResource


/**
 * @author chenjunjie
 * Created by CJJ on 2018/2/7 15:48.
 */
class SelectMemberDialog(context: Context, var members: HashMap<Long, RecordTag>) {


    private val tvEdit: TextView
    private val tvOk: TextView
    private val llContent: LinearLayout

    private val mBottomSheetDialog = BottomSheetDialog(context)

    private var listener: (members: HashMap<Long, RecordTag>) -> Unit = {}

    private lateinit var recordTags: ArrayList<RecordTag>

    fun setOnAccountChangeListener(listener: (HashMap<Long, RecordTag>) -> Unit) {
        this.listener = listener
    }

    init {
        val view = View.inflate(context, R.layout.dialog_select_member, null)
        tvEdit = view.findViewById(R.id.tv_edit)
        tvOk = view.findViewById(R.id.tv_ok)
        llContent = view.findViewById(R.id.ll_content)
        initData()
        initListener()
        mBottomSheetDialog.setContentView(view)
    }

    private fun initData() {
        recordTags = DaoManager.getRecordTagDao().queryBuilder()
                .where(RecordTagDao.Properties.IsDeleted.eq(0))
                .list() as ArrayList<RecordTag>
        recordTags.forEach {
            val checkBox = getCheckBox(it)
            llContent.addView(checkBox)
        }
    }


    private fun initListener() {
        tvEdit.setOnClickListener {
            ActivityTool.skipActivity<EditMemberActivity>()
        }
        tvOk.setOnClickListener {

            listener.invoke(members)
            dismiss()

        }
        mBottomSheetDialog.setOnDismissListener {
            RecordTagTool.removeOnDaoChangeListener(recordTagChangeListener)
        }
    }

    private val recordTagChangeListener = object : OnDaoChangeListener<RecordTag> {
        override fun onInsertEntity(entity: RecordTag) {
            recordTags.add(entity)
            val checkBox = getCheckBox(entity)
            llContent.addView(checkBox)
        }


        override fun onUpdateEntity(oldEntity: RecordTag, newEntity: RecordTag) {
            recordTags.forEachWithIndex { i, recordTag ->
                if (recordTag.uuid == newEntity.uuid) {
                    recordTags[i] = newEntity
                    val checkBox = llContent[i] as CheckBox
                    checkBox.text = newEntity.tagName
                    return
                }
            }
        }

        override fun onDeleteEntity(entity: RecordTag) {
            for ((index, it) in recordTags.withIndex()) {
                if (it.uuid == entity.uuid) {
                    recordTags.remove(it)
                    llContent.removeViewAt(index)
                    members.remove(it.uuid)
                    break
                }
            }
        }
    }

    private fun getCheckBox(it: RecordTag): CheckBox {
        val checkBox = CheckBox(context)
        checkBox.text = it.tagName
        checkBox.textColorResource = R.color.text_color_655f5f
        checkBox.horizontalPadding = Utils.getDimension(R.dimen.start)
        checkBox.gravity = Gravity.CENTER_VERTICAL
        checkBox.buttonDrawable = null
        checkBox.backgroundResource = R.drawable.ripple_bg_click_white
        checkBox.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(45F))
        val rightDrawable = Utils.getDrawable(R.drawable.selector_account_check_box_drawable) as StateListDrawable
        checkBox.drawableEnd = rightDrawable
        val recordTag = members[it.uuid]
        checkBox.isChecked = recordTag != null
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                members[it.uuid] = it
            } else {
                members.remove(it.uuid)
            }
        }
        return checkBox
    }

    fun show() {
        RecordTagTool.addOnDaoChangeListener(recordTagChangeListener, null)
        if (!mBottomSheetDialog.isShowing) {
            mBottomSheetDialog.show()
        }
    }

    private fun dismiss() {
        if (mBottomSheetDialog.isShowing) {
            mBottomSheetDialog.dismiss()
        }
    }

}