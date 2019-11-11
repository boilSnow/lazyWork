package com.boilsnow.lib.common.util.assist

import android.content.Context
import android.graphics.*

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import android.graphics.RectF
import android.graphics.BitmapShader
import android.graphics.Bitmap

/**
 * Description:圆形图片
 * Remark:
 */
internal class PictureCircleTransform(context: Context, private val radius: Float = 0F) :
    BitmapTransformation(context) {

    override fun transform(
        pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int
    ): Bitmap? = if (radius == 0F) circleCrop(pool, toTransform) else roundCrop(pool, toTransform)

    private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) return null
        val size = Math.min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val squared = Bitmap.createBitmap(source, x, y, size, size)
        var result: Bitmap? = pool.get(size, size, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(result!!)
        val paint = Paint()
        paint.shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val r = size / 2F
        canvas.drawCircle(r, r, r, paint)
        return result
    }

    private fun roundCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) return null
        var result: Bitmap? = pool.get(source.width, source.height, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(result!!)
        val paint = Paint()
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val rectF = RectF(0f, 0f, source.width.toFloat(), source.height.toFloat())
        canvas.drawRoundRect(rectF, radius, radius, paint)
        return result
    }

    override fun getId(): String = javaClass.name
}