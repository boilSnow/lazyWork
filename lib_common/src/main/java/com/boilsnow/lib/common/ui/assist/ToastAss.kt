package com.boilsnow.lib.common.ui.assist

import android.content.Context
import android.widget.Toast

/**
 * Description:toast辅助工具类
 * Remark:
 */
object ToastAss {

    private var mLastDate = 0L

    fun show(context: Context, text: String, isLong: Boolean = false) {
        if ((System.currentTimeMillis() - mLastDate) <= 1000) return
        mLastDate = System.currentTimeMillis()
        Toast.makeText(context, text, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }
}