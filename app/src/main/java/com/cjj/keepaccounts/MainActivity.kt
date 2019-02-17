package com.cjj.keepaccounts

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.MotionEvent
import com.cjj.keepaccounts.activity.bill.NewBillRecordActivity
import com.cjj.keepaccounts.adapter.ViewPagerAdapter
import com.cjj.keepaccounts.application.MyApplication
import com.cjj.keepaccounts.base.BaseSlideCloseActivity
import com.cjj.keepaccounts.base.empty.EmptyPresenter
import com.cjj.keepaccounts.fragment.bill.BillFragment
import com.cjj.keepaccounts.fragment.property.PropertyFragment
import com.cjj.keepaccounts.fragment.statement.StatementFragment
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.drawableTop
import com.cjj.keepaccounts.view.setRadioGroup
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundDrawable
import javax.inject.Inject


class MainActivity : BaseSlideCloseActivity<EmptyPresenter>(), HasSupportFragmentInjector {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.backgroundDrawable = null
        super.onCreate(savedInstanceState)
        setWhiteStatusBar(true)
        setContentView(R.layout.activity_main)
        view_title.layoutParams.height = Utils.getTitleHeight()
//        //延时50ms加载子页面
//        Observable.timer(50, TimeUnit.MILLISECONDS)
//                .observeOnMain()
//                .subscribe {
//                    init()
//                }
        MyApplication.registerDatabaseInitCallbacks {
            init()
        }
    }

    private fun init() {

        val propertyFragment = PropertyFragment()
        val billFragment = BillFragment()
        val statementFragment = StatementFragment()
        val fragmentList = arrayListOf(
                propertyFragment,
                billFragment,
                statementFragment
        )
        //设置页面缓存数量
        vp_main.offscreenPageLimit = 2
        vp_main.adapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
        //与底部的RadioGroup绑定
        vp_main.setRadioGroup(rg_main_bottom)
        //选中第二个页面
        vp_main.currentItem = 1

        initListener()
        MyApplication.mark.printTime("首页启动")


    }

    private fun initListener() {

        rb_bill.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.text = ""
                buttonView.drawableTop = Utils.getDrawable(R.mipmap.jiyibi)
                buttonView.gravity = Gravity.CENTER
            } else {
                buttonView.drawableTop = Utils.getDrawable(R.mipmap.zhangdan_close)
                buttonView.text = getString(R.string.bill)
                buttonView.gravity = Gravity.CENTER_HORIZONTAL
                buttonView.setOnClickListener(null)
            }
        }
        rb_bill.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downTime = System.currentTimeMillis()
                }
                MotionEvent.ACTION_UP -> {
                    if (downTime < System.currentTimeMillis() + 200) {
                        if (rb_bill.isChecked) {
                            startActivity(Intent(this, NewBillRecordActivity::class.java))
                            overridePendingTransition(R.anim.activity_in_bottom, 0)
                        } else {
                            rg_main_bottom.check(rb_bill.id)
                        }
                    }
                }
                else -> {

                }
            }
            true
        }
    }

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    private var downTime = 0L


    override fun isSupportSwipeBack(): Boolean = false


}