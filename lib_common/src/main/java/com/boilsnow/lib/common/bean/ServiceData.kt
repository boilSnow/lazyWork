package com.boilsnow.lib.common.bean

/**
 * Description:服务器数据格式定义
 * Remark:
 */
data class ServiceData<out D>(val data: D, val msg: String, val statusCode: Int)