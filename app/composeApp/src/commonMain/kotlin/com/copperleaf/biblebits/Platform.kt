package com.copperleaf.biblebits

import com.copperleaf.biblebits.auth.AuthServiceImpl
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.host
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

abstract class Platform {
    abstract val name: String

    val httpClient: HttpClient by lazy {
        httpClient {
            install(Auth) {
                bearer {
                    loadTokens {
                        // Load tokens from a local storage and return them as the 'BearerTokens' instance
                        authService.getAuthToken()
                    }

                    refreshTokens {
                        authService.refreshAuthToken(this)
                    }

                    sendWithoutRequest {
                        it.host == "10.0.2.2"
                    }
                }
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        this@Platform.log(message)
                    }
                }
            }
            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    val authService by lazy {
        AuthServiceImpl(this@Platform)
    }

    abstract val authLogInEndpoint: String
    abstract val authLogOutEndpoint: String
    abstract val authTokenEndpoint: String
    abstract val authClientId: String
    abstract val authRedirectUri: String

    abstract fun openWebpage(url: String)

    abstract fun log(text: String)

    abstract fun error(text: String)
}

expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient
