package com.copperleaf.biblebits

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.defaultRequest
import java.util.concurrent.TimeUnit


class AndroidPlatform(
    private val appContext: Context,
    override val redirectContent: String?,
) : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    override val bearerTokenStorage: MutableList<BearerTokens> = mutableListOf()

    override fun openWebpage(url: String) {
        startActivity(
            appContext,
            Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            },
            Bundle(),
        )
    }

    override fun log(text: String) {
        Log.i("Android", text)
    }

    override fun error(text: String) {
        Log.e("Android", text)
    }
}

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
    config(this)

    defaultRequest {
        url("http://10.0.2.2:9001/")
    }

    engine {
        config {
            retryOnConnectionFailure(true)
            connectTimeout(0, TimeUnit.SECONDS)
        }
    }
}
