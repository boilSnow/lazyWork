package com.boilsnow.lib.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.text.TextUtils
import android.widget.ImageView
import com.boilsnow.lib.common.R
import com.boilsnow.lib.common.util.assist.PictureCircleTransform
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Description:图片相关工具类
 * Remark:
 */
object PictureTool {

    //加载图片 type 1 长方形(w>h)  2 正方形
    fun loadPicture(
        context: Context?, path: String?, view: ImageView?, type: Int = -1,
        defaultSrc: Int = R.drawable.l00_img_default_1,
        isSkipCache: Boolean = false, isNullScale: Boolean = false
    ) = loadGlidePicture(
        context = context, path = path, view = view, type = type, radius = 0F,
        defaultSrc = defaultSrc, isCircle = false,
        isSkipCache = isSkipCache, isNullScale = isNullScale
    )

    //加载圆形/圆角图片 type 1 长方形(w>h)  2 正方形
    fun loadCirclePicture(
        context: Context?, path: String?, view: ImageView?, type: Int = -1,
        radius: Float = 0F, defaultSrc: Int = R.drawable.l00_img_default_1,
        isSkipCache: Boolean = false, isNullScale: Boolean = false
    ) = loadGlidePicture(
        context = context, path = path, view = view, type = type, radius = radius,
        defaultSrc = defaultSrc, isCircle = true,
        isSkipCache = isSkipCache, isNullScale = isNullScale
    )

    private fun loadGlidePicture(
        context: Context?, path: String?, view: ImageView?, type: Int, radius: Float,
        defaultSrc: Int, isCircle: Boolean, isSkipCache: Boolean, isNullScale: Boolean
    ) {
        if (TextUtils.isEmpty(path) || context == null) return
        val default = when (type) {
            1 -> R.drawable.l00_img_default_1
            2 -> R.drawable.l00_img_default_2
            else -> defaultSrc
        }
        val glideBuild = Glide.with(context).load(path)
        //跳过缓存
        if (isSkipCache) glideBuild
            .skipMemoryCache(isSkipCache)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
        //使用默认填充方式
        if (!isNullScale) glideBuild.centerCrop()
        //填充默认占位图
        glideBuild.placeholder(default)
        //转换为圆形
        if (isCircle) glideBuild.transform(PictureCircleTransform(context, radius))
        //加载view
        glideBuild.crossFade().into(view)
    }

    //压缩图片  大小单位:KB
    fun compressImage(
        filePath: String, rWidth: Int = 480, rHeight: Int = 800, minSize: Int = 1024 * 2
    ): File {
        val bitmap = getSmallBitmap(filePath, rWidth, rHeight)
        val out = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        var options = 100
        while (out.toByteArray().size / 1024 > minSize && options > 0) {
            out.reset()
            options -= 10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, out)
        }
        if (options <= 0) bitmap.compress(Bitmap.CompressFormat.JPEG, 5, out)

        val date = Date(System.currentTimeMillis())
        val filename = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(date)
        val file = File(Environment.getExternalStorageDirectory(), "$filename.png")
        try {
            val fos = FileOutputStream(file)
            try {
                fos.write(out.toByteArray())
                fos.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        if (!bitmap.isRecycled) bitmap.recycle()
        return file
    }

    //获取小图
    fun getSmallBitmap(filePath: String, rWidth: Int, rHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)
        options.inSampleSize = calcInSampleSize(options, rWidth, rHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }

    private fun calcInSampleSize(op: BitmapFactory.Options, rWidth: Int, rHeight: Int): Int {
        val height = op.outHeight
        val width = op.outWidth
        var inSampleSize = 1
        if (height > rHeight || width > rWidth) {
            val heightRatio = Math.round(height.toFloat() / rHeight)
            val widthRatio = Math.round(width.toFloat() / rWidth)
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }

}