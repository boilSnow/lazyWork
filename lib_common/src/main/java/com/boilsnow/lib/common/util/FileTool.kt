package com.boilsnow.lib.common.util

import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * Description:文件操作类
 * Remark:
 */
object FileTool {

    //读取本地文本
    fun readLocalFile2Text(context: Context, fileName: String, charset: String = "UTF-8"): String {
        val inPut: InputStreamReader
        try {
            inPut = InputStreamReader(context.assets.open(fileName), charset)
            val buffReader = BufferedReader(inPut)
            var line: String? = ""
            val sb = StringBuilder()
            line = buffReader.readLine()
            while (line != null) {
                sb.append(line)
                line = buffReader.readLine()
            }
            inPut.close()
            buffReader.close()

            return sb.toString()
        } catch (e: Exception) {
        }
        return ""
    }

    //获取目录下所有文件名字
    fun getAllFileName4Dir(dirPath: String): ArrayList<String> {
        val result: ArrayList<String> = ArrayList()
        val dir = File(dirPath)
        if (!dir.isDirectory) return result

        val files = File(dirPath).listFiles() ?: return result

        for (it in files.indices) result.add(files[it].name)

        return result
    }

}