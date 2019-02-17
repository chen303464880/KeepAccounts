package com.cjj.keepaccounts.activity.bill

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.tool.EditTextActivity
import com.cjj.keepaccounts.adapter.MemberAdapter
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.bean.ActivityJumpInfo
import com.cjj.keepaccounts.dao.RecordTagTool
import com.cjj.keepaccounts.dialog.MsgDialog
import com.cjj.keepaccounts.enum.InputType
import com.cjj.keepaccounts.mvp.activity.bill.editmember.CEditMember
import com.cjj.keepaccounts.mvp.activity.bill.editmember.PEditMember
import com.cjj.keepaccounts.utils.RecyclerSpace
import com.cjj.keepaccounts.utils.ToastUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.addTextChangedListener
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView


/**
 * @author chenjunjie
 * Created by CJJ on 2018/4/2 10:57.
 */
class EditMemberActivity : WhiteActivity<PEditMember>(), CEditMember.View {


    private lateinit var recyclerView: RecyclerView

    override val adapter: MemberAdapter = MemberAdapter()
    private lateinit var etMember: EditText
    private lateinit var btnOk: Button


    private val deleteDialog: MsgDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = MsgDialog(this)
        dialog.setTitle(Utils.getString(R.string.delete_record_hint))
        dialog.setNegativeListener(null)
        dialog.message = Utils.getString(R.string.delete_record_affirm_hint)
        dialog
    }

    override fun getContentView(): View =
            linearLayout {
                backgroundColorResource = R.color.bg_color_f7
                orientation = LinearLayout.VERTICAL
                recyclerView = recyclerView {
                    overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                    backgroundResource = R.drawable.shape_top_e7_line
                    layoutManager = LinearLayoutManager(this@EditMemberActivity)
                    adapter = this@EditMemberActivity.adapter
                    addItemDecoration(RecyclerSpace(1, Utils.getColor(R.color.divider_color_e7)))
                }.lparams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1F)

                linearLayout {
                    backgroundResource = R.drawable.shape_vertical_e7_line
                    etMember = editText {
                        backgroundColorResource = R.color.white
                        hint = getString(R.string.new_member)
                        horizontalPadding = Utils.getDimension(R.dimen.start)
                        gravity = Gravity.CENTER_VERTICAL
                        hintTextColor = Utils.getColor(R.color.text_color_a39f9f)
                        textColorResource = R.color.text_color_655f5f
                    }.lparams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1F)
                    btnOk = button(getString(R.string.ok)) {
                        gravity = Gravity.CENTER
                        isEnabled = false
                        textColorResource = R.color.white
                        backgroundDrawable = Utils.getDrawable(R.drawable.selector_new_member_bg_color)
                    }.lparams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
                }.lparams(LinearLayout.LayoutParams.MATCH_PARENT, dip(50))
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityBackText(getString(R.string.member))
        setActivityTitleText(getString(R.string.member_setting))
        showTitleLine()

    }

    override fun initView() {
        presenter.presenter()
    }


    override fun initListener() {
        super.initListener()
        etMember.addTextChangedListener { _, _, _, _ ->
            btnOk.isEnabled = etMember.text.isNotEmpty()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && adapter.isOpen()) {
                    adapter.close()
                }
            }
        })

        adapter.setOnDeleteListener { _, recordTag ->
            deleteDialog.setPositiveListener {
                presenter.deleteMember(recordTag)
            }.show()
        }

        adapter.setOnItemClickListener { _, position, itemInfo ->
            val info = ActivityJumpInfo(position, Activity.RESULT_OK, InputType.TEXT.type, Utils.getString(R.string.member_setting), "成员名称修改", itemInfo.tagName
                    ?: "")
            EditTextActivity.openActivity(info)
        }
        btnOk.setOnClickListener {
            presenter.insertMember(etMember.text.toString())
            etMember.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }
    }

    override fun nameIsEmpty() {
        ToastUtils.shortToast("请输入名称")
    }

    override fun memberRepetition() {
        ToastUtils.shortToast("已存在该成员")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val name = data.getStringExtra("contentStr")
            if (!presenter.checkType(name)) {
                ToastUtils.shortToast("已存在该成员")
                return
            }
            val recordTag = adapter.getItem(requestCode)
            val newRecordTag = recordTag.clone()
            newRecordTag.tagName = name
            RecordTagTool.update(recordTag, newRecordTag)
        }
    }


}