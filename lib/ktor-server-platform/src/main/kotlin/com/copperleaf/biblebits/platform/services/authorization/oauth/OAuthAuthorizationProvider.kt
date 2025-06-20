package com.copperleaf.biblebits.platform.services.authorization.oauth

import com.copperleaf.biblebits.platform.services.authorization.AuthorizationConfig
import com.copperleaf.biblebits.platform.services.authorization.AuthorizationProvider
import com.copperleaf.biblebits.platform.services.authorization.oauth.models.OAuthAuthorizationRequest
import io.ktor.server.application.ApplicationCall

class OAuthAuthorizationProvider(
    override val name: String?,
    val jwtScopes: ApplicationCall.() -> List<String>?,
) : AuthorizationProvider<OAuthAuthorizationRequest> {

    override suspend fun checkAuthorization(
        request: OAuthAuthorizationRequest,
    ): Result<Boolean> {
        return runCatching { false }
    }

    override fun copy(): OAuthAuthorizationProvider {
        return OAuthAuthorizationProvider(
            name = name,
            jwtScopes = jwtScopes,
        )
    }
}

fun AuthorizationConfig.oauth(
    name: String? = null,
    jwtScopes: ApplicationCall.() -> List<String>?,
) {
    addProvider(
        OAuthAuthorizationProvider(
            name = name,
            jwtScopes = jwtScopes,
        )
    )
}
