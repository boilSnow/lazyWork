package com.boilsnow.lib.common.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.boilsnow.lib.common.ui.assist.ToastAss
import com.boilsnow.lib.common.util.LL

/**
 * Description:定义扩展
 * Remark:
 */
abstract class BaseActivity : AppCompatActivity() {

    protected val RESULT_ERR = -99

    protected abstract fun getLayoutID(): Int

    protected abstract fun setupViews()

    protected abstract fun initialized()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutID())
        window.setBackgroundDrawable(null)

        preliminary()
    }

    private fun preliminary() {
        if (verifyExtras()) {
            setupViews()
            initialized()
        } else finish()
    }

    protected open fun verifyExtras() = true

    open fun onBack(v: View?) = finish()

    protected open fun onSuccessFinish() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    protected open fun onErrFinish() {
        setResult(RESULT_ERR)
        finish()
    }

    protected fun log(text: String) = LL.d(tag = this.javaClass.name, msg = text)

    protected fun toast(text: String, isLong: Boolean = false) =
        ToastAss.show(this, text, isLong)

    protected fun toActivity(name: Class<*>, extras: Bundle? = null, requestCode: Int = -1) {
        val intent = Intent(this, name)
        if (extras != null) intent.putExtras(extras)
        if (requestCode < 0) startActivity(intent) else startActivityForResult(intent, requestCode)
    }

    protected fun toRouteActivity(path: String, extras: Bundle? = null, requestCode: Int = -1) {
        var build = ARouter.getInstance().build(path)
        if (extras != null) build = build.with(extras)
        build.navigation(this, requestCode)
    }
}