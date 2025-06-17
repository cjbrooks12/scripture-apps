package com.copperleaf.biblebits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.caseyjbrooks.dto.HealthCheckResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.encodedPath
import io.ktor.http.formUrlEncode
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview

@Serializable
data class UserInfo(
    val id: String,
    val name: String,
    @SerialName("given_name") val givenName: String,
    @SerialName("family_name") val familyName: String,
    val picture: String,
    val locale: String
)

@Serializable
data class TokenInfo(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Int,

    @SerialName("refresh_token") val refreshToken: String? = null,
//    @SerialName("refresh_expires_in") val refreshExpiresIn: Int,

    val scope: String,
    @SerialName("token_type") val tokenType: String,
//    @SerialName("id_token") val idToken: String,
)

@Composable
fun App(
    platform: Platform,
) {
    val http = remember {
        httpClient {
            install(Auth) {
                bearer {
                    loadTokens {
                        // Load tokens from a local storage and return them as the 'BearerTokens' instance
                        platform.bearerTokenStorage.lastOrNull()
                    }

                    refreshTokens { // this: RefreshTokensParams
                        val refreshTokenInfo: TokenInfo = client.submitForm(
                            url = "https://accounts.google.com/o/oauth2/token",
                            formParameters = parameters {
                                append("grant_type", "refresh_token")
                                append("client_id", "end_users")
                                append("refresh_token", oldTokens?.refreshToken ?: "")
                            }
                        ) { markAsRefreshTokenRequest() }.body()
                        platform.bearerTokenStorage.add(BearerTokens(refreshTokenInfo.accessToken, oldTokens?.refreshToken!!))
                        platform.bearerTokenStorage.last()
                    }

                    sendWithoutRequest { request ->
                        request.url.host == "www.googleapis.com" || request.url.encodedPath.startsWith("/api/v1/public")
                    }
                }
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        platform.log(message)
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

    App(
        platform = platform,
        httpClient = http,
    )
}

@Composable
@Preview
private fun App(
    platform: Platform,
    httpClient: HttpClient,
) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var key: Int by remember { mutableStateOf(0) }

            Text("Hello, ${platform.name}!")
            if(platform.redirectContent != null) {
                Text("redirected: ${platform.redirectContent}")
            }
            Text("key: $key")
            HorizontalDivider()

            HttpRequest<HealthCheckResponse>(httpClient, path = "/api/v1/public/health", key = key)
            HorizontalDivider()

            HttpRequest<HealthCheckResponse>(httpClient, path = "/api/v1/protected/health", key = key)
            HorizontalDivider()

            HttpRequest<HealthCheckResponse>(httpClient, path = "/api/v1/admin/health", key = key)
            HorizontalDivider()

            RetryApisButton(
                onClick = { key++ }
            )
            LogInButton(platform, httpClient)
        }
    }
}

@Composable
private inline fun <reified T> HttpRequest(
    httpClient: HttpClient = httpClient(),
    path: String,
    key: Int,
)  {
    val response by produceState<Pair<HttpResponse?, T?>>(
        initialValue = null to null,
        key1 = key,
    ) {
        val res = httpClient.get(path)
        value = res to runCatching { res.body<T>() }.getOrNull()
    }
    val (publicResponse, publicBody) = response

    Text(path, style = MaterialTheme.typography.bodyLarge)
    publicResponse?.let { response ->
        Text("Status Code: ${response.status}")
    }
    publicBody?.let { body ->
        Text("Body: $body")
    }
}

@Composable
private fun LogInButton(
    platform: Platform,
    httpClient: HttpClient,
) {
    Button(onClick = {
        val authorizationUrlQuery = parameters {
            append("client_id", "end_users")
            append("scope", "profile")
            append("response_type", "code")
            append("redirect_uri", "bibleBits://app/login")
            append("access_type", "offline")
        }.formUrlEncode()
        val url = "http://10.0.2.2:4567/auth/realms/biblebits/protocol/openid-connect/auth?$authorizationUrlQuery"
        platform.log("opening URL: $url")
        platform.openWebpage(url)
    }) {
        Text("Log In")
    }

    LaunchedEffect(platform) {
        if(platform.redirectContent != null) {
            val tokenInfo: TokenInfo = httpClient.submitForm(
                url = "http://10.0.2.2:4567/auth/realms/biblebits/protocol/openid-connect/token",
                formParameters = parameters {
                    append("grant_type", "authorization_code")
                    append("code", platform.redirectContent!!)
                    append("client_id", "end_users")
//                    append("client_secret", "DFuAReAUjTK8Jg1bmzvToNrS6F0G40Xy")
                    append("redirect_uri", "bibleBits://app/login")
                }
            ).body()

            platform.log("token info: $tokenInfo")
            platform.bearerTokenStorage.add(BearerTokens(tokenInfo.accessToken, tokenInfo.refreshToken!!))
        }
    }
}

@Composable
private fun RetryApisButton(
    onClick: () -> Unit,
) {
    Button(onClick = onClick) {
        Text("Retry APIs")
    }
}
