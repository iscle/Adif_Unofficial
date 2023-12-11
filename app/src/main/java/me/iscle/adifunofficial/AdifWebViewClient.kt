package me.iscle.adifunofficial

import android.webkit.HttpAuthHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class AdifWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (request == null) return shouldOverrideUrlLoading(view, request as WebResourceRequest?)
        return request.url.host?.contains("elcanoweb.adif.es") != true
    }

    override fun onReceivedHttpAuthRequest(
        view: WebView?,
        handler: HttpAuthHandler?,
        host: String?,
        realm: String?
    ) {
        if (handler != null) {
            handler.proceed("deimos", "deimostt")
            return
        }

        super.onReceivedHttpAuthRequest(view, handler, host, realm)
    }
}