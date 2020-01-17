package com.boilsnow.lib.common.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

/**
 * Description:权限校验工具类
 * Remark:
 */
object PermissionTool {

    fun checkCamera(context: Context): Boolean = PackageManager.PERMISSION_GRANTED ==
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

    fun checkRecordAudio(context: Context): Boolean = PackageManager.PERMISSION_GRANTED ==
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)

    fun checkReadSD(context: Context): Boolean = PackageManager.PERMISSION_GRANTED ==
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)

    //是否开启通知权限
    fun checkNotification(context: Context): Boolean = try {
        NotificationManagerCompat.from(context).areNotificationsEnabled()
    } catch (e: Exception) {
        false
    }
}