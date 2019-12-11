package com.boilsnow.lib.common.ui

import android.text.TextUtils
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.boilsnow.lib.common.R
import com.boilsnow.lib.common.config.ExtrasConfig
import kotlinx.android.synthetic.main.l00_activity_web.*

/**
 * Description:网页界面
 * Remark:
 */
class WebActivity : BaseActivity() {

    override fun getLayoutID() = R.layout.l00_activity_web

    override fun setupViews() {
        findViewById<TextView>(R.id.tvTopTitle).text = mTitle

        val webSettings = wbView.settings

        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        wbView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
    }

    private var mUrl = ""
    private var mTitle = ""

    override fun initialized() {
        wbView.loadUrl(mUrl)
    }

    override fun verifyExtras(): Boolean {
        val extras = intent.extras
        if (extras != null) {
            mUrl = extras.getString(ExtrasConfig.ENTER_PATH, "")
            mTitle = extras.getString(ExtrasConfig.ENTER_TEXT, "")
            return !TextUtils.isEmpty(mUrl)
        }
        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && wbView.canGoBack()) {
            wbView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        wbView.removeAllViews()
        wbView.destroy()
    }
}