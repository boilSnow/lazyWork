package com.boilsnow.lib.common.util

import android.text.TextUtils
import com.boilsnow.lib.common.BaseApplication
import com.google.gson.GsonBuilder
import java.text.SimpleDateFormat
import java.util.*

/**
 * Description:字符串相关工具类
 * Remark:
 */
object StringTool {

    //获取文本
    fun getString(rId: Int): String = BaseApplication.appContext.getString(rId)

    //合法的手机号
    fun isLegalMobile(str: String): Boolean =
        str.matches("^1\\d{10}$".toRegex()) || str.matches("^([5|6|8|9])\\d{7}$".toRegex())

    //合法的昵称
    fun isLegalNickname(str: String): Boolean = str.length in 2..16

    //合法的密码
    fun isLegalPassword(str: String): Boolean = str.matches("^[A-Za-z][A-Za-z0-9]{7,15}$".toRegex())

    //转换时间戳
    fun date4UnixTime(time: Long, fmt: String = "MM-dd HH:mm"): String =
        SimpleDateFormat(fmt, Locale.CHINA).format(Date(time))

    //解析json
    fun <T> fromJson(json: String, clazz: Class<T>) = GsonBuilder().create().fromJson(json, clazz)!!

    //转换json
    fun <T> toJson(data: T): String = GsonBuilder().create().toJson(data)!!

    //时间转换
    fun time4Second(time: Long): String {
        val second = time % 60
        val minuteTime = time / 60
        return "$minuteTime : $second"
    }

    //转换字符串数组
    fun convertArray2Text(list: ArrayList<String>, split: String = ","): String {
        val spLength = split.length

        if (spLength == 0) return ""

        val sb = StringBuffer()
        for (item in list) {
            sb.append(item)
            sb.append(split)
        }
        if (sb.length > spLength) sb.delete(sb.length - spLength, sb.length)
        return sb.toString()
    }

    //数组字符串转为list（text:"a,b,c"）
    fun convertArray4Text(text: String?, sText: String = ","): ArrayList<String> {
        val result = ArrayList<String>()
        if (!TextUtils.isEmpty(text)) result.addAll(text!!.split(sText))
        return result
    }
}