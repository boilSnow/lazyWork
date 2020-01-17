package com.boilsnow.lib.common.view.assist

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

/**
 * Description:居中显示的图片
 * Remark:
 */
class CenterImageSpan(drawable: Drawable) : ImageSpan(drawable) {

    override fun getSize(
        paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?
    ): Int = try {
        val rect = drawable.bounds
        if (fm != null) {
            val fmPaint = paint.fontMetricsInt
            val fontHeight: Int = fmPaint.bottom - fmPaint.top
            val drHeight: Int = rect.bottom - rect.top
            val top = drHeight / 2 - fontHeight / 4
            val bottom = drHeight / 2 + fontHeight / 4
            fm.ascent = -bottom
            fm.top = -bottom
            fm.bottom = top
            fm.descent = top
        }
        rect.right
    } catch (e: Exception) {
        20
    }

    override fun draw(
        canvas: Canvas, text: CharSequence?, start: Int, end: Int,
        x: Float, top: Int, y: Int, bottom: Int, paint: Paint
    ) {
        try {
            canvas.save()
            val transY = (bottom - top - drawable.bounds.bottom) / 2 + top
            canvas.translate(x, transY.toFloat())
            drawable.draw(canvas)
            canvas.restore()
        } catch (e: Exception) {
        }
    }
}