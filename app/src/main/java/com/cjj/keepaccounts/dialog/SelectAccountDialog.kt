package com.cjj.keepaccounts.dialog

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.text.inSpans
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.bean.Account
import com.cjj.keepaccounts.bean.AccountDao
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.manager.LogoManager
import com.cjj.keepaccounts.utils.Utils
import com.cjj.keepaccounts.utils.Utils.context
import com.cjj.keepaccounts.utils.toMoney
import com.cjj.keepaccounts.view.drawableEnd
import com.cjj.keepaccounts.view.drawableStart
import org.jetbrains.anko.backgroundResource


/**
 * @author chenjunjie
 * Created by CJJ on 2018/2/7 15:48.
 */
class SelectAccountDialog(context: Context, val name: String) {

    private var tvTitle: TextView
    private var flContent: FrameLayout
    private var tvPositive: TextView

    private lateinit var rgSelectAccount: RadioGroup

    private val mBottomSheetDialog = BottomSheetDialog(context)

    private var listener: (Account) -> Unit = {}

    fun setOnAccountChangeListener(listener: (Account) -> Unit) {
        this.listener = listener
    }

    init {
        val view = View.inflate(context, R.layout.dialog_base_view, null)
        tvTitle = view.findViewById(R.id.tv_title)
        tvPositive = view.findViewById(R.id.tv_positive)
        flContent = view.findViewById(R.id.fl_content)
        flContent.addView(setContentView())
        mBottomSheetDialog.setContentView(view)
        tvPositive.visibility = View.GONE
        tvTitle.visibility = View.VISIBLE
        tvTitle.text = context.getText(R.string.select_account)
        tvTitle.backgroundResource = R.drawable.shape_bottom_e7_line
    }

    private lateinit var accountList: MutableList<Account>


    private fun setContentView(): View {
        val view = Utils.inflate(R.layout.dialog_select_account_dialog, context)

        view.layoutParams = WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        rgSelectAccount = view.findViewById(R.id.rg_select_account)


        accountList = DaoManager.getAccountDao().queryBuilder()
                .where(AccountDao.Properties.IsDeleted.eq(0))
                .where(AccountDao.Properties.TypeId.notEq(7))
                .where(AccountDao.Properties.TypeId.notEq(8))
                .orderAsc(AccountDao.Properties.OrderIndex)
                .list()

        var index = 0
        for ((i, it) in accountList.withIndex()) {
            if (name == it.name) {
                index = i
            }
            rgSelectAccount.addView(getItem(it))
        }
        if (rgSelectAccount.childCount > 0) {
            rgSelectAccount.check(rgSelectAccount.getChildAt(index).id)
        }
//        rgSelectAccount.setOnCheckedChangeListener { group, checkedId ->
//
//        }
        return view
    }

//    val layoutParams = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.getDimension(R.dimen.select_account_item_height))

    private fun getItem(account: Account): RadioButton {

        val radioButton = Utils.inflate(R.layout.list_item_select_account, context) as RadioButton
        radioButton.layoutParams = RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.getDimension(R.dimen.select_account_item_height))
        radioButton.setTextColor(Utils.getColor(R.color.text_color_655f5f))
        val drawable = Utils.getDrawable(LogoManager.getHomeLogo(account.accountType.idImg))
        drawable.setBounds(0, 0, Utils.dip2px(30), Utils.dip2px(30))
        radioButton.drawableStart = drawable
        radioButton.drawableEnd = Utils.getDrawable(R.drawable.selector_account_check_box_drawable)


        val str = when (account.typeId) {
            3, 12, 13 -> {
                "\n剩余额度:${(account.creditLimit + account.money).toMoney()}\t欠款:${(-account.money).toMoney()}"
            }
            else -> {
                "\n余额:${account.money.toMoney()}"
            }
        }
        val builder = SpannableStringBuilder(account.name)
        builder.inSpans(AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12)), ForegroundColorSpan(Utils.getColor(R.color.text_color_a19e9d))) {
            append(str)
        }
        radioButton.text = builder
        radioButton.backgroundResource = R.drawable.ripple_dialog_select_account_bg_click_white
        radioButton.setOnClickListener {
            listener.invoke(accountList[rgSelectAccount.indexOfChild(it)])
            dismiss()
        }
        return radioButton
    }

    fun show() {
        if (!mBottomSheetDialog.isShowing) {
            mBottomSheetDialog.show()
        }
    }

    fun dismiss() {
        if (mBottomSheetDialog.isShowing) {
            mBottomSheetDialog.dismiss()
        }
    }

}