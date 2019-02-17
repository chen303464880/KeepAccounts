package com.cjj.keepaccounts.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.text.inSpans
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.utils.LogUtils
import com.cjj.keepaccounts.utils.Utils
import java.math.BigDecimal


/**
 * @author chenjunjie
 * Created by CJJ on 2017/12/8 9:57.
 */

val spanColor = ForegroundColorSpan(Utils.getColor(R.color.text_color_655f5f))

class CalculatorView : RecyclerView {
    private var dividerHeight = 1
    private var dividerColor = Color.WHITE

    val textColor = Color.WHITE
    val textSize = 20F

    private var rowHeight: Int = dip2px(50F)

    //当前公式
    val equation = StringBuilder()

    //上一个公式
    var previous = ""
    val nums = arrayListOf<String>()
    //结果
    var result: BigDecimal = BigDecimal(0)
    private val resultSpan = SpannableStringBuilder("0")

    var listener: OnCalculatorListener? = null

    interface OnCalculatorListener {
        fun calculatorChange(equation: String, nums: ArrayList<String>, result: SpannableStringBuilder)
        fun result(result: SpannableStringBuilder)
        fun finish(result: SpannableStringBuilder)
    }

    var resultView: Button? = null

    var isCalculate: Boolean = true
        set(value) {
            field = value
            if (resultView != null) {
                resultView!!.text = if (value) "OK" else "="
            }
        }

    fun setResult(result: String) {
        resultSpan.clear()
        resultSpan.clearSpans()
        resultSpan.inSpans(spanColor) {
            append(result)
        }
        listener?.result(resultSpan)
    }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalculatorView)
            rowHeight = typedArray.getDimension(R.styleable.CalculatorView_itemHeight, dip2px(50F).toFloat()).toInt()
            dividerHeight = typedArray.getDimension(R.styleable.CalculatorView_dividesHeight, 1F).toInt()
            dividerColor = typedArray.getColor(R.styleable.CalculatorView_dividesColor, Color.WHITE)
            typedArray.recycle()
        }
        init()
    }

    private fun init() {
        this.adapter = Adapter()
        this.addItemDecoration(itemDecoration)
        this.layoutManager = GridLayoutManager(context, 4)
        this.overScrollMode = View.OVER_SCROLL_NEVER
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = rowHeight * 4 + dividerHeight * 3
        }

        setMeasuredDimension(widthSize, heightSize)

    }


    inner class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val codes = arrayListOf('7', '8', '9', '-', '4', '5', '6', '+', '1', '2', '3', '-', 'C', '0', '.', 'O')

        inner class CodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView = itemView as Button
        }

        inner class RemoveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val removeView = itemView as ImageView
        }


        override fun getItemViewType(position: Int): Int =
                when (position) {
                    3 -> 1
                    15 -> 2
                    else -> 0
                }


        override fun getItemCount(): Int = codes.size


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            when (holder) {
                is CodeViewHolder -> {
                    holder.textView.text = if (position != codes.size - 1) codes[position].toString() else "OK"
                    holder.textView.setOnClickListener {
                        when (position) {
                            0, 1, 2, 4, 5, 6, 8, 9, 10 -> {

                                if (nums.isNotEmpty()) {
                                    if (equation.last() == '+') {
                                        equation.append(codes[position])
                                    } else {

                                        val last = nums.last()
                                        if (last.contains(".")) {
                                            if (last.length - last.lastIndexOf(".") <= 2 && last.lastIndexOf(".") <= 8) {
                                                equation.append(codes[position])
                                            }
                                        } else if (last.length <= 8 || equation.last() == '+' || equation.last() == '-') {
                                            equation.append(codes[position])
                                        }
                                        if (nums.last() == "0") {
                                            equation.replace(equation.length - 1, equation.length, codes[position].toString())
                                        }
                                    }
                                } else {
                                    equation.append(codes[position])
                                }
                            }
                            7, 11 -> {//+,-
                                isCalculate = false
                                if (equation.isNotEmpty()) {
                                    val last = equation.last()
                                    if (last in '0'..'9') {
                                        if (position == 11) {
                                            equation.append(codes[7])
                                        }
                                        equation.append(codes[position])
                                    } else if (last != codes[position]) {
                                        if (position == 7) {
                                            equation.replace(equation.length - 2, equation.length, codes[position].toString())
                                        }
                                        if (position == 11) {
                                            equation.delete(equation.length - 1, equation.length)
                                            equation.append(codes[7].toString())
                                            equation.append(codes[11].toString())
                                        }
                                    }
                                } else {
                                    if (position == 11) {
                                        equation.append(codes[position])
//                                        equation.append('0')
                                    }

                                }
                            }
                            12 -> {//C
                                equation.delete(0, equation.length)
                            }
                            13 -> {//0
                                if (equation.isEmpty() || nums.isEmpty()) {
                                    equation.append("0")
                                } else if (nums.last() != "0") {
                                    equation.append("0")
                                }
                            }
                            14 -> {//.
                                if (equation.isEmpty() || nums.isEmpty() || nums.last().isEmpty() || nums.last() == "-") {
                                    equation.append("0")
                                    equation.append(".")
                                } else if (nums.last().isNotEmpty() && !nums.last().contains(".")) {
                                    equation.append(".")
                                }
                            }
                            15 -> {//确定
                                if (listener != null) {
                                    if (isCalculate) {
                                        listener!!.finish(resultSpan)
                                        return@setOnClickListener
                                    } else {
                                        isCalculate = true
                                        equation.delete(0, equation.length)
                                        equation.append(resultSpan.toString())
                                        listener!!.result(resultSpan)
                                    }
                                } else if (isCalculate) {
                                    isCalculate = true
                                }

                            }
                        }
//                        LogUtils.i(equation.toString())
                        test(equation.toString())
                    }
                }
                is RemoveViewHolder -> {
                    holder.removeView.setImageResource(R.mipmap.jiyibi_icon_tuige)
                    holder.removeView.setOnClickListener {
                        if (equation.isNotEmpty()) {
                            if (equation.last() == '-' && equation.toString() != "-") {
                                equation.delete(equation.length - 2, equation.length)
                            } else {
                                equation.delete(equation.length - 1, equation.length)
                            }
                        } else if (resultSpan.isNotEmpty() && resultSpan.toString() != "0.00") {
                            equation.append(resultSpan.delete(resultSpan.length - 1, resultSpan.length).toString())
                        }
                        LogUtils.i(equation.toString())
                        test(equation.toString())
                    }
                }
            }

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return if (viewType == 0 || viewType == 2) {
                val textView = Button(context)
                textView.gravity = Gravity.CENTER
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
                textView.setTextColor(textColor)
                textView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, rowHeight)
                textView.background = getDrawable(R.drawable.sekector_calculater_item_bg)

                if (viewType == 2) {
                    resultView = textView
                }
                CodeViewHolder(textView)
            } else {
                val imageView = ImageView(context)
                imageView.scaleType = ImageView.ScaleType.CENTER
                imageView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, rowHeight)
                imageView.background = getDrawable(R.drawable.sekector_calculater_item_bg)
                RemoveViewHolder(imageView)
            }
        }

    }

    private fun test(equation: String) {

        if (equation.isEmpty() || previous == equation) {
//            if (isCalculate) {
//                this.equation.delete(0, this.equation.length)
//                this.equation.append(result.toString())
//            }
            previous = equation
            if (equation.isEmpty()) {
                nums.clear()
                result = BigDecimal(0)
            }
            resultSpan.clear()
            resultSpan.clearSpans()
            resultSpan.append(result.setScale(2, BigDecimal.ROUND_HALF_UP).toString())
            if (previous == equation && equation.isNotBlank()) {
                resultSpan.setSpan(spanColor, 0, resultSpan.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }

            val strEquation = equation.replace("+-", "-")
            listener?.calculatorChange(strEquation, nums, resultSpan)
            return
        }
        isCalculate = !equation.contains('+')
        nums.clear()
        nums.addAll(equation.split("+"))
        nums.remove("")
        result = BigDecimal(if (nums.isEmpty() || (nums.size == 1 && nums.first() == "-")) "-0" else if (nums.first().last() == '-') nums.first().replace("-", "") else nums.first())
        if (nums.size >= 2) {
            (1 until nums.size)
                    .asSequence()
                    .filter { nums[it].isNotEmpty() && nums[it] != "-" }
                    .forEach { result += BigDecimal(nums[it]) }
        }
        previous = equation
        if (listener != null) {
            val strEquation = equation.replace("+-", "-")
            resultSpan.clearSpans()
            resultSpan.clear()
            if (nums.size == 1 && result == BigDecimal.ZERO && equation.first() == '-') {
                resultSpan.append("-")
            }
            resultSpan.append(result.setScale(2, BigDecimal.ROUND_HALF_UP).toString())
            resultSpan.setSpan(spanColor, 0, resultSpan.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            if (equation.contains('+')) {
                resultSpan.setSpan(spanColor, 0, resultSpan.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            } else {
                if (nums.isNotEmpty()) {
                    val last = nums.last()
                    val indexOf = last.lastIndexOf(".")
                    if (indexOf == -1) {
                        resultSpan.setSpan(spanColor, 0, resultSpan.length - 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    } else {
                        resultSpan.setSpan(spanColor, 0, resultSpan.length - (3 - last.length + indexOf), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    }
                } else {
                    resultSpan.setSpan(spanColor, 0, resultSpan.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                }
            }
            listener!!.calculatorChange(strEquation, nums, resultSpan)
        }
    }

    fun dip2px(dip: Float): Int {
        return Math.round(dip * context.resources.displayMetrics.density)
    }

    fun getColor(@ColorRes colorId: Int): Int {
        return ContextCompat.getColor(context, colorId)
    }

    fun getDrawable(@DrawableRes drawableId: Int): Drawable {
        val drawable = ContextCompat.getDrawable(context, drawableId)!!
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        return drawable
    }

    private val itemDecoration = object : RecyclerView.ItemDecoration() {
        val mPaint = Paint()

        init {
            mPaint.color = dividerColor
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: State) {
            super.onDraw(c, parent, state)
            drawHorizontal(c, parent)
            drawVertical(c, parent)
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
            super.getItemOffsets(outRect, view, parent, state)
            val childAdapterPosition = parent.getChildAdapterPosition(view)
            if ((childAdapterPosition + 1) % 4 != 0) {
                outRect.right = dividerHeight
            }
            if (childAdapterPosition < 12) {
                outRect.bottom = dividerHeight
            }
        }

        //绘制纵向 item 分割线

        private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
            val top = parent.paddingTop
            val bottom = parent.measuredHeight - parent.paddingBottom
            val childSize = parent.childCount
            for (i in 0 until childSize) {
                val child = parent.getChildAt(i)
                val layoutParams = child.layoutParams as RecyclerView.LayoutParams
                val left = child.right + layoutParams.rightMargin
                val right = left + dividerHeight
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)

            }
        }

        //绘制横向 item 分割线
        private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
            val left = parent.paddingLeft
            val right = parent.measuredWidth - parent.paddingRight
            val childSize = parent.childCount
            for (i in 0 until childSize) {
                val child = parent.getChildAt(i)
                val layoutParams = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + layoutParams.bottomMargin
                val bottom = top + dividerHeight
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
            }
        }
    }

}
