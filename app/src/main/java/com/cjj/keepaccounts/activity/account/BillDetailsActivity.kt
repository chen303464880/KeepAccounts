package com.cjj.keepaccounts.activity.account

import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.bill.NewBillRecordActivity
import com.cjj.keepaccounts.base.TitleActivity
import com.cjj.keepaccounts.bean.Record
import com.cjj.keepaccounts.bean.RecordDao
import com.cjj.keepaccounts.databinding.ActivityBillDetailsBinding
import com.cjj.keepaccounts.dialog.MsgDialog
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.mvp.activity.account.billdetails.CBillDetails
import com.cjj.keepaccounts.mvp.activity.account.billdetails.PBillDetails
import com.cjj.keepaccounts.utils.Utils
import kotlinx.android.synthetic.main.activity_bill_details.*

/**
 * @author CJJ
 * 显示单笔消费或者转账的页面
 */
class BillDetailsActivity : TitleActivity<PBillDetails>(), CBillDetails.View {

    private lateinit var dataBinding: ActivityBillDetailsBinding

    private val deleteDialog: MsgDialog by lazy(LazyThreadSafetyMode.NONE) {
        val dialog = MsgDialog(this)
        dialog.setTitle(Utils.getString(R.string.delete_record_hint))
        dialog.setNegativeListener(null)
        dialog.setPositiveListener {
            presenter.deleteRecord()
            finish()
        }
        dialog.message = Utils.getString(R.string.delete_record_affirm_hint)
        dialog
    }

    override fun getContentView(): View {
        dataBinding = Utils.inflateDataBinding(R.layout.activity_bill_details, this)
        return dataBinding.root
    }


    override fun initView() {
        setNextTextDrawable(Utils.getDrawable(R.mipmap.ic_delete))
        presenter.presenter()
    }


    override fun initListener() {
        super.initListener()
        tv_edit.setOnClickListener {
            ActivityTool.skipActivity<NewBillRecordActivity>(dataBinding.record!!)
            overridePendingTransition(R.anim.activity_in_bottom, 0)
        }
    }

    override fun setData(record: Record) {

        setActivityTitleColor(record.recordType.color)
        dataBinding.record = record
        record.__setDaoSession(DaoManager.daoSession)
        //设置账户名
        if (record.typeId != -1L) {
            tv_account_name.text = record.account.name
            tv_content.text = record.content
            //设置成员
            record.__setDaoSession(DaoManager.daoSession)
            val members = record.members
            if (members.isNotEmpty()) {
                val sb = StringBuilder()
                val size = members.size
                for (i in 0 until size) {
                    sb.append(members[i].member.tagName)
                    if (i != size - 1) {
                        sb.append("、")
                    }
                }
                tv_member.text = sb.toString()
            } else {
                tv_member.setText(R.string.no_member)
            }
        } else {
            val first = DaoManager.getRecordDao().queryBuilder()
                    .where(RecordDao.Properties.ActionId.eq(record.actionId))
                    .where(RecordDao.Properties.AccountId.notEq(record.accountId))
                    .unique()
            first.__setDaoSession(DaoManager.daoSession)
            if (record.rateMoney > 0) {
                tv_account_name.text = first.account.name
                tv_member.text = record.account.name
            } else {
                tv_account_name.text = record.account.name
                tv_member.text = first.account.name
            }
            tv_member_key.text = getString(R.string.income_account)
            tv_account_name_hint.text = getString(R.string.expenditure_account)
        }

    }


    override fun onNext() {
        super.onNext()
        deleteDialog.show()
    }


}
