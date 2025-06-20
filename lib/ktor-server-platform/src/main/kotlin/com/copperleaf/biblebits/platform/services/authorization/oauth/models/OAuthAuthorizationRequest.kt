package com.copperleaf.biblebits.platform.services.authorization.oauth.models

data class OAuthAuthorizationRequest(
    val requestedScopes: Set<String>,
)
