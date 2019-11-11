package com.boilsnow.lib.common.net

import android.text.TextUtils
import com.boilsnow.lib.common.config.NetConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Description:网络访问服务
 * Remark:
 */
internal class NetService private constructor() {

    companion object {
        val instance = NetService()
    }

    init {
        resetClient()
    }

    private lateinit var retrofit: Retrofit

    fun <T> createServiceApi(service: Class<T>, baseUrl: String = ""): T =
        if (TextUtils.isEmpty(baseUrl)) retrofit.create(service) else {
            buildRetrofit(buildClient().build(), baseUrl).create(service)
        }

    fun resetClient() {
        retrofit = buildRetrofit(
            buildClient()
                .addInterceptor(HeadInterceptor())
                .build()
        )
    }

    private fun buildRetrofit(client: OkHttpClient? = null, baseUrl: String = ""): Retrofit {
        val url = if (TextUtils.isEmpty(baseUrl)) NetConfig.BASE_URL
        else baseUrl

        val builder = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        if (client != null) builder.client(client)

        return builder.build()
    }

    private fun buildClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
//                .retryOnConnectionFailure(true) //连接失败后是否重连
//                .connectTimeout(15, TimeUnit.SECONDS) //超时时间
            .addInterceptor(LogInterceptor()) //日志过滤器
    }
}