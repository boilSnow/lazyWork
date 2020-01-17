package com.boilsnow.lib.common.picture

import android.content.Intent
import android.text.TextUtils
import com.boilsnow.lib.common.R
import com.boilsnow.lib.common.ui.BaseActivity
import com.boilsnow.lib.common.util.PictureTool
import kotlinx.android.synthetic.main.l00_activity_picture_crop.*
import java.io.File

/**
 * Description:图片裁剪界面
 * Remark:
 */
internal class PictureCropActivity : BaseActivity() {

    override fun getLayoutID() = R.layout.l00_activity_picture_crop

    override fun setupViews() {
        tvDone.setOnClickListener {
            val resultPath = mCropTempFile?.path ?: ""

            PictureTool.saveBitmap2Local(pcvView.cropBitmap(), resultPath)
            PictureTool.notificationGallery(this, resultPath)

            setResult(RESULT_OK, Intent().putExtra(PicturePick.SKIP_PATH, resultPath))
            finish()
        }

        pcvView.post {
            val bmp = PictureTool.loadPicture2Bitmap(mPicturePath, pcvView.width, pcvView.height)
            pcvView.setImageBitmap(bmp)
            pcvView.setClipSize(mSkipData.cropWidth, mSkipData.cropHeight)
        }
    }

    //进入界面数据
    private lateinit var mSkipData: PictureConfig
    //图片地址
    private var mPicturePath = ""
    //图片地址
    private var mCropTempFile: File? = null

    override fun initialized() {
        mCropTempFile = PicturePick.createTmpPictureFile(this, mSkipData.picDir)
    }

    override fun verifyExtras(): Boolean {
        val extras = intent.extras ?: return false
        if (!extras.containsKey(PicturePick.SKIP_CONFIG)) return false

        mSkipData = extras.getSerializable(PicturePick.SKIP_CONFIG) as PictureConfig
        mPicturePath = extras.getString(PicturePick.SKIP_PATH, "")
        return !TextUtils.isEmpty(mPicturePath)
    }
}