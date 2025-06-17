package com.copperleaf.biblebits

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.providers.BearerTokens

interface Platform {
    val name: String

    val bearerTokenStorage: MutableList<BearerTokens>
    val redirectContent: String?

    fun openWebpage(url: String)

    fun log(text: String)

    fun error(text: String)
}

expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient
