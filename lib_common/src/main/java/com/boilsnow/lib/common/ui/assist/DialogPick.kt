package com.boilsnow.lib.common.ui.assist

import android.content.Context
import com.boilsnow.lib.common.R
import com.boilsnow.lib.common.entente.ViewEntente

/**
 * Description:弹窗管理
 * Remark:
 */
object DialogPick {

    //提示警告
    fun showWarnView(context: Context, text: String, click: ViewEntente.OnClickAction? = null) =
        AffirmOrCancelDialog(context)
            .setContent(text)
            .setTitle(context.getString(R.string.l00_text_safety))
            .setAffirm(context.getString(R.string.l00_text_affirm))
            .setForce(true)
            .setOnActionListener(click)
            .show()
}