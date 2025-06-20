package com.copperleaf.biblebits.platform.configuration

import com.auth0.jwk.JwkProviderBuilder
import com.copperleaf.biblebits.platform.configuration.models.AuthenticationDriver
import com.copperleaf.biblebits.platform.services.authorization.authorization
import com.copperleaf.biblebits.platform.services.authorization.oauth.oauth
import com.copperleaf.biblebits.platform.services.authorization.openfga.models.TupleKey
import com.copperleaf.biblebits.platform.services.authorization.openfga.openFga
import com.copperleaf.biblebits.platform.util.extractConfiguration
import io.ktor.server.application.Application
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import org.koin.core.KoinApplication
import org.koin.ktor.plugin.scope
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
            userId = { principal<JWTPrincipal>()?.subject },
            defaultContextualTuples = { userId ->
                buildList {
                    this += TupleKey(
                        user = "user:$userId",
                        relation = "member",
                        _object = "role:member",
                    )
                }
            }
        )

        oauth(
            jwtScopes = {
                principal<JWTPrincipal>()
                    ?.payload
                    ?.getClaim("scope")
                    ?.asString()
                    ?.split(" ")
            }
        )
    }
}

// Authentication Routing
// ---------------------------------------------------------------------------------------------------------------------

public fun Route.authenticateRoutes(required: Boolean, name: String? = null, block: Route.() -> Unit) {
    authenticate(name, optional = !required) {
        block()
    }
}

