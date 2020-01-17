package com.boilsnow.lib.common.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore

/**
 * Description:
 * Remark:
 */
object LocalUriTool {

    //根据URI获取文件真实路径
    fun getFilePathByUri(context: Context, uri: Uri?): String? = when {
        uri == null -> null
        // 如果是document类型的 uri, 则通过document id来进行处理
        DocumentsContract.isDocumentUri(context, uri) -> {
            val documentId: String = DocumentsContract.getDocumentId(uri)
            when {
                isMediaDocument(uri) -> {
                    // MediaProvider 使用':'分割
                    val splitList = documentId.split(":")
                    val contentUri: Uri? = when (splitList[0]) {
                        "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        else -> null
                    }
                    getDataColumn(
                        context, contentUri,
                        "${MediaStore.Images.Media._ID}=?",
                        arrayOf(splitList[1])
                    )
                }
                isDownloadsDocument(uri) -> {
                    val contentUri: Uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(documentId)
                    )
                    getDataColumn(context, contentUri, null, null)
                }
                isExternalStorageDocument(uri) -> {
                    val docId: String = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    if (!"primary".equals(type, ignoreCase = true)) null
                    else Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
                else -> null
            }
        }
        // 如果是 content 类型的 Uri
        "content".equals(uri.scheme, ignoreCase = true) -> getDataColumn(
            context,
            uri,
            null,
            null
        )
        // 如果是 file 类型的 Uri,直接获取图片对应的路径
        "file" == uri.scheme -> uri.path
        else -> null
    }

    //获取数据库表中的 _data 列，即返回Uri对应的文件路径
    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?
    ): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(projection[0])
                return cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            cursor?.close()
        }
        return null
    }

    private fun isMediaDocument(uri: Uri): Boolean =
        "com.android.providers.media.documents" == uri.authority

    private fun isExternalStorageDocument(uri: Uri): Boolean =
        "com.android.externalstorage.documents" == uri.authority

    private fun isDownloadsDocument(uri: Uri): Boolean =
        "com.android.providers.downloads.documents" == uri.authority
}