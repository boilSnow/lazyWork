package com.boilsnow.lib.common.util

import android.util.Log
import com.boilsnow.lib.common.BuildConfig

/**
 * Description:日志输出控制
 * Remark:
 */
object LL {

    private const val TAG = "LOG_TAG"
    private var outFlag = BuildConfig.isOutLog

    private const val TYPE_I = 0
    private const val TYPE_D = 1
    private const val TYPE_E = 2
    private const val TYPE_V = 3
    private const val TYPE_W = 4

    fun i(msg: String, tag: String = TAG) {
        if (outFlag) out(tag, msg, TYPE_I)
    }

    fun d(msg: String, tag: String = TAG) {
        if (outFlag) out(tag, msg, TYPE_D)
    }

    fun e(msg: String, tag: String = TAG) {
        if (outFlag) out(tag, msg, TYPE_E)
    }

    fun v(msg: String, tag: String = TAG) {
        if (outFlag) out(tag, msg, TYPE_V)
    }

    fun w(msg: String, tag: String = TAG) {
        if (outFlag) out(tag, msg, TYPE_W)
    }

    private fun out(tag: String, msg: String, type: Int) {
        var index = 0
        val maxLength = 4000
        var sub: String
        while (index < msg.length) {
            sub = if (msg.length <= index + maxLength) {
                msg.substring(index)
            } else {
                msg.substring(index, index + maxLength)
            }
            index += maxLength
            when (type) {
                TYPE_I -> Log.i(tag, sub)
                TYPE_D -> Log.d(tag, sub)
                TYPE_E -> Log.e(tag, sub)
                TYPE_V -> Log.v(tag, sub)
                TYPE_W -> Log.w(tag, sub)
            }
        }
    }
}