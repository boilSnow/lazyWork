package com.boilsnow.lib.common.util

import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * Description:网络相关工具类
 * Remark:
 */
object NetTool {

    //创建json数据体
    fun <D> createJsonBody(d: D): RequestBody {
        val json = GsonBuilder().create().toJson(d)
        return RequestBody.create(MediaType.parse("application/json"), json)
    }
}