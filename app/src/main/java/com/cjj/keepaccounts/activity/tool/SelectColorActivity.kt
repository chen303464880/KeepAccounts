package com.cjj.keepaccounts.activity.tool

import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.adapter.SelectColorAdapter
import com.cjj.keepaccounts.base.WhiteActivity
import com.cjj.keepaccounts.bean.SelectColorBean
import com.cjj.keepaccounts.bean.SelectColorInfo
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.mvp.activity.tool.selectcolor.CSelectColor
import com.cjj.keepaccounts.mvp.activity.tool.selectcolor.PSelectColor
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.view.clearItemAnimator
import kotlinx.android.synthetic.main.activity_select_color.*

class SelectColorActivity : WhiteActivity<PSelectColor>(), CSelectColor.View {

    private var color = 0
    private val info by extra<SelectColorInfo>()
    private val adapter = SelectColorAdapter()


    override fun getContentView(): View = Utils.inflate(R.layout.activity_select_color, this)

    override fun initView() {
        showTitleLine()

        iv_logo.setImageDrawable(Utils.getDrawable(info.idImg))

        //设置账户名
        iv_account_name.text = info.name

        //设置item背景
        color = info.color
        rl_content.background = getContentBg(color)


        rv_select_color.adapter = adapter
        rv_select_color.clearItemAnimator()
        rv_select_color.layoutManager = GridLayoutManager(this, 7)
        rv_select_color.addItemDecoration(itemDecoration)

        presenter.presenter(color)
    }

    override fun initListener() {
        super.initListener()
        adapter.setOnItemClickListener { _, _, itemInfo ->
            color = itemInfo.color
            rl_content.background = getContentBg(color)
        }
    }

    override fun setData(list: List<SelectColorBean>) {
        adapter.setData(list)
    }

    /**
     * 根据颜色动态生成内容布局的背景
     */
    private fun getContentBg(color: Int): Drawable {

        val drawable1 = GradientDrawable()
        drawable1.setColor(color)
        val radii = Utils.getDimension(R.dimen.radius).toFloat()
        drawable1.cornerRadius = radii

        val drawable2 = GradientDrawable()
        drawable2.setColor(Color.WHITE)
        drawable2.cornerRadii = floatArrayOf(0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F)
        drawable2.alpha = 0x33

        val drawable = LayerDrawable(arrayOf(drawable1, drawable2))
        drawable.setLayerInset(1, Utils.dip2px(8F), 0, 0, 0)
        return drawable
    }

    override fun finish() {
        setResult(info.resultCode, Intent().putExtra("color", color))
        super.finish()
    }


    private val itemDecoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            if (parent.getChildAdapterPosition(view) + 1 % 7 != 0) {
                outRect.right = Utils.dip2px(10F)
            }
            outRect.bottom = Utils.dip2px(10F)
        }
    }
    companion object {
        fun openActivity(info: SelectColorInfo) {
            val activity = ActivityTool.currentActivity()
            val intent = Intent(activity, SelectColorActivity::class.java)
            intent.putExtra("info", info)
            activity.startActivityForResult(intent, info.requestCode)
        }
    }
}
