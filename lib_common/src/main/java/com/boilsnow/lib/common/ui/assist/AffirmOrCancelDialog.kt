package com.boilsnow.lib.common.ui.assist

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import com.boilsnow.lib.common.R
import com.boilsnow.lib.common.entente.ViewEntente

/**
 * Description:确认或取消弹窗
 * Remark:
 */
class AffirmOrCancelDialog(mContext: Context) : Dialog(mContext), View.OnClickListener {

    private var tvTitle: TextView
    private var vTitle: View
    private var tvConnect: TextView
    private var tvCancel: TextView
    private var vCancel: View
    private var tvAffirm: TextView

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.l00_dialog_affirm_or_cancel, null)

        tvTitle = view.findViewById(R.id.tvTitle)
        vTitle = view.findViewById(R.id.vTitle)
        tvConnect = view.findViewById(R.id.tvConnect)
        tvAffirm = view.findViewById(R.id.tvAffirm)
        vCancel = view.findViewById(R.id.vCancel)
        tvCancel = view.findViewById(R.id.tvCancel)

        tvAffirm.setOnClickListener(this)
        tvCancel.setOnClickListener(this)

        setContentView(view)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        val params = window?.attributes
        val metrics = context.applicationContext.resources.displayMetrics
        params?.width = metrics.widthPixels * 3 / 4
        params?.height = -2
        params?.gravity = Gravity.CENTER
        window?.attributes = params
        setCancelable(false)
    }

    fun setTitle(title: String): AffirmOrCancelDialog {
        tvTitle.text = title
        if (TextUtils.isEmpty(title)) {
            tvTitle.visibility = View.GONE
            vTitle.visibility = View.GONE
        } else {
            tvTitle.visibility = View.VISIBLE
            vTitle.visibility = View.VISIBLE
        }
        return this
    }

    fun setContent(connect: String): AffirmOrCancelDialog {
        tvConnect.text = connect
        return this
    }

    fun setAffirm(affirm: String): AffirmOrCancelDialog {
        tvAffirm.text = affirm
        return this
    }

    fun setCancel(cancel: String): AffirmOrCancelDialog {
        tvCancel.text = cancel
        return this
    }

    fun setForce(isForce: Boolean): AffirmOrCancelDialog {
        if (isForce) {
            tvCancel.visibility = View.GONE
            vCancel.visibility = View.GONE
        } else {
            tvCancel.visibility = View.VISIBLE
            vCancel.visibility = View.VISIBLE
        }
        return this
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvAffirm -> mAction?.onAffirm()
            R.id.tvCancel -> mAction?.onCancel()
        }
        dismiss()
    }

    private var mAction: ViewEntente.OnClickAction? = null

    fun setOnActionListener(onAction: ViewEntente.OnClickAction?): AffirmOrCancelDialog {
        mAction = onAction
        return this
    }
}