package com.copperleaf.biblebits.platform.services.authorization.openfga.models

import kotlinx.serialization.Serializable

@Serializable
data class OpenFgaCheckResponse(
    val allowed: Boolean
)
