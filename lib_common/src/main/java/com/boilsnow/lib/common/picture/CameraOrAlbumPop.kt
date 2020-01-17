package com.boilsnow.lib.common.picture

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.boilsnow.lib.common.R
import com.boilsnow.lib.common.util.ViewTool

/**
 * Description:图片弹窗
 * Remark:
 */
class CameraOrAlbumPop(private val mContext: Activity) : PopupWindow(mContext),
    View.OnClickListener {

    private var mCallBack: OnClickCallBack? = null

    init {
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.l00_pop_camera_album, null)

        view.findViewById<View>(R.id.tvCamera).setOnClickListener(this)
        view.findViewById<View>(R.id.tvAlbum).setOnClickListener(this)
        view.findViewById<View>(R.id.tvClose).setOnClickListener(this)

        this.contentView = view
        this.isFocusable = true
        this.width = -1
        this.height = -2
        this.animationStyle = R.style.L00Animation4Bottom

        setOnDismissListener { ViewTool.updateBackgroundAlpha(mContext.window, true) }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            //相机
            R.id.tvCamera -> mCallBack?.onClickCamera()
            //相册
            R.id.tvAlbum -> mCallBack?.onClickAlbum()
        }
        dismiss()
    }

    fun setOnClickCallBack(callback: OnClickCallBack) {
        mCallBack = callback
    }

    interface OnClickCallBack {
        fun onClickCamera()
        fun onClickAlbum()
    }
}
