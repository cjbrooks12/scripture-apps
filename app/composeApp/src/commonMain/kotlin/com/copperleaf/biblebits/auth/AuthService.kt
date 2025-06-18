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
        authorizationCode: String?,
    )
}

class AuthServiceImpl(
    private val platform: Platform,
) : AuthService {

    private val bearerTokenStorage: MutableList<BearerTokens> = mutableListOf()

    override suspend fun requestLogIn(
        scopes: List<String>,
    ) {
        val authorizationUrlQuery = parameters {
            append("client_id", platform.authClientId)
            append("scope", scopes.joinToString(" "))
            append("response_type", "code")
            append("redirect_uri", platform.authRedirectUri)
            append("access_type", "offline")
        }.formUrlEncode()

        platform.openWebpage("${platform.authLogInEndpoint}?$authorizationUrlQuery")
    }

    override suspend fun requestLogOut() {
        val authorizationUrlQuery = parameters {
            append("client_id", platform.authClientId)
            append("post_logout_redirect_uri", platform.authRedirectUri)
        }.formUrlEncode()

        platform.openWebpage("${platform.authLogOutEndpoint}?$authorizationUrlQuery")
        bearerTokenStorage.clear()
    }

    override suspend fun getAuthToken(): BearerTokens? {
        return bearerTokenStorage.lastOrNull()
    }

    override suspend fun refreshAuthToken(
        params: RefreshTokensParams,
    ): BearerTokens? = with(params) {
        try {
            val refreshTokenInfo: TokenInfo = client.submitForm(
                url = platform.authTokenEndpoint,
                formParameters = parameters {
                    append("grant_type", "refresh_token")
                    append("client_id", platform.authClientId)
                    append("refresh_token", oldTokens?.refreshToken ?: "")
                }
            ){ markAsRefreshTokenRequest() }.body()

            bearerTokenStorage.add(BearerTokens(refreshTokenInfo.accessToken, oldTokens?.refreshToken!!))
        } catch (e: Exception) {
            // ignore
        }
        return bearerTokenStorage.lastOrNull()
    }

    override suspend fun getAuthToken(
        authorizationCode: String?,
    ) {
        if(authorizationCode != null) {
            val tokenInfo: TokenInfo = platform.httpClient.submitForm(
                url = platform.authTokenEndpoint,
                formParameters = parameters {
                    append("grant_type", "authorization_code")
                    append("code", authorizationCode)
                    append("client_id", platform.authClientId)
                    append("redirect_uri", platform.authRedirectUri)
                }
            ).body()

            bearerTokenStorage.add(BearerTokens(tokenInfo.accessToken, tokenInfo.refreshToken!!))
        }
    }
}
