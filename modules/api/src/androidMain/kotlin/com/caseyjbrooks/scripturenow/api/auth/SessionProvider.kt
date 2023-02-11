package com.caseyjbrooks.scripturenow.api.auth

import com.caseyjbrooks.scripturenow.config.LocalAppConfig
import com.caseyjbrooks.scripturenow.models.auth.SessionService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.ktor.client.*

public actual object SessionProvider {
    public actual fun get(
        provider: SessionService,
        config: LocalAppConfig,
        client: HttpClient,
    ): Session {
        return when (provider) {
            SessionService.Firebase -> FirebaseSession(
                firebaseAuth = Firebase.auth,
                converter = FirebaseSessionConverterImpl(),
            )
        }
    }
}
