package com.cjj.keepaccounts.base

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.ColorInt
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.utils.Delegate
import com.cjj.keepaccounts.utils.LogUtils
import com.cjj.keepaccounts.utils.TimeMarkUtils
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.ActivitySlideCloseLayout
import dagger.android.AndroidInjection
import javax.inject.Inject


/**
 * @author CJJ
 * Created by CJJ on 2017/11/10 11:54.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 */
@SuppressLint("Registered")
open class BaseSlideCloseActivity<P : BasePresenter<out IView, out IModel>> : AppCompatActivity(), IView {
    @Inject
    lateinit var presenter: P
    private lateinit var leftView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        lifecycle.addObserver(presenter)
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            try {
                val actionBarRoot = findViewById<ViewGroup>(R.id.action_bar_root)
                val layout = actionBarRoot.parent as FrameLayout
                layout.addView(Utils.inflateRecyclerViewItem(layout, R.layout.divider_navigation))
                window.navigationBarColor = Color.WHITE
            } catch (ex: Exception) {
                LogUtils.exception(ex)
            }
        } else {
            window.navigationBarColor = Color.WHITE
            window.navigationBarDividerColor = Utils.getColor(R.color.divider_color_e4e5e4)
        }

        ActivityTool.addActivity(this)
        initSlideBackClose()
    }


    override val mIntent: Intent
        get() = intent

    fun setStatusBarColor(@ColorInt color: Int) {
        window.statusBarColor = color
    }

    fun setNavigationBarColor(@ColorInt color: Int) {
        window.navigationBarColor = color
    }

    fun setWhiteStatusBar(isWhits: Boolean) {
        if (isWhits && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            setStatusBarColor(Color.BLACK)
        }
    }

    /**
     * 获取页面传值
     */
    override fun <T : Parcelable?> extra(key: String): Delegate<T> = Delegate(key)

    open fun <T : Parcelable?> getInfo(key: String): T = intent.getParcelableExtra<T>(key)

    private fun initSlideBackClose() {
        if (isSupportSwipeBack()) {
            val closeLayout = ActivitySlideCloseLayout(this)
            closeLayout.setOnSlideListener(object : ActivitySlideCloseLayout.SlideListener {
                override fun onPanelSlide(slideOffset: Float) {
                    leftView.translationX = Utils.evaluate(slideOffset, -Utils.widthPixels.toFloat(), 0.0f)
                    leftView.alpha = Utils.evaluate(slideOffset, 0.8f, 0.0f)
                }

                override fun onPanelOpened() {

                }

                override fun onPanelClosed() {
                    finish()
                    overridePendingTransition(0, 0)
                }
            })
            leftView = View(this)
            leftView.setBackgroundColor(Color.BLACK)
            leftView.alpha = 0.8F
            leftView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

            val decorView = window.decorView as ViewGroup
            // 右侧的内容视图
            val decorChild = decorView.getChildAt(0) as ViewGroup
//            decorChild.setBackgroundColor(Color.WHITE)
            decorView.removeView(decorChild)

            leftView.translationX = -Utils.widthPixels.toFloat()
            closeLayout.addView(leftView)
            closeLayout.addView(decorChild)
            decorView.addView(closeLayout)

            // 为 SlidingPaneLayout 添加内容视图
        }
    }

    /**
     * 是否开启滑动退出Activity
     *
     * @return Boolean
     */
    protected open fun isSupportSwipeBack(): Boolean {
        return true
    }

    override fun onDestroy() {

        ActivityTool.removeActivity(this)
        super.onDestroy()
        val markUtils = TimeMarkUtils.mark("内存释放检测")
        Utils.fixInputMethodManagerLeak(this)
        markUtils.printTime()

    }
}