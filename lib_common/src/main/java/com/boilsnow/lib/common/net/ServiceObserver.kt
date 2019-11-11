package com.boilsnow.lib.common.net

import com.boilsnow.lib.common.bean.ServiceData
import com.boilsnow.lib.common.config.NetConfig
import com.boilsnow.lib.common.entente.BaseMvpEntente
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Description: 订阅事件定义扩展
 * Remark:
 */
abstract class ServiceObserver<D>(private val mView: BaseMvpEntente.IView) :
    Observer<ServiceData<D>> {

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(tData: ServiceData<D>) {
        try {
            //校验数据状态码
            when (tData.statusCode) {
                NetConfig.NET_CODE_SUCCESS -> onComplete(tData.data)
                else -> onError(errText = tData.msg, errCode = tData.statusCode)
            }
        } catch (e: Exception) {
        }
    }

    override fun onError(e: Throwable) {
        onError("Net err")
    }

    override fun onComplete() {
    }

    protected open fun onError(errText: String, errCode: Int = -1) {
        mView.hideProgress()
    }

    protected abstract fun onComplete(data: D)
}