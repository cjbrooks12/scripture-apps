package com.copperleaf.biblebits.platform.services.authorization.openfga.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFgaReadResponse(
    val tuples: List<Tuple>,
) {
    @Serializable
    data class Tuple(
        val key: Key,
        val timestamp: Instant,
    ) {
        @Serializable
        data class Key(
            @SerialName("user") val user: String,
            @SerialName("relation") val relation: String,
            @SerialName("object") val _object: String,
        )
    }
}
