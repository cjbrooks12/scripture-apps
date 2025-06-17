package com.caseyjbrooks.platform.configuration

import com.auth0.jwk.JwkProviderBuilder
import com.caseyjbrooks.platform.configuration.models.AuthenticationDriver
import com.caseyjbrooks.platform.services.authorization.OpenFgaAuthorizationService
import com.caseyjbrooks.platform.services.authorization.authorization
import com.caseyjbrooks.platform.services.authorization.openFga
import com.caseyjbrooks.platform.util.extractConfiguration
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import org.koin.core.KoinApplication
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlin.time.toJavaDuration


fun Application.configureSecurity(koinApplication: KoinApplication) {
    val authenticationDriver: AuthenticationDriver = environment.config.extractConfiguration("security.jwt");

    authentication {
        jwt {
            realm = authenticationDriver.realm

            with(authenticationDriver) {
                val jwkProvider = JwkProviderBuilder(URL(jwksUrl))
                    .cached(jwkCacheSize, jwkCacheDuration.toJavaDuration())
                    .rateLimited(jwkCacheSize, 1, TimeUnit.MINUTES)
                    .build()

                verifier(jwkProvider) {
                    withIssuer(*authenticationDriver.issuer.toTypedArray())
                    acceptLeeway(3)
                }
            }

            validate { credential ->
                JWTPrincipal(credential.payload)
            }
        }
    }
    authorization {
        openFga(
            service = OpenFgaAuthorizationService(),
            userId = { principal<JWTPrincipal>()?.subject },
            roles = { emptyList() },
        )
    }
}

// Authentication Routing
// ---------------------------------------------------------------------------------------------------------------------

public fun Route.authenticateRoutes(required: Boolean, block: Route.() -> Unit) {
    authenticate(optional = !required) {
        block()
    }
}
