package com.boilsnow.lib.common.net

import okhttp3.Interceptor

/**
 * Description:网络请求接口管理
 * Remark:
 */
class ApiManage private constructor() {

    companion object {
        val instance = ApiManage()
    }

    init {
        resetHead()
    }

    fun resetHead(icr: Interceptor? = null) = NetService.instance.resetClient(icr)

    fun <T> createApi(service: Class<T>, baseUrl: String = ""): T =
        NetService.instance.createServiceApi(service, baseUrl)
}