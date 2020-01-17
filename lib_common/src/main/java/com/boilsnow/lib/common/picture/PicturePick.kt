package com.boilsnow.lib.common.picture

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.View
import com.boilsnow.lib.common.util.PermissionTool
import com.boilsnow.lib.common.util.StringTool
import com.boilsnow.lib.common.util.ViewTool
import java.io.File

/**
 * Description: 图片处理选择器
 * Remark:
 */
object PicturePick {
    //配置参数
    internal const val SKIP_CONFIG = "PICTURE_CONFIG"
    //图片路径
    internal const val SKIP_PATH = "PICTURE_PATH"
    //请求拍照
    internal const val REQUEST_CAMERA = 0x9
    //请求相册
    internal const val REQUEST_ALBUM = 0x8
    //请求裁剪
    internal const val REQUEST_CROP = 0x7
    //默认文件目录
    private const val DEFAULT_DIR = "/TEMP/"
    private const val mCropWidth = 800
    private const val mCropHeight = 800

    internal var callback: IPictureCallBack? = null

    /**
     * 打开相册或照相机
     * @context     activity
     * @parent      依托view
     * @isCrop      是否需要裁剪 默认不需要
     * @cropWidth   裁剪宽度 默认800 需要 isCrop 为true
     * @cropHeight  裁剪高度 默认800 需要 isCrop 为true
     * @picDir       裁剪生成目录
     * @callback     回调
     */
    fun showCameraOrAlbumView(
        context: Activity, parent: View, isCrop: Boolean = false,
        cropWidth: Int = mCropWidth, cropHeight: Int = mCropHeight,
        picDir: String = DEFAULT_DIR, callback: IPictureCallBack
    ) {
        if (PermissionTool.checkReadSD(context)) {
            val mPop = CameraOrAlbumPop(context)
            mPop.setOnClickCallBack(object : CameraOrAlbumPop.OnClickCallBack {

                override fun onClickCamera() = openCamera(
                    context, isCrop = isCrop, cropWidth = cropWidth, cropHeight = cropHeight,
                    picDir = picDir, callback = callback
                )

                override fun onClickAlbum() = openSelect(
                    context, isCrop = isCrop, cropWidth = cropWidth, cropHeight = cropHeight,
                    picDir = picDir, callback = callback
                )
            })

            mPop.showAtLocation(parent, Gravity.BOTTOM, 0, 0)
            ViewTool.updateBackgroundAlpha(context.window, false)
        }
    }

    /**
     * 打开照相机
     * @context     context
     * @isCrop      是否需要裁剪 默认不需要
     * @cropWidth   裁剪宽度 默认800 需要 isCrop 为true
     * @cropHeight  裁剪高度 默认800 需要 isCrop 为true
     * @picDir       裁剪生成目录
     * @callback     回调
     */
    fun openCamera(
        context: Context, isCrop: Boolean = false, cropWidth: Int = mCropWidth,
        cropHeight: Int = mCropHeight, picDir: String = DEFAULT_DIR,
        callback: IPictureCallBack
    ) {
        val config = PictureConfig(true, isCrop)
        config.setSize4Crop(cropWidth, cropHeight)
        config.picDir = picDir
        PicturePick.callback = callback

        toActivity(context, config)
    }

    /**
     * 打开相册
     * @context     context
     * @isCrop      是否需要裁剪 默认不需要
     * @cropWidth   裁剪宽度 默认800 需要 isCrop 为true
     * @cropHeight  裁剪高度 默认800 需要 isCrop 为true
     * @picDir       裁剪生成目录
     * @callback     回调
     */
    fun openSelect(
        context: Context, isCrop: Boolean = false,
        cropWidth: Int = mCropWidth, cropHeight: Int = mCropHeight,
        picDir: String = DEFAULT_DIR, callback: IPictureCallBack
    ) {
        val config = PictureConfig(false, isCrop)
        config.setSize4Crop(cropWidth, cropHeight)
        config.picDir = picDir
        PicturePick.callback = callback

        toActivity(context, config)
    }

    //创建临时文件
    fun createTmpPictureFile(context: Context, filePath: String): File {
        val timeStamp = StringTool.date4UnixTime(System.currentTimeMillis(), "MMddHHmmss")

        val externalStorageState = Environment.getExternalStorageState()
        return if (externalStorageState == Environment.MEDIA_MOUNTED) {
            val dir = File(Environment.getExternalStorageDirectory().toString() + filePath)
            if (!dir.exists()) dir.mkdirs()
            File(dir, "$timeStamp.jpg")
        } else {
            val cacheDir = context.cacheDir
            File(cacheDir, "$timeStamp.jpg")
        }
    }

    private fun toActivity(context: Context, config: PictureConfig) {
        verifyValidSize(context = context, config = config)
        val extras = Bundle()
        extras.putSerializable(SKIP_CONFIG, config)
        context.startActivity(Intent(context, PictureJointActivity::class.java).putExtras(extras))
    }

    private fun verifyValidSize(context: Context, config: PictureConfig) {
        if (!config.isCrop) return

        val dm = context.applicationContext.resources.displayMetrics
        if (dm.widthPixels < config.cropWidth || dm.heightPixels < config.cropHeight) {
            val scale =
                Math.min(
                    dm.widthPixels.toFloat() / config.cropWidth,
                    dm.heightPixels.toFloat() / config.cropHeight
                )
            config.setSize4Crop(
                width = (config.cropWidth * scale).toInt(),
                height = (config.cropHeight * scale).toInt()
            )
        }
    }

    /**
     * Description: 图片处理回调
     * Remark:
     */
    interface IPictureCallBack {
        fun onSuccess(result: String)

        fun onError(error: String) {}
    }
}