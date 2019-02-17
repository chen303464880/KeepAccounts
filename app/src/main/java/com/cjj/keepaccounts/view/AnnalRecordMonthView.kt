package com.cjj.keepaccounts.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.bean.AnnalRecordMonthBean
import com.cjj.keepaccounts.utils.Utils

/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/26 15:03.
 */
class AnnalRecordMonthView : View {
    var month: AnnalRecordMonthBean? = null
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    private val monthPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dayPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        monthPaint.textSize = Utils.getDimension(R.dimen.text_size14).toFloat()
        monthPaint.typeface = Typeface.DEFAULT_BOLD
        monthPaint.color = Utils.getColor(R.color.AppThemeColor)
        dayPaint.textSize = Utils.getDimension(R.dimen.text_size09).toFloat()
        dayPaint.color = Utils.getColor(R.color.text_color_655f5f)
        dayPaint.textAlign = Paint.Align.CENTER
        bgPaint.color = Utils.getColor(R.color.transparent)
        bgPaint.strokeWidth = 4F
    }


    private var viewWidth = 0
    private var viewHeight = 0
    private var viewTop = 0
    private var viewLeft = 0
    private var lengthSide = 0F

    private var drawX = 0F
    private var drawY = 0F

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (month != null) {
            drawY = monthPaint.textSize
            monthPaint.color = if (month!!.isFuture) Color.parseColor("#FFB2AFAF") else Utils.getColor(R.color.AppThemeColor)

            canvas.drawText(Utils.context.getString(R.string.xx_month, month!!.month), 0F, drawY, monthPaint)

            val days = month!!.days
            drawY += monthPaint.textSize + Utils.dip2px(7F)
            if (days != null) {
                for ((index, value) in days.withIndex()) {
                    drawX = (index % 7) * lengthSide
                    drawY = monthPaint.textSize + index / 7 * lengthSide + Utils.dip2px(7F)
                    bgPaint.color = if (!value.isToday) if (value.isFuture) Color.TRANSPARENT else when (value.state) {
                        0 -> {
                            Color.TRANSPARENT
                        }
                        1 -> {
                            Utils.getColor(R.color.beyond_not_budget_color)
                        }
                        else -> {
                            Utils.getColor(R.color.beyond_budget_color)
                        }
                    } else Utils.getColor(R.color.AppThemeColor)
                    if (bgPaint.color != Color.TRANSPARENT) {
                        if (value.isToday) {
                            bgPaint.style = Paint.Style.STROKE
                            val excursion = 2
                            canvas.drawRect(drawX + excursion, drawY + excursion, drawX + lengthSide - excursion, drawY + lengthSide - excursion, bgPaint)
                        } else {
                            bgPaint.style = Paint.Style.FILL
                            canvas.drawRect(drawX, drawY, drawX + lengthSide, drawY + lengthSide, bgPaint)
                        }
                    }
                    if (!value.isLastMonth) {
                        dayPaint.color = if (value.isToday) Utils.getColor(R.color.AppThemeColor) else if (value.isFuture) Color.parseColor("#FFB2AFAF") else Utils.getColor(R.color.text_color_655f5f)

                        canvas.drawText(value.day.toString(), drawX + lengthSide / 2, drawY + dayPaint.textSize + Utils.dip2px(1.5F), dayPaint)

                    }
                }
            }

        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode != MeasureSpec.EXACTLY) {
            if (month != null) {
                val h = Math.ceil(month!!.days!!.size.toDouble() / 7.0)
                heightSize = monthPaint.textSize.toInt() + Utils.dip2px(7F) * 2 + h.toInt() * (widthSize / 7) + paddingBottom + paddingTop
            }
        }

        setMeasuredDimension(widthSize, heightSize)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        setLocation(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setLocation(left, top, right, bottom)
    }

    private fun setLocation(left: Int, top: Int, right: Int, bottom: Int) {
        viewWidth = measuredWidth - left - right
        viewHeight = measuredHeight - top - bottom
        viewTop = top
        viewLeft = left
        lengthSide = viewWidth.toFloat() / 7F
        invalidate()
    }


}