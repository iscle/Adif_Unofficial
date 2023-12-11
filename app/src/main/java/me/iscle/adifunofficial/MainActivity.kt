package me.iscle.adifunofficial

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import dagger.hilt.android.AndroidEntryPoint
import me.iscle.adifunofficial.ui.theme.AdifUnofficialTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdifUnofficialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AdifWebView()
                }
            }
        }
    }

    @Composable
    fun AdifWebView() {
        var webView = remember<WebView?> { null }

        BackHandler {
            val webView = webView
            if (webView?.canGoBack() == true) {
                webView.goBack()
            } else {
                finish()
            }
        }

        AndroidView(
            factory = {
                WebView(it).apply {
                    webView = this
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    webViewClient = AdifWebViewClient()
                    loadUrl("https://elcanoweb.adif.es")
                }
            }
        )
    }
}