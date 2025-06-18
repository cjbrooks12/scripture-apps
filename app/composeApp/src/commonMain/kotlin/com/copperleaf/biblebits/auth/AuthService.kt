package com.copperleaf.biblebits.auth

import com.copperleaf.biblebits.Platform
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.request.forms.submitForm
import io.ktor.http.formUrlEncode
import io.ktor.http.parameters

interface AuthService {

    suspend fun requestLogIn(
        scopes: List<String>,
    )

    suspend fun requestLogOut()

    suspend fun getAuthToken(): BearerTokens?

    suspend fun refreshAuthToken(
        params: RefreshTokensParams,
    ): BearerTokens?

    suspend fun getAuthToken(
        authorizationCode: String,
    )
}

class AuthServiceImpl(
    private val platform: Platform,
) : AuthService {

    private fun saveBearerToken(token: BearerTokens) {
        platform.log("Saving token:")
        platform.log("    accessToken: ${token.accessToken}")
        platform.log("    refreshToken: ${token.refreshToken}")
        platform.settings.putString(
            key = "accessToken",
            value = token.accessToken
        )
        if (token.refreshToken != null) {
            platform.settings.putString(
                key = "refreshToken",
                value = token.refreshToken!!
            )
        } else {
            platform.settings.remove("refreshToken")
        }
    }

    private fun getBearerToken(): BearerTokens? {
        val accessToken = platform.settings.getStringOrNull(key = "accessToken")
        val refreshToken = platform.settings.getStringOrNull(key = "refreshToken")

        if (accessToken != null && refreshToken != null) {
            return BearerTokens(accessToken, refreshToken)
        }

        return null
    }

    private fun saveCodeVerifier(codeVerifier: String) {
        platform.log("Saving codeVerifier: $codeVerifier")
        platform.settings.putString(
            key = "codeVerifier",
            value = codeVerifier
        )
    }

    private fun getCodeVerifier(): String {
        return platform.settings.getString(
            key = "codeVerifier",
            defaultValue = ""
        )
    }

    private fun createCodeVerifier(): String {
        return platform.secureRandomString(128).also {
            platform.log("new code verifier: $it")
        }
    }

    private fun createCodeChallenge(codeVerifier: String): String {
        return platform.sha256(codeVerifier).also {
            platform.log("new code challenge: $it")
        }
    }

    private fun clearBearerToken() {
        platform.log("clearing token")

        platform.settings.remove("accessToken")
        platform.settings.remove("refreshToken")
    }

    override suspend fun requestLogIn(
        scopes: List<String>,
    ) {
        val codeVerifier = createCodeVerifier()
        val codeChallenge = createCodeChallenge(codeVerifier)
        saveCodeVerifier(codeVerifier)

        platform.log("Redirecting to URL for login")
        val authorizationUrlQuery = parameters {
            append("client_id", platform.authClientId)
            append("scope", (scopes + listOf("email", "profile")).joinToString(" "))
            append("response_type", "code")
            append("redirect_uri", platform.authRedirectUri)
            append("access_type", "offline")
            append("code_challenge_method", "S256")
            append("code_challenge", codeChallenge)
        }.formUrlEncode()

        platform.openWebpage("${platform.authLogInEndpoint}?$authorizationUrlQuery")
    }

    override suspend fun requestLogOut() {
        platform.log("Redirecting to URL for logout")
        val authorizationUrlQuery = parameters {
            append("client_id", platform.authClientId)
            append("post_logout_redirect_uri", platform.authRedirectUri)
        }.formUrlEncode()

        platform.openWebpage("${platform.authLogOutEndpoint}?$authorizationUrlQuery")
        clearBearerToken()
    }

    override suspend fun getAuthToken(): BearerTokens? {
        platform.log("Getting bearer token from storage")
        return getBearerToken()
    }

    override suspend fun refreshAuthToken(
        params: RefreshTokensParams,
    ): BearerTokens? = with(params) {
        platform.log("refreshing bearer tokens")
        try {
            val refreshTokenInfo: TokenInfo = client.submitForm(
                url = platform.authTokenEndpoint,
                formParameters = parameters {
                    append("grant_type", "refresh_token")
                    append("client_id", platform.authClientId)
                    append("refresh_token", oldTokens?.refreshToken ?: "")
                    append("code_verifier", getCodeVerifier())
                }
            ) { markAsRefreshTokenRequest() }.body()

            val bearerToken = BearerTokens(refreshTokenInfo.accessToken, refreshTokenInfo.refreshToken)
            saveBearerToken(bearerToken)
            return bearerToken
        } catch (e: Exception) {
            // ignore
        }
        return getBearerToken()
    }

    override suspend fun getAuthToken(
        authorizationCode: String,
    ) {
        try {
            println("Getting new auth token from authorization code")
            val tokenInfo: TokenInfo = platform.httpClient.submitForm(
                url = platform.authTokenEndpoint,
                formParameters = parameters {
                    append("grant_type", "authorization_code")
                    append("code", authorizationCode)
                    append("client_id", platform.authClientId)
                    append("redirect_uri", platform.authRedirectUri)
                    append("code_verifier", getCodeVerifier())
                }
            ).body()

            val bearerToken = BearerTokens(tokenInfo.accessToken, tokenInfo.refreshToken!!)
            saveBearerToken(bearerToken)
        } catch (e: Exception) {

        }
    }
}
