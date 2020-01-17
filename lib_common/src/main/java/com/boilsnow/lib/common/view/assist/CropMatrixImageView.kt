package com.boilsnow.lib.common.view.assist

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.sqrt

/**
 * Description:可拖动图片
 * Remark:
 */
internal class CropMatrixImageView : AppCompatImageView, View.OnTouchListener,
    ScaleGestureDetector.OnScaleGestureListener, ViewTreeObserver.OnGlobalLayoutListener {

    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initData(context!!)
    }

    //最小缩放比
    private var mMinScale = 1.0f
    //最大缩放比
    private var mMaxScale = 3.0f

    //缩放手势检测
    private lateinit var mScaleDetector: ScaleGestureDetector
    //模板Matrix，用以初始化
    private val mMatrix = Matrix()
    //处理矩阵的9个值
    private val mMatrixValue = FloatArray(9)

    //记录上一个选择的坐标
    private var mLastX = 0F
    private var mLastY = 0F
    //记录选择的触控点个数
    private var mLastPointCount = 0
    //是否可以移动
    private var mIsDrag = false
    //是否为初次加载
    private var mRequestFlag = false
    //裁剪宽度
    private var mCropWidth = -1
    private var mCropHeight = -1

    fun setClipSize(width: Int, height: Int) {
        mCropWidth = width
        mCropHeight = height
        mRequestFlag = true
        requestLayout()
    }

    fun resetImage() {
        mMatrix.reset()
        imageMatrix = mMatrix
    }

    fun cropBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)

        return Bitmap.createBitmap(
            bitmap, (width - mCropWidth) / 2, (height - mCropHeight) / 2, mCropWidth, mCropHeight
        )
    }

    //初始化数据
    private fun initData(context: Context) {
        setBackgroundColor(Color.BLACK)
        scaleType = ScaleType.MATRIX
        mScaleDetector = ScaleGestureDetector(context, this)
        setOnTouchListener(this)
    }

    // 边界检测
    private fun checkBorder() {
        if (mCropWidth < 0 || mCropHeight < 0) return
        val rect = RectF()
        if (null != drawable) {
            rect.set(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
            mMatrix.mapRect(rect)
        }
        var deltaX = 0f
        var deltaY = 0f

        if (mCropWidth <= width && mCropHeight <= height) {
            val paddingLeft = (width - mCropWidth) / 2
            if (rect.left > paddingLeft)
                deltaX = paddingLeft - rect.left
            if (rect.right < width - paddingLeft)
                deltaX = width - paddingLeft - rect.right
        } else if (rect.width() + 0.01 >= width) {
            if (rect.left > 0) deltaX = -rect.left
            if (rect.right < width) deltaX = width - rect.right
        }

        if (mCropWidth <= width && mCropHeight <= height) {
            val paddingTop = (height - mCropHeight) / 2
            if (rect.top > paddingTop)
                deltaY = paddingTop - rect.top
            if (rect.bottom < height - paddingTop)
                deltaY = height.toFloat() - paddingTop.toFloat() - rect.bottom
        } else if (rect.height() + 0.01 >= height) {
            if (rect.top > 0)
                deltaY = -rect.top
            if (rect.bottom < height)
                deltaY = height - rect.bottom
        }
        mMatrix.postTranslate(deltaX, deltaY)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event == null) return true
        mScaleDetector.onTouchEvent(event)

        var x = 0f
        var y = 0f
        val pointerCount = event.pointerCount
        for (i in 0 until pointerCount) {
            x += event.getX(i)
            y += event.getY(i)
        }
        x /= pointerCount
        y /= pointerCount

        if (pointerCount != mLastPointCount) {
            mIsDrag = false
            mLastX = x
            mLastY = y
        }
        mLastPointCount = pointerCount

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val dx = x - mLastX
                val dy = y - mLastY
                mIsDrag = mIsDrag || sqrt((dx * dx + dy * dy).toDouble()) > 0

                if (mIsDrag && drawable != null) {
                    mMatrix.postTranslate(dx, dy)
                    checkBorder()
                    imageMatrix = mMatrix
                }
                mLastX = x
                mLastY = y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> mLastPointCount = 0
        }
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean = true

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
    }

    //手势事件
    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        if (detector == null) return true
        if (drawable == null) return true

        mMatrix.getValues(mMatrixValue)
        val nowScale = mMatrixValue[Matrix.MSCALE_X]

        var scaleFactor = detector.scaleFactor
        if (nowScale < mMaxScale && scaleFactor > 1.0 || nowScale > mMinScale && scaleFactor < 1.0) {
            if (scaleFactor * nowScale < mMinScale) scaleFactor = mMinScale / nowScale
            if (scaleFactor * nowScale > mMaxScale) scaleFactor = mMaxScale / nowScale

            mMatrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
            checkBorder()
            imageMatrix = mMatrix
        }
        return true
    }

    override fun onGlobalLayout() {
        if (!mRequestFlag) return
        if (mCropWidth < 0 || mCropHeight < 0) return
        val drawable = drawable ?: return

        val imgWidth = drawable.intrinsicWidth
        val imgHeight = drawable.intrinsicHeight

        val widthScale = width.toFloat() / imgWidth
        val heightScale = height.toFloat() / imgWidth
        val viewScale = widthScale.coerceAtMost(heightScale)

        var scale: Float = viewScale
        mMinScale = viewScale

        if (mCropWidth <= width && mCropHeight <= height) {
            val cropWidthScale = mCropWidth.toFloat() / imgWidth
            val cropHeightScale = mCropHeight.toFloat() / imgWidth
            val cropScale = cropWidthScale.coerceAtLeast(cropHeightScale)

            scale = cropScale.coerceAtLeast(viewScale)
            mMinScale = cropScale.coerceAtMost(viewScale)
        }
        mMaxScale = mMinScale * 3

        mMatrix.postTranslate(
            ((width - imgWidth) / 2).toFloat(),
            ((height - imgHeight) / 2).toFloat()
        )
        mMatrix.postScale(scale, scale, (width / 2).toFloat(), (height / 2).toFloat())
        imageMatrix = mMatrix
        mRequestFlag = false
    }
}