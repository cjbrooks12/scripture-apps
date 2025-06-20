package com.copperleaf.biblebits.platform.services.authorization.openfga.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFgaWriteRequest(
    val writes: TupleKeys? = null,
    val deletes: TupleKeys? = null,
    @SerialName("authorization_model_id") val authorizationModelId: String,
)
