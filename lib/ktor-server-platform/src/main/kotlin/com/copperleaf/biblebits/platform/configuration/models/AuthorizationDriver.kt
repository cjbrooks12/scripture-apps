package com.copperleaf.biblebits.platform.configuration.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("driver")
sealed interface AuthorizationDriver {

    @Serializable
    @SerialName("OpenFGA")
    data class OpenFga(
        val host: String,
    ) : AuthorizationDriver
}
