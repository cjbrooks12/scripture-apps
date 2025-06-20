package com.copperleaf.biblebits.platform.services.authorization.openfga.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFgaReadRequest(
    @SerialName("tuple_key") val tupleKey: ObjectTuple,
    @SerialName("authorization_model_id") val authorizationModelId: String,
)
