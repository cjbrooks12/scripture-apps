@file:OptIn(ExperimentalTime::class)

package com.copperleaf.server

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import com.copperleaf.ballast.debugger.utils.minus
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.parseAuthorizationHeader
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.principal
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.util.encodeBase64
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.toMap
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.Date
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

@Serializable
data class Message(
    val path: String,
    val query: Map<String, List<String>>,
    val message: String,
    val status: Int,
)

@Serializable
data class LoginWithUsernameAndPassword(
    val username: String,
    val password: String,
)

@Serializable
data class LoginWithPin(
    val pin: String,
)

@Serializable
data class AuthToken(
    val authToken: String,
    val refreshToken: String,
)

private suspend fun ApplicationCall.sendMessage(statusCode: HttpStatusCode, message: String) {
    this.response.headers.append(Header_Message, message)
    this.respond(
        status = statusCode,
        message = Message(
            path = this.request.path(),
            query = this.request.queryParameters.toMap(),
            message = message,
            status = statusCode.value,
        ),
    )
}

private suspend fun PipelineContext<*, ApplicationCall>.refreshTokenPair(
    message: String,
) {
    val principal = call.principal<JWTPrincipal>()
    val username = principal!!.payload.getClaim(Claim_Username).asString()
    addTokenPairToHeaders(username, message)
}
private suspend fun PipelineContext<*, ApplicationCall>.addTokenPairToHeaders(
    username: String,
    message: String,
) {
    val authToken = newJwt(username, Claim_TokenType_Authorization, 12.hours)
    val refreshToken = newJwt(username, Claim_TokenType_Refresh, 7.days)

    call.response.headers.append(Header_AuthToken, authToken)
    call.response.headers.append(Header_RefreshToken, refreshToken)

    call.sendMessage(HttpStatusCode.OK, message)
}


val timezone = TimeZone.currentSystemDefault()

public fun LocalDateTime.Companion.now(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(timezone)
}

operator fun LocalDateTime.plus(duration: Duration): LocalDateTime {
    return (this.toInstant(timezone) + duration).toLocalDateTime(timezone)
}


operator fun Date.minus(duration: LocalDateTime): Duration {
    return Instant.fromEpochMilliseconds(this.time).toLocalDateTime(timezone) - duration
}

public fun JWTCreator.Builder.withExpiresAt(datetime: LocalDateTime): JWTCreator.Builder {
    return this.withExpiresAt(Date(datetime.toInstant(timezone).toEpochMilliseconds()))
}

public fun newJwt(
    username: String,
    tokenType: String,
    ttl: Duration,
): String {
    val nonce = Random.nextBytes(8).encodeBase64()
    val audience = when (tokenType) {
        Claim_TokenType_Authorization -> {
            authTokenNonce = nonce
            "${jwtIssuer}authorized"
        }
        Claim_TokenType_Refresh -> {
            refreshTokenNonce = nonce
            "${jwtIssuer}refresh"
        }
        else -> {
            error("invalid token type")
        }
    }

    return JWT.create()
        .withAudience(audience)
        .withIssuer(jwtIssuer)
        .withClaim(Claim_Username, username)
        .withClaim(Claim_TokenType, tokenType)
        .withClaim(Claim_Nonce, nonce)
        .withExpiresAt(LocalDateTime.now() + ttl)
        .sign(Algorithm.HMAC256(jwtSecret))
}

const val hardcodedUsername = "testUser"
const val hardcodedPassword = "password1"
var registerdPin: String? = null
var authTokenNonce: String? = null
var refreshTokenNonce: String? = null

const val jwtSecret = "secret"
const val jwtIssuer = "http://0.0.0.0:8080/"

const val Header_AuthToken = "X-AuthToken"
const val Header_RefreshToken = "X-RefreshToken"
const val Header_Message = "X-Message"

const val Claim_Username = "username"
const val Claim_TokenType = "tokenType"
const val Claim_TokenType_Authorization = "authorization"
const val Claim_TokenType_Refresh = "refresh"
const val Claim_Nonce = "nonce"

fun main() {
    embeddedServer(CIO, port = 8080) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                }
            )
        }
        install(Authentication) {
            jwt("authToken") {
                realm = "Access to routes which need an auth token"
                verifier(
                    JWT
                        .require(Algorithm.HMAC256(jwtSecret))
                        .withAudience("${jwtIssuer}authorized")
                        .withIssuer(jwtIssuer)
                        .build()
                )

                validate { credential ->
                    val claimUsername = credential.payload.getClaim(Claim_Username).asString()
                    val claimTokenType = credential.payload.getClaim(Claim_TokenType).asString()
                    val claimNonce = credential.payload.getClaim(Claim_Nonce).asString()

                    println("""
                        |claimUsername: $claimUsername
                        |claimTokenType: $claimTokenType
                        |claimNonce: $claimNonce
                        |authTokenNonce: $authTokenNonce
                    """.trimMargin())

                    if (claimUsername.isNotBlank() &&
                        claimTokenType == Claim_TokenType_Authorization &&
                        claimNonce == authTokenNonce
                    ) {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                }
                challenge { _, _ ->
                    call.sendMessage(HttpStatusCode.Unauthorized, "Auth token is not valid or has expired")
                }
            }
            jwt("refreshToken") {
                realm = "Access to routes that need only a refresh token"
                verifier(
                    JWT
                        .require(Algorithm.HMAC256(jwtSecret))
                        .withAudience("${jwtIssuer}refresh")
                        .withIssuer(jwtIssuer)
                        .build()
                )
                authHeader {
                    it.request.headers[Header_RefreshToken]
                        ?.let {refreshTokenValue ->
                            parseAuthorizationHeader(refreshTokenValue)
                        }
                }

                validate { credential ->
                    val claimUsername = credential.payload.getClaim(Claim_Username).asString()
                    val claimTokenType = credential.payload.getClaim(Claim_TokenType).asString()
                    val nonce = credential.payload.getClaim(Claim_Nonce).asString()

                    if (claimUsername.isNotBlank() &&
                        claimTokenType == Claim_TokenType_Refresh &&
                        nonce == refreshTokenNonce
                    ) {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                }
                challenge { _, _ ->
                    call.sendMessage(HttpStatusCode.Unauthorized, "Refresh token is not valid or has expired")
                }
            }
        }
        install(CallLogging) {
            format { call ->
                val status = call.response.status()
                val httpMethod = call.request.httpMethod.value
                val path = call.request.path()

                buildString {
                    appendLine("$status --> $httpMethod $path ")

                    appendLine("")

                    appendLine("Request")
                    call.request.headers.entries().forEach { (key, value) ->
                        appendLine("    $key: $value")
                    }

                    appendLine("Response")
                    call.response.headers.allValues().entries().forEach { (key, value) ->
                        appendLine("    $key: $value")
                    }
                    appendLine("$status <-- $httpMethod $path")
                }
            }
        }

        routing {
            // routes available without authorization needed
            get {
                call.sendMessage(HttpStatusCode.OK, "Index")
            }
            route("/public") {
                get {
                    call.sendMessage(HttpStatusCode.OK, "Public Resource")
                }
            }

            // routes that require authorization to access
            authenticate("authToken") {
                route("/authorized") {
                    // an example protected route
                    get("/tokenInfo") {
                        val principal = call.principal<JWTPrincipal>()
                        val username = principal!!.payload.getClaim(Claim_Username).asString()
                        val expiry = principal.expiresAt!!
                        val ttl = expiry - LocalDateTime.now()

                        call.sendMessage(HttpStatusCode.OK, "Hello, $username! Your access token expires in $ttl")
                    }

                    // register a PIN
                    post("/pin") {
                        val requestBody = call.receive<LoginWithPin>()
                        registerdPin = requestBody.pin
                        call.sendMessage(HttpStatusCode.OK, "Successfully registered PIN")
                    }

                    // manually request an update to access and refresh tokens
                    post("/refresh") {
                        refreshTokenPair("Access and Refresh tokens have been renewed")
                    }

                    // logout, expiring tokens and deleting the PIN
                    post("/logout") {
                        registerdPin = null
                        authTokenNonce = null
                        refreshTokenNonce = null
                        call.sendMessage(HttpStatusCode.OK, "Successfully logged out")
                    }
                }
            }

            // routes that require a valid refresh token, but not a valid auth token
            authenticate("refreshToken") {
                route("/refresh") {
                    // an example protected route
                    get("/tokenInfo") {
                        val principal = call.principal<JWTPrincipal>()
                        val username = principal!!.payload.getClaim(Claim_Username).asString()
                        val expiry = principal.expiresAt!!
                        val ttl = expiry - LocalDateTime.now()

                        call.sendMessage(HttpStatusCode.OK, "Hello, $username! Your refresh token expires in $ttl")
                    }

                    // login with PIN to get an access token and a new refresh token, as long as the given refresh token is
                    // still valid
                    post("/pin") {
                        val user = call.receive<LoginWithPin>()

                        if (registerdPin == null) {
                            call.sendMessage(HttpStatusCode.NotFound, "you must register a PIN first")
                        } else if (user.pin != registerdPin) {
                            call.sendMessage(HttpStatusCode.NotFound, "Incorrect PIN")
                        } else {
                            refreshTokenPair("PIN Login Successful")
                        }
                    }
                }
            }

            // routes for authenticating
            route("/login") {
                // login with username and password to get an access token and refresh token
                post {
                    val user = call.receive<LoginWithUsernameAndPassword>()

                    if (user.username == hardcodedUsername && user.password == hardcodedPassword) {
                        addTokenPairToHeaders(user.username, "Login Successful")
                    } else if (user.username != hardcodedUsername) {
                        call.sendMessage(HttpStatusCode.NotFound, "Invalid username")
                    } else if (user.password != hardcodedPassword) {
                        call.sendMessage(HttpStatusCode.NotFound, "Invalid password")
                    } else {
                        call.sendMessage(HttpStatusCode.InternalServerError, "something went horribly wrong")
                    }
                }
            }
        }
    }.start(wait = true)
}
