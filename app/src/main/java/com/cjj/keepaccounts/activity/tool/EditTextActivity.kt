package com.cjj.keepaccounts.activity.tool

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowManager
import androidx.core.text.inSpans
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.ActivityJumpInfo
import com.cjj.keepaccounts.enum.InputType
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.CalculatorView
import kotlinx.android.synthetic.main.activity_edit_text.*
import org.jetbrains.anko.textColorResource

class EditTextActivity : WhiteActivity<EmptyPresenter>() {


    lateinit var info: ActivityJumpInfo

    private val moneySpan = SpannableStringBuilder()
    private val colorSpan = ForegroundColorSpan(Utils.getColor(R.color.text_color_a39f9f))
    private val sizeSpan25 = AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size25))
    private val sizeSpan12 = AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12))

    override fun getContentView(): View = Utils.inflate(R.layout.activity_edit_text, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showTitleLine()
        setNextText(getString(R.string.ok))
        if (info.inputType == InputType.TEXT.type) {
            calculator.visibility = View.GONE
            et_text.textColorResource = R.color.text_color_655f5f
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        } else {
            et_text.isEnabled = false
            setNavigationBarColor(Utils.getColor(R.color.text_color_655f5f))
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        }
    }

    override fun initView() {
        info = intent.getParcelableExtra("info")
        setActivityBackText(info.backStr)
        setActivityTitleText(info.titleStr)
        et_text.setText(info.contentStr)
        if (info.inputType == InputType.TEXT.type) {
            et_text.setSelection(0, info.contentStr.length)
        } else {
            val builder = SpannableStringBuilder()
            builder.inSpans(ForegroundColorSpan(Utils.getColor(R.color.text_color_655f5f))) {
                append(info.contentStr)
            }
            et_text.text = builder
            calculator.setResult(info.contentStr)
        }
    }


    override fun initListener() {
        super.initListener()
        calculator.listener = object : CalculatorView.OnCalculatorListener {
            override fun calculatorChange(equation: String, nums: ArrayList<String>, result: SpannableStringBuilder) {
                moneySpan.clear()
                if (equation.contains('+')
                        || (equation.contains('-') && nums.size != 1)) {
                    moneySpan.append(result)
                    moneySpan.setSpan(sizeSpan25, 0, result.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    moneySpan.inSpans(sizeSpan12, colorSpan) {
                        append("\n")
                        append(equation)
                    }
                    et_text.text = moneySpan
                } else {
                    et_text.text = result
                }

            }

            override fun result(result: SpannableStringBuilder) {
                et_text.text = result
            }

            override fun finish(result: SpannableStringBuilder) {
                onNext()
            }

        }
    }

    override fun onNext() {
        super.onNext()
        val intent = Intent()
        intent.putExtra("contentStr", et_text.text.toString())
        setResult(info.resultCode, intent)
        finish()
    }

    companion object {
        fun openActivity(info: ActivityJumpInfo) {
            val activity = ActivityTool.currentActivity()
            val intent = Intent(activity, EditTextActivity::class.java)
            intent.putExtra("info", info)
            activity.startActivityForResult(intent, info.requestCode)
        }
    }

}
