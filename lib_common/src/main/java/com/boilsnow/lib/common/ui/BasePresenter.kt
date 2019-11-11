package com.boilsnow.lib.common.ui

import com.boilsnow.lib.common.bean.ServiceData
import com.boilsnow.lib.common.entente.BaseMvpEntente
import com.boilsnow.lib.common.net.ServiceObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Description:mvp框架presenter层定义
 * Remark:
 */
abstract class BasePresenter : BaseMvpEntente.IPresenter {

    init {
        this.initialized()
    }

    protected abstract fun initialized()

    protected fun <D> addSubscribe(
        observable: Observable<ServiceData<D>>?, observer: ServiceObserver<D>
    ) {
        observable?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(observer)
    }

    override fun onDestroy() {
    }
}