package com.cjj.keepaccounts.fragment.bill

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.inSpans
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.activity.bill.EditClassifyActivity
import com.cjj.keepaccounts.adapter.NewBillRecordAdapter
import com.cjj.keepaccounts.adapter.databinding.typeLogo
import com.cjj.keepaccounts.bean.*
import com.cjj.keepaccounts.dao.RecordTypeTool
import com.cjj.keepaccounts.listener.OnDaoChangeListener
import com.cjj.keepaccounts.manager.ActivityTool
import com.cjj.keepaccounts.manager.DaoManager
import com.cjj.keepaccounts.utils.*
import com.cjj.keepaccounts.utils.Utils.inflate
import com.cjj.keepaccounts.view.SpringBackLayout
import kotlinx.android.synthetic.main.fragment_new_bill_record.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Suppress("UNUSED_EXPRESSION")
/**
 * @author chenjunjie
 * Created by CJJ on 2018/2/28 14:25.
 */
class NewBillRecordFragment : RecordTypeFragment() {

    private lateinit var adapter: NewBillRecordAdapter
    private var type = 0
    private lateinit var record: Record


    private val moneySpan = SpannableStringBuilder()
    private val colorSpan = ForegroundColorSpan(Utils.getColor(R.color.text_color_a39f9f))
    private val sizeSpan25 = AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size25))
    private val sizeSpan12 = AbsoluteSizeSpan(Utils.getDimension(R.dimen.text_size12))


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflate(R.layout.fragment_new_bill_record, activity!!)
    }

    override fun initView() {
        EventBusUtils.register(this, presenter)
        RecordTypeTool.addOnDaoChangeListener(daoChangeListener, presenter)

        adapter = NewBillRecordAdapter()
        type = arguments?.getInt("type") ?: 0
        record = arguments?.getParcelable("info") ?: Record()

        val layoutManager = GridLayoutManager(context, 5)
        rv_new_bill_record.layoutManager = layoutManager
        rv_new_bill_record.adapter = adapter

        //创建SimpleItemTouchHelperCallback
        val callback = SimpleItemTouchHelperCallback(adapter)
        //用Callback构造ItemtouchHelper
        val touchHelper = ItemTouchHelper(callback)
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(rv_new_bill_record)

        DaoManager.getRecordTypeDao().queryBuilder()
                .where(RecordTypeDao.Properties.ListId.eq(1))
                .where(RecordTypeDao.Properties.IsIncoming.eq(type))
                .where(RecordTypeDao.Properties.IsDeleted.eq(0))
                .where(RecordTypeDao.Properties.ImgSrcId.ge(0))
                .orderAsc(RecordTypeDao.Properties.OrderIndex)
                .rx().list()
                .observeOnMain()
                .subscribe { list ->
                    adapter.setData(list)

                    record.__setDaoSession(DaoManager.daoSession)

                    if (record.recordType != null) {
                        val recordType = record.recordType
                        if (recordType.isIncoming == 0 && type == 0) {
                            setClassify(record.recordType)
                        } else {
                            setClassify(list.first())
                        }
                    } else {
                        setClassify(list.first())
                    }

                    val rateMoney = record.rateMoney
                    if (rateMoney != 0.0) {
                        val builder = SpannableStringBuilder()
                        builder.inSpans(ForegroundColorSpan(Utils.getColor(R.color.text_color_655f5f))) {
                            append(Math.abs(rateMoney).toMoney())
                        }
                        tv_money.text = builder
                    }

                }
    }

    override fun initListener() {
        super.initListener()
        sbv.setOnStartSpringBackListener(object : SpringBackLayout.OnSpringBackListener {
            override fun onTopSpringBack() {
                if (!adapter.isEdit) {
                    EventBus.getDefault().post(BooleanEvent(false))
                }
            }

            override fun onBottomSpringBack() {
                if (!adapter.isEdit) {
                    EventBus.getDefault().post(BooleanEvent(true))
                }
            }
        })
        adapter.setOnItemClickListener { _, position, itemInfo ->
            if (position < adapter.itemCount - 1) {
                setClassify(itemInfo)
                EventBus.getDefault().post(BooleanEvent(false))
            } else {
                val activityInfoBean = ActivityInfoBean(Utils.getString(if (type == 0) R.string.edit_expend_classify else R.string.edit_income_classify), next = Utils.getString(R.string.add))
                val bundle = Bundle()
                bundle.putInt("type", type)
                ActivityTool.skipActivity<EditClassifyActivity>(bundle, activityInfoBean)
            }
        }
        adapter.setOnEditChangeListener {
            EventBus.getDefault().post(BooleanEvent(it))
        }
    }


    fun onKeyDown(keyCode: Int, @Suppress("UNUSED_PARAMETER") event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (adapter.isEdit) {
                adapter.isEdit = false
                return true
            }
        }
        return false
    }


    private fun setClassify(recordType: RecordType) {
        typeId = recordType.uuid
        tv_type_desc.text = recordType.typeDesc
        tv_type_desc.typeLogo(recordType.imgSrcId, Utils.dip2px(35))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(result: CalculatorEventBean) {
        moneySpan.clear()
        if (result.equation.contains('+')
                || (result.equation.contains('-') && result.nums.size != 1)) {
            moneySpan.append(result.result)
            moneySpan.setSpan(sizeSpan25, 0, result.result.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            moneySpan.inSpans(sizeSpan12, colorSpan) {
                append("\n")
                append(result.equation)
            }
            tv_money.text = moneySpan
        } else {
            tv_money.text = result.result
        }
    }

    private val daoChangeListener = object : OnDaoChangeListener<RecordType> {
        override fun onInsertEntity(entity: RecordType) {
            if (entity.isIncoming == type) {
                adapter.insertData(entity)
            }
        }

        override fun onUpdateEntity(oldEntity: RecordType, newEntity: RecordType) {
            if (newEntity.isIncoming == type) {
                val data = adapter.data
                val index = data.interpolationSearch(newEntity.orderIndex) { it.orderIndex }
                if (index != -1) {
                    data[index] = newEntity
                    adapter.notifyItemChanged(index)
                }
            }
        }

        override fun onDeleteEntity(entity: RecordType) {
            if (entity.isIncoming == type) {
                val data = adapter.data
                val index = data.interpolationSearch(entity.orderIndex) { it.orderIndex }
                if (index != -1) {
                    data.removeAt(index)
                    adapter.notifyItemRemoved(index)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (adapter.isEdit) {
            adapter.isEdit = false
        }
    }
}