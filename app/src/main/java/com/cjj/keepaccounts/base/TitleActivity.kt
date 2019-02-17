package com.cjj.keepaccounts.base

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.bean.ActivityInfoBean
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.drawableEnd
import com.cjj.keepaccounts.view.drawableStart
import org.jetbrains.anko.*


/**
 * @author CJJ
 * Created by CJJ on 2017/11/10 11:41.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 */

abstract class TitleActivity<P : BasePresenter<out IView, out IModel>> : BaseSlideCloseActivity<P>() {

    protected var nextTextColor: Int = Color.WHITE
        set(value) {
            if (isNext) {
                tvNext.setTextColor(value)
            }
            field = value
        }
    protected var mPageColor: Int = Color.WHITE

    private var lLayout: FrameLayout? = null
    private lateinit var fLayoutTitle: FrameLayout
    private lateinit var tvBack: TextView
    private lateinit var tvTitle: TextView
    private lateinit var vsNext: ViewStub
    private lateinit var butterKnife: Unbinder

    private var isNext = false
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_base)
        lLayout = frameLayout {
            fLayoutTitle = frameLayout {
                topPadding = Utils.getTitleHeight()
                tvBack = textView {
                    textSizeDimen = R.dimen.text_size16
                    textColorResource = R.color.white
                    gravity = Gravity.CENTER_VERTICAL
                    drawableStart = Utils.getDrawable(R.mipmap.fanhui_bai)
                    horizontalPadding = Utils.getDimension(R.dimen.start)
                }.lparams(height = ViewGroup.LayoutParams.MATCH_PARENT) {
                    gravity = Gravity.CENTER_VERTICAL
                }
                tvTitle = textView {
                    gravity = Gravity.CENTER
                    textColorResource = R.color.white
                    textSizeDimen = R.dimen.text_size19

                }.lparams(height = ViewGroup.LayoutParams.MATCH_PARENT) {
                    gravity = Gravity.CENTER
                }
                vsNext = viewStub {
                    inflatedId = R.id.tv_next
                    layoutResource = R.layout.view_baseactivity_next
                }.lparams(height = ViewGroup.LayoutParams.MATCH_PARENT) {
                    gravity = Gravity.CENTER_VERTICAL or Gravity.END
                }
            }.lparams(width = ViewGroup.LayoutParams.MATCH_PARENT, height = Utils.getTitleHeight() + Utils.getDimension(R.dimen.title_height))
        }
        val contentView = getContentView()
        contentView.layoutParams = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT).apply {
            topMargin = fLayoutTitle.layoutParams.height
        }
        lLayout!!.addView(contentView)
        butterKnife = ButterKnife.bind(this)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


        val it = intent.getParcelableExtra<ActivityInfoBean>("activityInfo")
        if (it != null) {
            setActivityTitleText(it.title)
            setActivityBackText(it.back)
            mPageColor = it.titleColor
            setActivityTitleColor(mPageColor)
            if (!TextUtils.isEmpty(it.next)) {
                showNext()
                setNextText(it.next!!)
            }
        }


        initView()

        initListener()
    }


    override fun onDestroy() {
        super.onDestroy()
        butterKnife.unbind()

    }

    override fun setContentView(view: View) {
        if (lLayout == null) {
            super.setContentView(view)
        }
    }

    abstract fun getContentView(): View
    abstract fun initView()
    protected open fun initListener() {
        tvBack.setOnClickListener {
            onBackPressed()
        }
    }


    /**
     * 修改activity标题栏及状态栏的颜色
     */
    open fun setActivityTitleColor(@ColorInt color: Int) {
        mPageColor = color
        fLayoutTitle.setBackgroundColor(color)
        setStatusBarColor(Color.TRANSPARENT)
    }

    /**
     * 修改标题栏的文字
     *
     * @param title 标题
     */
    fun setActivityTitleText(title: CharSequence) {
        tvTitle.text = title
    }

    /**
     * 修改标题栏的文字
     *
     * @param title 标题
     */
    fun setActivityTitleText(title: CharSequence, onclick: () -> Unit) {
        tvTitle.text = title
        tvTitle.setOnClickListener {
            onclick.invoke()
        }
    }

    fun getActivityTitleText(): CharSequence = tvTitle.text

    /**
     * 修改标题栏的文字颜色
     */
    fun setActivityTitleTextColor(@ColorInt color: Int) {
        tvTitle.setTextColor(color)
    }

    fun setActivityTitleBackground(@DrawableRes resId: Int) {
        fLayoutTitle.setBackgroundResource(resId)
    }

    fun setActivityTitleBackground(drawable: Drawable) {
        fLayoutTitle.background = drawable
    }


    open fun setActivityBackIc(drawable: Drawable) {
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        tvBack.drawableStart = drawable
    }

    open fun setActivityBackText(str: CharSequence) {
        tvBack.text = str
    }

    open fun setActivityBackColor(@ColorInt color: Int) {
        tvBack.setTextColor(color)
    }

    open fun showTitleLine() {
        fLayoutTitle.background = getDrawable(R.drawable.shape_bottom_e7_line)
    }


    private lateinit var tvNext: TextView
    private fun showNext() {
        isNext = true
        val view = vsNext.inflate()
        tvNext = view.findViewById(R.id.tv_next)
        tvNext.setTextColor(nextTextColor)
        tvNext.setOnClickListener {
            onNext()
        }
    }

    fun setNextText(str: CharSequence) {
        if (!isNext) {
            showNext()
        }
        tvNext.text = str
    }

    fun setNextTextDrawable(drawable: Drawable) {
        if (!isNext) {
            showNext()
        }
        tvNext.drawableEnd = drawable
    }


    open fun onNext() {

    }


}