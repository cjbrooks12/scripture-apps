package com.caseyjbrooks.platform.configuration.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("driver")
sealed interface BlobStorageDriver {

    @Serializable
    @SerialName("Filesystem")
    data class Filesystem(
        val path: String,
    ) : BlobStorageDriver
}
