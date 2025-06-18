package com.copperleaf.biblebits

import android.R.id.input
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
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


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

    override fun secureRandomString(length: Int): String {
        val secureRandom = SecureRandom()
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + listOf('-', '.', '_', '~')

        return buildString {
            repeat(length) {
                val index: Int = secureRandom.nextInt(allowedChars.size)
                append(allowedChars[index])
            }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun sha256(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(input.toByteArray())
        return Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).encode(digest)
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
