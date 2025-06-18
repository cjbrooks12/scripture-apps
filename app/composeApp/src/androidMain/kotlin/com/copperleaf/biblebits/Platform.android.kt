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
import io.ktor.client.plugins.defaultRequest
import java.util.concurrent.TimeUnit

class AndroidPlatform(
    private val appContext: Context,
) : Platform() {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    private val authBaseUrl: String = "http://10.0.2.2:4567/auth/realms/biblebits/protocol/openid-connect"
    override val authLogInEndpoint: String = "$authBaseUrl/auth"
    override val authLogOutEndpoint: String = "$authBaseUrl/logout"
    override val authTokenEndpoint: String = "$authBaseUrl/token"
    override val authClientId: String = "end_users"
    override val authRedirectUri: String = "bibleBits://app/login"

    override fun openWebpage(url: String) {
        log("opening webpage: $url")
        startActivity(
            appContext,
            Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            },
            Bundle(),
        )
    }

    override fun log(text: String) {
        Log.i("[Android]", text)
    }

    override fun error(text: String) {
        Log.e("[Android]", text)
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
