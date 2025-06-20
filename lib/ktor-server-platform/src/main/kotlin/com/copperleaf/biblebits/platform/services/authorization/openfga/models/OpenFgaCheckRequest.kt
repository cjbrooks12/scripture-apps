package com.copperleaf.biblebits.platform.services.authorization.openfga.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFgaCheckRequest(
    @SerialName("tuple_key") val tupleKey: TupleKey,
    @SerialName("authorization_model_id") val authorizationModelId: String,
    @SerialName("contextual_tuples") val contextualTuples: TupleKeys?,
)
