package com.boilsnow.lib.common.net

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

    fun resetHead() = NetService.instance.resetClient()

    fun <T> createApi(service: Class<T>, baseUrl: String = ""): T =
        NetService.instance.createServiceApi(service, baseUrl)
}