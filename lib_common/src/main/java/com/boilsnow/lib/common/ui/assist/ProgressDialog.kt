package com.boilsnow.lib.common.ui.assist

import android.app.Dialog
import android.content.Context
import android.view.*
import android.widget.TextView
import com.boilsnow.lib.common.R

/**
 * Description:进度条弹窗
 * Remark:
 */
class ProgressDialog(mContext: Context) : Dialog(mContext) {

    private var tvText: TextView

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.l00_dialog_progress, null)

        tvText = view.findViewById(R.id.tvText)

        setContentView(view)
        val window = window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        val params = window?.attributes
        val metrics = context.applicationContext.resources.displayMetrics
        params?.width = (metrics.density * 150).toInt()
        params?.height = (metrics.density * 50).toInt()
        params?.gravity = Gravity.CENTER
        window?.attributes = params
        setCancelable(false)
    }

    fun updateData(text: String) {
        tvText.text = text
    }
}