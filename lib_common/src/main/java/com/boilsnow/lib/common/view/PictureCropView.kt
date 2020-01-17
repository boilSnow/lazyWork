package com.boilsnow.lib.common.view

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.boilsnow.lib.common.view.assist.CropBorderView
import com.boilsnow.lib.common.view.assist.CropMatrixImageView

/**
 * Description:裁剪图片视图
 * Remark:
 */
class PictureCropView : RelativeLayout {

    private lateinit var mImageView: CropMatrixImageView
    private lateinit var mBorderView: CropBorderView

    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initData(context!!)
    }

    private fun initData(context: Context) {
        mImageView = CropMatrixImageView(context)
        mBorderView = CropBorderView(context)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(mImageView, layoutParams)
        addView(mBorderView, layoutParams)
    }

    fun setImageBitmap(bitmap: Bitmap?) {
        mImageView.setImageBitmap(bitmap)
        mImageView.resetImage()
    }

    fun hideBorderView(isHide: Boolean) {
        mBorderView.visibility = if (isHide) View.GONE else View.VISIBLE
    }

    fun setClipSize(width: Int, height: Int) {
        mImageView.setClipSize(width, height)
        mBorderView.setBorderSize(width, height)
    }

    fun cropBitmap(): Bitmap = mImageView.cropBitmap()
}