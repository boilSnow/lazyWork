package com.boilsnow.lib.common.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.widget.ImageView
import com.boilsnow.lib.common.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
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
        context: Context?, view: ImageView?, type: Int = -1,
        path: String?, defaultSrc: Int = -1, isSkipCache: Boolean = false
    ) = loadGlidePicture(
        context = context, view = view, type = type, radius = 0F,
        path = path, defaultSrc = defaultSrc, isCircle = false, isSkipCache = isSkipCache
    )

    //加载圆形/圆角图片 type 1 长方形(w>h)  2 正方形
    fun loadCirclePicture(
        context: Context?, view: ImageView?, type: Int = -1, radius: Float = 0F,
        path: String?, defaultSrc: Int = -1, isSkipCache: Boolean = false
    ) = loadGlidePicture(
        context = context, view = view, type = type, radius = radius,
        path = path, defaultSrc = defaultSrc, isCircle = true, isSkipCache = isSkipCache
    )

    //加载网络图片bitmap
    fun loadBitmap4NetUri(context: Context?, netUri: String?, callBack: (bmp: Bitmap) -> Unit) {
        if (TextUtils.isEmpty(netUri) || context == null) return
        Glide.with(context)
            .asBitmap()
            .load(netUri)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) =
                    callBack(resource)
            })
    }

    private fun loadGlidePicture(
        context: Context?, view: ImageView?, type: Int, radius: Float,
        path: String?, defaultSrc: Int, isCircle: Boolean, isSkipCache: Boolean
    ) {
        if (TextUtils.isEmpty(path) || context == null || view == null) return
        val default = if (defaultSrc != -1) defaultSrc else when (type) {
            1 -> R.drawable.l00_img_default_00
            2 -> R.drawable.l00_img_default_01
            else -> R.drawable.l00_img_default_00
        }

        var options = RequestOptions
            .skipMemoryCacheOf(isSkipCache)
            .placeholder(default)   //填充默认占位图
            .error(default)         //填充错误占位图

        //转换为圆角
        if (isCircle) {
            options = if (radius == 0F) options.transform(CircleCrop()) else
                options.transform(CenterCrop(), RoundedCorners(radius.toInt()))
        }
        //加载view
        Glide.with(context).load(path).apply(options).into(view)
    }

    //压缩图片  大小单位:KB
    fun compressImage(
        filePath: String, rWidth: Int = 480, rHeight: Int = 800, maxSize: Int = 1024 * 2
    ): File {
        val bitmap = getSmallBitmap(filePath, rWidth, rHeight)
        val out = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        var options = 100
        while (out.toByteArray().size / 1024 > maxSize && options > 0) {
            out.reset()
            options -= 10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, out)
        }
        if (options <= 0) bitmap.compress(Bitmap.CompressFormat.JPEG, 5, out)

        val date = Date(System.currentTimeMillis())
        val filename = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(date)
        val file = File(Environment.getExternalStorageDirectory(), "$filename.jpg")
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

    //保存图片到本地
    fun saveBitmap2Local(bmp: Bitmap?, filePath: String): Boolean = try {
        val file = File(filePath)
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        if (file.length() > 0) file.delete()
        val fos = FileOutputStream(file)
        val isSuccess = bmp!!.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        isSuccess
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }

    //通知系统相册添加图片
    fun notificationGallery(context: Context, filePath: String) {
        if (TextUtils.isEmpty(filePath)) return
        val uri = Uri.fromFile(File(filePath))
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
        context.sendBroadcast(intent)
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

    //生成bitmap
    fun loadPicture2Bitmap(path: String, w: Int, h: Int): Bitmap {
        val opts = BitmapFactory.Options()
        opts.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, opts)
        var ratio = 1
        if (opts.outWidth > opts.outHeight) {
            if (opts.outWidth >= w) ratio = opts.outWidth / w
        } else {
            if (opts.outHeight >= h) ratio = opts.outHeight / h
        }
        opts.inSampleSize = ratio
        opts.inJustDecodeBounds = false
        val degree = getBitmapDegree(path)
        return if (degree != 0) rotateBitmapByDegree(BitmapFactory.decodeFile(path, opts), degree)
        else BitmapFactory.decodeFile(path, opts)
    }

    //获取图片旋转度数
    private fun getBitmapDegree(path: String): Int = try {
        var degree = 0
        val attribute = ExifInterface(path).getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        when (attribute) {
            ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
            ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
            ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
        }
        degree
    } catch (e: IOException) {
        0
    }

    //旋转图片
    private fun rotateBitmapByDegree(bm: Bitmap, degree: Int): Bitmap {
        var resultBmp: Bitmap? = null
        try {
            val matrix = Matrix()
            matrix.postRotate(degree.toFloat())
            resultBmp = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
        } catch (e: OutOfMemoryError) {
        }
        if (resultBmp == null) resultBmp = bm
        if (bm != resultBmp) bm.recycle()
        return resultBmp
    }
}