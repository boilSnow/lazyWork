package com.boilsnow.lib.common.ui

import com.boilsnow.lib.common.entente.BaseMvpEntente
import com.boilsnow.lib.common.ui.assist.DialogPick
import com.boilsnow.lib.common.ui.assist.ProgressDialog

/**
 * Description:mvp框架fragment定义
 * Remark:
 */
abstract class BaseMvpFragment<P : BaseMvpEntente.IPresenter> : BaseFragment(),
    BaseMvpEntente.IView {

    protected val mPresenter: P = createPresenter()
    private var mProgressDialog: ProgressDialog? = null
    private var mLastTime = 0L
    private var mLastWarnText = ""

    abstract fun createPresenter(): P

    override fun showProgress(text: String) {
        if (mProgressDialog == null) mProgressDialog =
            ProgressDialog(activity!!)
        mProgressDialog?.updateData(text)
        mProgressDialog?.show()
    }

    override fun hideProgress() {
        mProgressDialog?.dismiss()
    }

    override fun showHintText(text: String, isWarn: Boolean) = if (!isWarn) toast(text)
    else {
        val nowTime = System.currentTimeMillis()
        if (mLastWarnText == text && nowTime - mLastTime <= 1000) {
        } else {
            mLastWarnText = text
            mLastTime = nowTime
            DialogPick.showWarnView(activity!!, text)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            mProgressDialog?.dismiss()
            mPresenter?.onDestroy()
        } catch (e: Exception) {
            log("onDestroy.err:$e")
        }
    }
}