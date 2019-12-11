package com.boilsnow.lib.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.text.TextUtils
import android.widget.ImageView
import com.boilsnow.lib.common.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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
        context: Context?, view: ImageView?, type: Int = -1, path: String?,
        defaultSrc: Int = -1, isSkipCache: Boolean = false, isNullScale: Boolean = false
    ) = loadGlidePicture(
        context = context, path = path, view = view, type = type,
        radius = 0F,
        defaultSrc = defaultSrc, isCircle = false,
        isSkipCache = isSkipCache, isNullScale = isNullScale
    )

    //加载圆形/圆角图片 type 1 长方形(w>h)  2 正方形
    fun loadCirclePicture(
        context: Context?, view: ImageView?, type: Int = -1, path: String?, radius: Float = 0F,
        defaultSrc: Int = -1, isSkipCache: Boolean = false, isNullScale: Boolean = false
    ) = loadGlidePicture(
        context = context, view = view, type = type, defaultSrc = defaultSrc, radius = radius,
        path = path, isCircle = true, isSkipCache = isSkipCache, isNullScale = isNullScale
    )

    private fun loadGlidePicture(
        context: Context?, view: ImageView?, type: Int, path: String?, radius: Float,
        defaultSrc: Int, isCircle: Boolean, isSkipCache: Boolean, isNullScale: Boolean
    ) {
        if (TextUtils.isEmpty(path) || context == null || view == null) return
        val default = if (defaultSrc != -1) defaultSrc else when (type) {
            1 -> R.drawable.l00_img_default_00
            2 -> R.drawable.l00_img_default_01
            else -> R.drawable.l00_img_default_00
        }

        var options = RequestOptions
            .circleCropTransform()
            .placeholder(default)   //填充默认占位图
            .error(default)         //填充错误占位图

        //跳过缓存
        if (isSkipCache) options = options
            .skipMemoryCache(isSkipCache)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
        //使用默认填充方式
        if (!isNullScale) options = options.centerCrop()
        //转换为圆角
        if (isCircle) {
            options = if (radius == 0F) options.transform(CircleCrop())
            else options.transform(CenterCrop(), RoundedCorners(radius.toInt()))
        }
        //加载view
        Glide.with(context).load(path).apply(options).into(view)
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