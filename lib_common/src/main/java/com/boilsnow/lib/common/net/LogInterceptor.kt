package com.boilsnow.lib.common.net

import com.boilsnow.lib.common.util.LL
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset

/**
 * Description: 网络访问过滤器
 * Remark:
 */
internal class LogInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body()
        var body = ""
        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            val contentType = requestBody.contentType()
            if (contentType != null) {
                var charset = contentType.charset()
                if (charset == null) charset = Charset.forName("UTF-8")
                body = buffer.readString(charset)
            }
        }

        val url = request.url()
        val hashCode = url.hashCode().toString()
        LL.d("$hashCode===url:$url")
        LL.d("$hashCode===method:${request.method()}")
        LL.d("$hashCode===body:$body")

        val response = chain.proceed(request)
        val responseBody = response.body()
        var rBody = ""
        if (responseBody != null) {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer()
            val contentType = responseBody.contentType()
            if (contentType != null) {
                var charset = contentType.charset()
                if (charset == null) charset = Charset.forName("UTF-8")
                rBody = buffer.clone().readString(charset)
            }
        }

        LL.d("$hashCode===rCode:${response.code()}")
        LL.d("$hashCode===rMessage:${response.message()}")
        LL.d("$hashCode===rBody:$rBody")

        return response
    }
}