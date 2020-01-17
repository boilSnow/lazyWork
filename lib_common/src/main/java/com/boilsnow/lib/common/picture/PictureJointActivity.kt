package com.boilsnow.lib.common.picture

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.boilsnow.lib.common.R
import com.boilsnow.lib.common.ui.BaseActivity
import com.boilsnow.lib.common.util.AppTool
import com.boilsnow.lib.common.util.LocalUriTool
import com.boilsnow.lib.common.util.PictureTool
import java.io.File

/**
 * Description:图片处理中间界面
 * Remark:
 */
class PictureJointActivity : BaseActivity() {

    override fun getLayoutID() = R.layout.l00_activity_empty

    override fun setupViews() {
    }

    //进入界面数据
    private lateinit var mSkipData: PictureConfig
    //临时图片文件
    private var mCameraTempFile: File? = null

    override fun initialized() {
        if (mSkipData.isCamera) toCamera() else toAlbum()
    }

    override fun verifyExtras(): Boolean {
        val extras = intent.extras ?: return false
        if (!extras.containsKey(PicturePick.SKIP_CONFIG)) return false

        mSkipData = extras.getSerializable(PicturePick.SKIP_CONFIG) as PictureConfig
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) PicturePick.callback?.onError("cancel")
        else when (requestCode) {
            //拍摄返回
            PicturePick.REQUEST_CAMERA -> {
                val resultPath = mCameraTempFile!!.path
                PictureTool.notificationGallery(this, resultPath)
                if (!mSkipData.isCrop) PicturePick.callback?.onSuccess(resultPath) else {
                    toCrop(resultPath)
                    return
                }
            }
            //相册返回
            PicturePick.REQUEST_ALBUM -> {
                val resultPath = LocalUriTool.getFilePathByUri(this, data?.data) ?: ""
                if (!mSkipData.isCrop) PicturePick.callback?.onSuccess(resultPath) else {
                    toCrop(resultPath)
                    return
                }
            }
            //裁剪返回
            PicturePick.REQUEST_CROP -> if (data?.extras == null) PicturePick.callback?.onError("crop err") else {
                val resultPath = data.extras!!.getString(PicturePick.SKIP_PATH, "")
                PicturePick.callback?.onSuccess(resultPath)
            }
        }
        finish()
    }

    //打开相机
    private fun toCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        mCameraTempFile = PicturePick.createTmpPictureFile(this, mSkipData.picDir)

        val pageName = AppTool.getAppPackageName(this)
        cameraIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val imgUri = FileProvider.getUriForFile(
            this, "${pageName}.imagePicker.provider", mCameraTempFile!!
        )
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)

        startActivityForResult(cameraIntent, PicturePick.REQUEST_CAMERA)
    }

    //打开相册
    private fun toAlbum() {
        val intentToPickPic = Intent(Intent.ACTION_PICK, null)
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intentToPickPic, PicturePick.REQUEST_ALBUM)
    }

    //打开裁剪
    private fun toCrop(picPath: String) {
        val extras = Bundle()
        extras.putSerializable(PicturePick.SKIP_CONFIG, mSkipData)
        extras.putString(PicturePick.SKIP_PATH, picPath)
        toActivity(PictureCropActivity::class.java, extras, PicturePick.REQUEST_CROP)
    }

}