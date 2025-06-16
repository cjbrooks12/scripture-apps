package com.caseyjbrooks.platform.configuration.models

import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

@Serializable
data class AuthenticationDriver(
    val issuer: String,
    val jwksUrl: String,
    val realm: String,
    val jwkCacheDuration: Duration = 24.hours,
    val jwkCacheSize: Long = 10L,
)
