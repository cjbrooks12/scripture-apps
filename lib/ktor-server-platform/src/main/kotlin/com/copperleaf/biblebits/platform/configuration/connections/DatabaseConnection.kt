package com.copperleaf.biblebits.platform.configuration.connections

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("driver")
sealed interface DatabaseConnection {
    @Serializable
    @SerialName("Postgres")
    data class Postgres(
        val postgresConnectionString: String,
    ) : DatabaseConnection
}
