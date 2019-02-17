package com.cjj.keepaccounts.activity.tool

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.TransparencyActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.utils.Utils
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.*

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/15 17:01.
 */
class EditBillRecordContentActivity : TransparencyActivity<EmptyPresenter>() {
    private lateinit var etContent: EditText
    private lateinit var ivClose: ImageView
    private lateinit var ivOk: ImageView
    private lateinit var relativeLayout: RelativeLayout

    private lateinit var record: Record


    @Suppress("EXPERIMENTAL_FEATURE_WARNING")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        record = intent.getParcelableExtra("info")
        relativeLayout = relativeLayout {
            fitsSystemWindows = true
            backgroundColor = Utils.getColor(R.color.bg_color_80000000)
            linearLayout {
                orientation = LinearLayout.VERTICAL
                backgroundColor = Color.WHITE


                etContent = editText {
                    backgroundResource = R.drawable.shape_bottom_e7_line
                    hintTextColor = Utils.getColor(R.color.text_color_b2afaf)
                    textColor = Utils.getColor(R.color.text_color_655f5f)
                    padding = Utils.getDimension(R.dimen.start)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getDimension(R.dimen.text_size20).toFloat())
                    gravity = Gravity.TOP
                    hint = getString(R.string.input_content)
                    if (!TextUtils.isEmpty(record.content)) {
                        setText(record.content)
                        setSelection(0, record.content.length)
                    }

                }.lparams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(100F))

                frameLayout {
                    ivClose = imageView {
                        imageResource = R.mipmap.icon_circle_close
                    }.lparams(Utils.dip2px(30F), Utils.dip2px(30F), Gravity.START or Gravity.CENTER_VERTICAL) {
                        marginStart = Utils.dip2px(100F)
                    }

                    ivOk = imageView {
                        imageResource = R.mipmap.icon_circle_gou
                    }.lparams(Utils.dip2px(30F), Utils.dip2px(30F), Gravity.END or Gravity.CENTER_VERTICAL) {
                        marginEnd = Utils.dip2px(100F)
                    }

                }.lparams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(50F)) {}


            }.lparams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(150F)) {
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }
        }

        initListener()
    }

    private fun initListener() {
        ivOk.setOnClickListener {
            EventBus.getDefault().post(etContent.text.toString())
            finish()
        }
        ivClose.setOnClickListener { finish() }
        relativeLayout.setOnClickListener { finish() }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.activity_out_alpha)
    }

    override fun isSupportSwipeBack(): Boolean = false
}