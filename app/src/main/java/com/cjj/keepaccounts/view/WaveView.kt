package com.cjj.keepaccounts.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.cjj.keepaccounts.R
import com.cjj.keepaccounts.utils.Utils


/**
 * @author chenjunjie
 * Created by CJJ on 2018/3/14 11:06.
 */
class WaveView : View {


    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val wavePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var bgColor = Utils.getColor(R.color.bg_color_d1cfcf)

    private var waveColor = Utils.getColor(R.color.AppThemeColor)
    private var waveStartColor = Utils.getColor(R.color.AppThemeColor)
    private var waveEndColor = Utils.getColor(R.color.red)

    private val circlePath: Path = Path()

    private var wavePath = Path()

    private var waveCrestHeight: Float = Utils.dip2px(5F).toFloat()
    private var waveCrestWidth: Float = Utils.dip2px(25F).toFloat()

    var waveLevel: Float = 1F
        set(value) {
            waveColor = Utils.evaluateColor(value, waveEndColor, waveStartColor)
            field = value
            postInvalidate()
        }

    private var waveCrestX: Float = 0F

    private var viewWidth = 0
    private var viewHeight = 0
    private var isDraw: Boolean = true

    private lateinit var mAnimator: ValueAnimator

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)


    init {
        mPaint.color = bgColor
        wavePaint.color = waveColor
        mPaint.isAntiAlias = true
        wavePaint.isAntiAlias = true
    }


    override fun onDraw(canvas: Canvas) {

        viewWidth = width - paddingLeft - paddingRight
        viewHeight = height - paddingTop - paddingBottom
        val radius = Math.min(viewWidth, viewHeight) / 2
        canvas.drawCircle((paddingLeft + viewWidth / 2).toFloat(), (paddingTop + viewHeight / 2).toFloat(), radius.toFloat(), mPaint)
        circlePath.addCircle((paddingLeft + viewWidth / 2).toFloat(), (paddingTop + viewHeight / 2).toFloat(), radius.toFloat(), Path.Direction.CCW)
        canvas.clipPath(circlePath)
        drawWave(canvas)
    }


    private val paintFlagsDrawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private fun drawWave(canvas: Canvas) {
        canvas.drawFilter = paintFlagsDrawFilter
        wavePaint.color = waveColor
        wavePath.reset()
        var y = paddingTop - waveCrestHeight + viewHeight.toFloat() * (1 - waveLevel) + waveCrestHeight / 2
        if (y > viewHeight - waveCrestHeight) {
            y = viewHeight - waveCrestHeight
        }
        wavePath.moveTo(left.toFloat() + paddingLeft - waveCrestWidth * 2 + waveCrestX, y)
        for (i in 0..3) {
            wavePath.rQuadTo(waveCrestWidth / 2F, waveCrestHeight, waveCrestWidth, 0F)
            wavePath.rQuadTo(waveCrestWidth / 2F, -waveCrestHeight, waveCrestWidth, 0F)
        }
        wavePath.lineTo(width - paddingRight.toFloat(), height - paddingBottom.toFloat())
        wavePath.lineTo(paddingLeft.toFloat(), height - paddingBottom.toFloat())
        wavePath.close()
        canvas.drawPath(wavePath, wavePaint)

        wavePaint.color = Color.WHITE
        wavePaint.alpha = 20
        wavePath.reset()
        wavePath.moveTo(left.toFloat() + paddingLeft - waveCrestWidth * 2 + waveCrestX - waveCrestWidth / 2F, paddingTop + viewHeight.toFloat() * (1 - waveLevel) + waveCrestHeight / 2)
        for (i in 0..3) {
            wavePath.rQuadTo(waveCrestWidth / 2F, waveCrestHeight, waveCrestWidth, 0F)
            wavePath.rQuadTo(waveCrestWidth / 2F, -waveCrestHeight, waveCrestWidth, 0F)
        }
        wavePath.lineTo(width - paddingRight.toFloat(), height - paddingBottom.toFloat())
        wavePath.lineTo(paddingLeft.toFloat(), height - paddingBottom.toFloat())
        wavePath.close()
        canvas.drawPath(wavePath, wavePaint)


    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        updateXControl()
    }

    /**
     * 不断的更新偏移量，并且循环。
     */
    private fun updateXControl() {
        //设置一个波长的偏移
//        mAnimator = ValueAnimator.ofFloat(0F, waveCrestWidth * 2)
//        mAnimator.interpolator = LinearInterpolator()
//        mAnimator.addUpdateListener { animation ->
//            val animatorValue = animation.animatedValue as Float
//            waveCrestX = animatorValue//不断的设置偏移量，并重画
//            postInvalidate()
//        }
//        mAnimator.duration = 3000
//        mAnimator.repeatCount = ValueAnimator.INFINITE
//        mAnimator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isDraw = false
    }

}