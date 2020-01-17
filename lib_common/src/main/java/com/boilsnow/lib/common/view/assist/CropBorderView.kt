package com.boilsnow.lib.common.view.assist

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Description:图片边框视图
 * Remark:
 */
internal class CropBorderView : View {

    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initData()
    }

    //边框颜色
    private val mBorderColor = Color.parseColor("#FFFFFF")
    private lateinit var mPaint: Paint
    //边框宽度
    private val mBorderWidth = 5
    //可视区域大小
    private var mShowWidth = -1
    private var mShowHeight = -1

    private fun initData() {
        mPaint = Paint()
        mPaint.isAntiAlias = true
    }

    fun setBorderSize(width: Int, height: Int) {
        mShowWidth = width
        mShowHeight = height
        requestLayout()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val pLeft = (width - mShowWidth) / 2
        val pTop = (height - mShowHeight) / 2

        mPaint.color = Color.parseColor("#AA000000")
        mPaint.style = Paint.Style.FILL

        canvas.drawRect(
            0f, 0f, pLeft.toFloat(), (height - pTop).toFloat(), mPaint
        )
        canvas.drawRect(
            pLeft.toFloat(), 0f, width.toFloat(), pTop.toFloat(), mPaint
        )
        canvas.drawRect(
            (width - pLeft).toFloat(), pTop.toFloat(), width.toFloat(), height.toFloat(), mPaint
        )
        canvas.drawRect(
            0f, (height - pTop).toFloat(), (width - pLeft).toFloat(), height.toFloat(), mPaint
        )

        mPaint.color = mBorderColor
        mPaint.strokeWidth = mBorderWidth.toFloat()
        mPaint.style = Paint.Style.STROKE
        canvas.drawRect(
            pLeft.toFloat(), pTop.toFloat(), (width - pLeft).toFloat(), (height - pTop).toFloat(),
            mPaint
        )
    }
}