package com.cjj.keepaccounts.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.util.SparseIntArray
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseRecyclerViewAdapter
import com.cjj.keepaccounts.base.viewholder.BaseViewHolder
import com.cjj.keepaccounts.base.viewholder.ClickViewHolder
import com.cjj.keepaccounts.bean.AccountType
import com.cjj.keepaccounts.enum.ListViewType
import com.cjj.keepaccounts.utils.Utils
import org.jetbrains.anko.backgroundColor

/**
 * @author chenjunjie
 * Created by CJJ on 2017/11/29 14:35.
 */
class SelectAccountTypeAdapter(private val propertyList: ArrayList<AccountType>) : BaseRecyclerViewAdapter<RecyclerView.ViewHolder, AccountType>() {
    class HeadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView as TextView
    }

    class ContentViewHolder(itemView: View) : BaseViewHolder(itemView), ClickViewHolder {
        @BindView(R.id.iv_logo)
        lateinit var ivLogo: ImageView
        @BindView(R.id.tv_name)
        lateinit var tvName: TextView
        @BindView(R.id.tv_desc)
        lateinit var tvDesc: TextView
    }

    override fun getItemViewType(position: Int): Int =
            when (position) {
                0, propertyList.size + 1 -> ListViewType.TYPE_HEAD.type
                else -> ListViewType.TYPE_CONTENT.type
            }

    override fun getItem(position: Int): AccountType =
            when (position) {
                in 1..propertyList.size -> propertyList[position - 1]
                else -> data[position - propertyList.size - 2]
            }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            if (viewType == ListViewType.TYPE_HEAD.type) {
                HeadViewHolder(getHeadView())
            } else {
                val view = Utils.inflateRecyclerViewItem(parent, R.layout.list_item_select_account_type)
                ContentViewHolder(view)
            }


    override fun getItemCount(): Int {
        return propertyList.size + super.getItemCount() + 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when (holder) {
            is ContentViewHolder -> {
                val item = getItem(position)
                holder.tvName.text = item.name
                holder.ivLogo.background = getLogoBg(item.idImg)
                if (item.desc.isNullOrEmpty()) {
                    holder.tvDesc.visibility = View.GONE
                } else {
                    holder.tvDesc.visibility = View.VISIBLE
                    holder.tvDesc.text = item.desc
                }
            }

            is HeadViewHolder -> {
                holder.textView.text = if (position == 0) {
                    Utils.getString(R.string.property) + Utils.getString(R.string.account)
                } else {
                    Utils.getString(R.string.liabilities) + Utils.getString(R.string.account)
                }
            }
        }
    }

    private fun getHeadView(): TextView {
        val textView = TextView(Utils.context)
        val layoutParams = AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Utils.dip2px(30F))
        textView.layoutParams = layoutParams
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.backgroundColor = Color.WHITE
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getDimension(R.dimen.text_size12).toFloat())
        textView.setTextColor(Utils.getColor(R.color.text_color_655f5f))
        textView.setPadding(Utils.getDimension(R.dimen.start), 0, 0, 0)
        return textView
    }

    private fun getLogoBg(imgId: Int): Drawable {
        val drawable1 = GradientDrawable()
        drawable1.setColor(colorList[imgId])
        drawable1.shape = GradientDrawable.OVAL
        return drawable1
    }

    companion object {
        val colorList = SparseIntArray()

        init {
            colorList.put(1, Color.parseColor("#FF2FB2E8"))//现金
            colorList.put(2, Color.parseColor("#FF82BBE7"))//储值卡
            colorList.put(3, Color.parseColor("#FF7FCFC7"))//信用卡
            colorList.put(4, Color.parseColor("#FFf2b489"))//网络账户
            colorList.put(5, Color.parseColor("#FFe58a84"))//投资账户
            colorList.put(6, Color.parseColor("#FF9361A2"))//储值卡
            colorList.put(7, Color.parseColor("#FFF3BD5D"))//借出
            colorList.put(8, Color.parseColor("#FFEC87BF"))//借入
            colorList.put(9, Color.parseColor("#FF2FB2E8"))//支付宝
            colorList.put(11, Color.parseColor("#FF45BDB3"))//固定收入
            colorList.put(10, Color.parseColor("#FF45BC80"))//微信
            colorList.put(12, Color.parseColor("#FF2FB2E8"))//蚂蚁花呗
            colorList.put(13, Color.parseColor("#FFEA7067"))//京东白条
        }
    }
}