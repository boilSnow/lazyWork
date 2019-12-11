package com.boilsnow.lib.common

import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter

/**
 * Description:应用全局控制
 * Remark:
 */
abstract class BaseApplication : MultiDexApplication() {

    companion object {
        lateinit var appContext: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()

        appContext = this
        if (BuildConfig.isOutLog) ARouter.openLog()
        if (BuildConfig.isDebug) ARouter.openDebug()
        ARouter.init(this)
        initialized()
    }

    protected abstract fun initialized()

}