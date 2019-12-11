package com.boilsnow.lib.common.ui.assist

import android.os.CountDownTimer

/**
 * Description:简单的倒计时
 * Remark:
 */
class SimpleCountDown(future: Long, interval: Long) : CountDownTimer(future, interval) {
    private var mIsStarting = false
    private var mCallBack: OnActionCallback? = null

    override fun onFinish() {
        mIsStarting = false
        mCallBack?.onFinish()
    }

    override fun onTick(millis: Long) {
        mIsStarting = true
        mCallBack?.onTick(millis)
    }

    fun isStarting() = mIsStarting

    fun setOnActionCallback(callBack: OnActionCallback) {
        mCallBack = callBack
    }

    interface OnActionCallback {
        fun onTick(millis: Long)

        fun onFinish()
    }
}