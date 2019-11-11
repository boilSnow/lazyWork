package com.boilsnow.lib.common.net

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Description: 网络访问头信息过滤器
 * Remark:
 */
internal class HeadInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .build()
        return chain.proceed(request)
    }
}