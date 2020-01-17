package com.boilsnow.lib.common.util

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * Description:
 * Remark:
 */
object AppTool {

    //获取应用名
    fun getAppPageName(context: Context): String {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.resources.getString(labelRes)
        } catch (e: Exception) {
        }
        return ""
    }

    //获取应用包名
    fun getAppPageName(context: Context, pID: Int): String {
        var processName = ""
        val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val i = am.runningAppProcesses.iterator()
        while (i.hasNext()) {
            val info = i.next() as ActivityManager.RunningAppProcessInfo
            try {
                if (info.pid == pID) {
                    processName = info.processName
                    return processName
                }
            } catch (e: Exception) {
            }
        }
        return processName
    }

    //获取应用包信息
    fun getAppPackageName(context: Context): String {
        try {
            val pid = android.os.Process.myPid()
            val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            for (info in manager.runningAppProcesses) {
                if (info.pid == pid) return info.processName
            }
        } catch (e: Exception) {
        }
        return ""
    }

    //当前应用的版本名称
    fun getAppVersionName(context: Context): String = try {
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName
    } catch (e: Exception) {
        ""
    }

    //获取应用版本号
    fun getAppVersionCode(context: Context): Int = try {
        val packageInfo = context
            .applicationContext
            .packageManager
            .getPackageInfo(context.packageName, 0)

        packageInfo.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        0
    }

    //获取设备号
    fun getDeviceId(context: Context): String = try {
        val manager = context.getSystemService((Context.TELEPHONY_SERVICE)) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) manager.imei!! else manager.deviceId
    } catch (e: Exception) {
        ""
    }

    //获取本地语言偏好
    fun getLocalLanguage(context: Context): ArrayList<String> {
        val result = ArrayList<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val config = context.resources.configuration
            for (i in 0 until config.locales.size()) {
                result.add(config.locales.get(i).language)
            }
        } else result.add(Locale.getDefault().language)

        return result
    }

    //跳转到谷歌商店
    fun skipGoogleShop(context: Context, pageName: String) {
        if (TextUtils.isEmpty(pageName)) return
        try {
            val uri = Uri.parse("market://details?id=$pageName")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.android.vending")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
        }
    }
}