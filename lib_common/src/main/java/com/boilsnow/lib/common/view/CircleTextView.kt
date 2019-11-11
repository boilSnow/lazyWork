package com.boilsnow.lib.common.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.boilsnow.lib.common.R

/**
 * Description:圆形背景TextView
 * Remark:
 */
class CircleTextView : AppCompatTextView {

    private var mSolidColor = 0x00000000
    private var mStrokeColor = 0x00000000
    private var mStrokeWith = 0
    private var mRadius = 0f
    private var mRadiusTL = 0f
    private var mRadiusTR = 0f
    private var mRadiusBL = 0f
    private var mRadiusBR = 0f

    constructor(context: Context?, attrs: AttributeSet?) : this(
        context, attrs, android.R.attr.textViewStyle
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        if (context != null) {
            val density = context.applicationContext.resources.displayMetrics.density

            val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.CircleTextView, 0, 0)
            mSolidColor = ta.getInteger(R.styleable.CircleTextView_solidColor, 0x00000000)
            mStrokeColor = ta.getInteger(R.styleable.CircleTextView_strokeColor, 0x00000000)
            val with = ta.getDimension(R.styleable.CircleTextView_strokeWith, 0f)
            mStrokeWith = (with * density).toInt()
            mRadius = ta.getDimension(R.styleable.CircleTextView_radius, 0f)
            if (mRadius == 0f) {
                mRadiusTL = ta.getDimension(R.styleable.CircleTextView_radiusTopLeft, 0f)
                mRadiusTR = ta.getDimension(R.styleable.CircleTextView_radiusTopRight, 0f)
                mRadiusBL = ta.getDimension(R.styleable.CircleTextView_radiusBottomLeft, 0f)
                mRadiusBR = ta.getDimension(R.styleable.CircleTextView_radiusBottomRight, 0f)
            }

            ta.recycle()
            setShapeDrawable()
        }
    }

    private fun setShapeDrawable() {
        val drawable = GradientDrawable()
        drawable.setColor(mSolidColor)
        drawable.setStroke(mStrokeWith, mStrokeColor)
        if (mRadius > 0f) {
            drawable.cornerRadius = mRadius
        } else if (mRadiusTL > 0f || mRadiusTR > 0f || mRadiusBL > 0f || mRadiusBR > 0f) {
            drawable.cornerRadii = floatArrayOf(
                mRadiusTL, mRadiusTL,
                mRadiusTR, mRadiusTR,
                mRadiusBR, mRadiusBR,
                mRadiusBL, mRadiusBL
            )
        }

        this.background = drawable
    }
}