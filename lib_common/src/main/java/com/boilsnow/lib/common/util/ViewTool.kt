package com.boilsnow.lib.common.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.text.InputFilter
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import java.util.regex.Pattern

/**
 * Description:控件相关工具类
 * Remark:
 */
object ViewTool {

    //直线移动view
    fun moveView4Line(view: View, toView: View, isVertical: Boolean = false, duration: Long = 100) {
        val animator = if (isVertical)
            ObjectAnimator.ofFloat(view, "translationY", view.y, toView.y)
        else ObjectAnimator.ofFloat(view, "translationX", view.x, toView.x)

        animator.duration = duration
        animator.interpolator = LinearInterpolator()
        animator.start()
    }

    //更新背景透明度
    fun updateBackgroundAlpha(window: Window, isLight: Boolean = false) {
        updateBackgroundAlpha(window, if (isLight) 1.0F else 0.7F)
    }

    fun updateBackgroundAlpha(window: Window, bgAlpha: Float) {
        if (bgAlpha !in 0.0F..1.0F) return

        val layoutParams = window.attributes
        layoutParams.alpha = bgAlpha            // 0.0-1.0
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.attributes = layoutParams
    }

    //隐藏软键盘
    fun hideInput(context: Context, viewList: ArrayList<View>) {
        val manager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        for (view in viewList) manager.hideSoftInputFromWindow(
            view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    //软键盘是否显示
    fun isInputVisible(activity: Activity): Boolean {
        val dView = activity.window.decorView
        val rect = Rect()
        dView.getWindowVisibleDisplayFrame(rect)
        return dView.height * 2 / 3 > rect.bottom
    }

    //生成 小数过滤器
    fun createFilter4Decimal(places: Int = 2) = InputFilter { source, _, _, dest, dStart, _ ->
        val text = dest.toString()
        val index = text.indexOf(".")
        if (source == ".") {
            if (index != -1) return@InputFilter ""
            if (text.isEmpty()) return@InputFilter "0."
            if (text.substring(dStart).length > places) return@InputFilter ""
            if (dStart == 0 && text.substring(dStart).length <= places) return@InputFilter "0."
        }
        if (index != -1 && text.substring(index).length > places && dStart > index) return@InputFilter ""
        return@InputFilter null
    }

    //生成 空格过滤器
    fun createFilter4Space() = InputFilter { source, _, _, _, _, _ ->
        if (source == " ") return@InputFilter ""
        return@InputFilter null
    }

    //生成 表情过滤器
    fun createFilter4Emoji() = InputFilter { source, _, _, _, _, _ ->
        return@InputFilter if (
            Pattern
                .compile(
                    "[\uD83C\uDC00-\uD83C\uDFFF]" +
                            "|[\uD83D\uDC00-\uD83D\uDFFF]" +
                            "|[\uD83E\uDC00-\uD83E\uDFFF]" +
                            "|[\u2600-\u27ff]"
                )
                .matcher(source)
                .find()
        ) "" else null
    }

    //生成 特殊字符
    fun createFilter4Spe() = InputFilter { source, _, _, _, _, _ ->
        return@InputFilter if (
            Pattern
                .compile(
                    "[`~!@#$%^&*()-+=]" +
                            "|[~！@#￥%……&*（）—+=]" +
                            "|[,.;'”’?/<>|\\\\{}]" +
                            "|[，。；：？、《》｜「」［］【】]"
                )
                .matcher(source)
                .find()
        ) "" else null
    }

    //根据密度获取像素
    fun dip2px(context: Context, dpValue: Float) =
        (dpValue * (context.resources.displayMetrics.density) + 0.5f).toInt()

    //根据像素获取密度
    fun px2dip(context: Context, pxValue: Float) =
        (pxValue / (context.resources.displayMetrics.density) + 0.5f).toInt()
}