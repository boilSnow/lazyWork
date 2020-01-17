package com.boilsnow.lib.common.picture

import java.io.Serializable

/**
 * Description:图片配置信息
 * Remark:
 */
internal class PictureConfig(val isCamera: Boolean = false, val isCrop: Boolean = false) :
    Serializable {
    var picDir = ""                     //图片生成目录
    var cropWidth = 800
        private set
    var cropHeight = 600
        private set

    fun setSize4Crop(width: Int, height: Int) {
        if (isCrop) {
            cropWidth = width
            cropHeight = height
        }
    }
}