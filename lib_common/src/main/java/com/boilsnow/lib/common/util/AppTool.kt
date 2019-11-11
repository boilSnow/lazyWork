package com.boilsnow.lib.common.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.content.Context.ACTIVITY_SERVICE

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
    fun getAppVersionName(context: Context): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName
        } catch (e: Exception) {
        }
        return ""
    }

    //获取设备号
    fun getDeviceId(context: Context): String {
        try {
            val manager = context.getSystemService((Context.TELEPHONY_SERVICE)) as TelephonyManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                manager.imei!!
            } else
                manager.deviceId
        } catch (e: Exception) {
        }
        return ""
    }
}