package com.caseyjbrooks.scripturenow.api.auth

import com.caseyjbrooks.scripturenow.config.LocalAppConfig
import com.caseyjbrooks.scripturenow.models.auth.SessionService
import io.ktor.client.*

public expect object SessionProvider {
    public fun get(
        provider: SessionService,
        config: LocalAppConfig,
        client: HttpClient,
    ): Session
}
