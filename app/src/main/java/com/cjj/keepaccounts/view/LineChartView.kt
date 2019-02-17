package com.cjj.keepaccounts.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.base.BaseView
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * Created by CJJ on 2018/5/24 14:55.
 * Copyright © 2015-2019 CJJ All rights reserved.
 */
class LineChartView : BaseView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val nodeRadius = dip2px(3F).toFloat()
    var gridWidth: Float
        set(value) {
            mGridPaint.strokeWidth = value
            invalidate()
        }
        get() {
            return mGridPaint.strokeWidth
        }
    var zeroLineY = 0.0

    private val monthStr = arrayOf("1月", "3月", "5月", "7月", "9月", "11月")

    private val mMonthPaint = Paint()
    private val mMoneyPaint = Paint()
    private val mGridPaint = Paint()
    private val mLinePaint = Paint()

    private var chartStartX = 0F
    private var chartStartY = 0F
    private var chartEndX = 0F
    private var chartEndY = 0F

    private var chartWidth = 0F
    private var chartHeight = 0F

    private var totalMoney = 200.0
        set(value) {
            field = value
            invalidate()
        }

    var maxMoney = 200.0
        set(value) {
            if (value > 200.0) {
                field = value
                totalMoney = maxMoney - minMoney
            }
        }
    var minMoney = 0.0
        set(value) {
            if (value < 0.0) {
                field = value
                totalMoney = maxMoney - minMoney
            }
        }


    var data = arrayListOf<LineChartBean>()
        set(value) {
            field.clear()
            field.addAll(value)
            invalidate()
        }


    init {
        mMonthPaint.color = getColor(R.color.text_color_a39f9f)
        mMonthPaint.isAntiAlias = true
        mMonthPaint.textAlign = Paint.Align.CENTER
        mMonthPaint.textSize = getDimension(R.dimen.text_size10).toFloat()

        mMoneyPaint.color = getColor(R.color.text_color_a39f9f)
        mMoneyPaint.isAntiAlias = true
        mMoneyPaint.textAlign = Paint.Align.RIGHT
        mMoneyPaint.textSize = getDimension(R.dimen.text_size10).toFloat()

        mGridPaint.strokeWidth = 1F
        mGridPaint.color = getColor(R.color.bg_color_d1cfcf)

        mLinePaint.isAntiAlias = true
        mLinePaint.strokeWidth = dip2px(2F).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawMoneyText(canvas)
        drawMonthText(canvas)
        drawLine(canvas)
    }

    private fun drawMoneyText(canvas: Canvas) {
        val array = getMoneyArray(maxMoney.toLong(), minMoney.toLong())
        //获取文字最大宽度
        var textX = 0F
        array.forEach {
            val tempX = mMoneyPaint.measureText(it.second)
            if (tempX > textX) {
                textX = tempX
            }
        }
        textX += paddingStart

        chartStartX = textX + 10
        chartEndX = (measuredWidth - paddingEnd).toFloat() - dip2px(10F)
        chartStartY = paddingTop.toFloat()
        val fontMetrics = mMonthPaint.fontMetrics
        chartEndY = measuredHeight - paddingBottom - (fontMetrics.bottom - fontMetrics.top) - dip2px(10F)


        chartWidth = chartEndX - chartStartX
        chartHeight = chartEndY - chartStartY

        val fontMetrics1 = mMoneyPaint.fontMetrics
        val textHeight = fontMetrics1.bottom - fontMetrics1.top

        array.forEach {
            val lineY = paddingTop + (maxMoney - it.first) / totalMoney * chartHeight
            if (it.first == 0L) {
                zeroLineY = lineY
            }
            canvas.drawLine(chartStartX, lineY.toFloat(), chartEndX, lineY.toFloat(), mGridPaint)
            val textY = lineY + textHeight / 4 * 1
            canvas.drawText(it.second, textX, textY.toFloat(), mMoneyPaint)
        }

    }

    private fun drawMonthText(canvas: Canvas) {
        val space = chartWidth / 11
        val fontMetrics = mMonthPaint.fontMetrics
        val textY = (measuredHeight - paddingBottom).toFloat() - (fontMetrics.bottom - fontMetrics.descent)
        monthStr.forEachWithIndex { i, s ->
            canvas.drawText(s, chartStartX + space * i * 2, textY, mMonthPaint)
        }
    }

    private fun drawLine(canvas: Canvas) {
        val space = chartWidth / 11
        data.forEachWithIndex { _, item ->
            val shaderPath = Path()
            val linePath = Path()
            val points = arrayListOf<PointF>()


            var minY = 0F
            var maxY = 0F
            item.values.forEachWithIndex { index, it ->
                val cx = chartStartX + space * index
                val cy = (paddingTop + (maxMoney - it) / totalMoney * chartHeight).toFloat()
                if (cy > maxY) {
                    maxY = cy
                }
                if (cy < minY) {
                    minY = cy
                }
                points.add(PointF(cx, cy))

                when (index) {

                    0 -> {
                        shaderPath.moveTo(chartStartX, zeroLineY.toFloat())
                        shaderPath.lineTo(cx, cy)

                        linePath.moveTo(cx, cy)
                    }
                    item.values.size - 1 -> {
                        shaderPath.lineTo(cx, cy)
                        shaderPath.lineTo(cx, zeroLineY.toFloat())
                        shaderPath.lineTo(chartStartX, zeroLineY.toFloat())

                        linePath.lineTo(cx, cy)
                    }

                    else -> {
                        shaderPath.lineTo(cx, cy)
                        linePath.lineTo(cx, cy)
                    }
                }
            }
            shaderPath.close()



            mLinePaint.style = Paint.Style.FILL
            mLinePaint.color = item.color
            mLinePaint.alpha = 255

            if (points.size > 1) {
                val r = (item.color shr 16 and 0xff) / 255.0f
                val g = (item.color shr 8 and 0xff) / 255.0f
                val b = (item.color and 0xff) / 255.0f
                val color = (0F * 255.0f + 0.5f).toInt() shl 24 or ((r * 255.0f + 0.5f).toInt() shl 16) or ((g * 255.0f + 0.5f).toInt() shl 8) or (b * 255.0f + 0.5f).toInt()
                val shader = LinearGradient(chartStartX, minY, chartStartX, zeroLineY.toFloat(), item.color, color, Shader.TileMode.MIRROR)
                mLinePaint.shader = shader
                canvas.drawPath(shaderPath, mLinePaint)
            }

            mLinePaint.shader = null
            mLinePaint.style = Paint.Style.STROKE



            canvas.drawPath(linePath, mLinePaint)

            mLinePaint.style = Paint.Style.FILL
            points.forEach {
                canvas.drawCircle(it.x, it.y, nodeRadius, mLinePaint)
            }
            mLinePaint.alpha = 80
            canvas.drawCircle(points.last().x, points.last().y, nodeRadius * 2, mLinePaint)

        }
    }


    private fun getMoneyArray(max: Long, min: Long): List<Pair<Long, String>> {
        val space = getSpace(max, min)
        val array = ArrayList<Long>(((max - min) / space).toInt())

        val i1 = min - min % space
        array.add(i1)

        var temp = i1 + space
        while (temp < max) {
            array.add(temp)
            temp += space
        }
        return array.map {
            val str = "%dK"
            if (it in -1000L..1000L) {
                Pair(it, it.toString())
            } else {
                if (space < 1000L) {
                    Pair(it, String.format("%.1fK", it / 1000.0))
                } else {
                    Pair(it, String.format(str, it / 1000))
                }
            }
        }

    }

    private fun getSpace(max: Long, min: Long): Long {
        var space = 50L
        val i = max - min
        while (i / space >= 5) {
            var length = 1.0
            var unit = 10L
            while (space / unit != 0L) {
                unit = Math.pow(10.0, ++length).toLong()
            }
            unit /= 10
            if (space / unit == 5L) {
                space *= 2
            } else {
                space += unit
            }
        }

        return space
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        invalidate()
    }


}

data class LineChartBean(val color: Int, val values: List<Double>)