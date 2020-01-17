package com.boilsnow.lib.common.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.boilsnow.lib.common.BaseApplication

/**
 * Description:网络处理工具类
 * Remark:
 */
object NetTool {

    //注册网络变化监听
    fun registerSwitchCallBack(callBack: OnNetWorkSwitchCallBack) {
        val service = BaseApplication.appContext.getSystemService(Context.CONNECTIVITY_SERVICE)
        if (service == null) {
            callBack.onErr("service is null")
            return
        }
        (service as ConnectivityManager).requestNetwork(
            NetworkRequest.Builder().build(), object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network?, networkCapabilities: NetworkCapabilities?
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    if (networkCapabilities == null) return
                    //是否连接网络
                    if (!networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) return
                    when {
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> callBack.onLinkWifi()
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> callBack.onLinkCellular()
                        else -> callBack.onLinkOther()
                    }
                }
            })
    }

    //网络切换回调
    interface OnNetWorkSwitchCallBack {
        //出错
        fun onErr(errText: String) {}

        //链接到网络
        fun onLinkWifi()

        //链接到蜂窝网络
        fun onLinkCellular()

        //链接到其他网络
        fun onLinkOther()
    }
}