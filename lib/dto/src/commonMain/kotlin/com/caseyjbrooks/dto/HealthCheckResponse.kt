package com.caseyjbrooks.dto

import kotlinx.serialization.Serializable

@Serializable
public data class HealthCheckResponse(val healthy: Boolean)
