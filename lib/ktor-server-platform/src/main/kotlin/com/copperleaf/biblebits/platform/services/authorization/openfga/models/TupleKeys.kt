package com.copperleaf.biblebits.platform.services.authorization.openfga.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TupleKeys(
    @SerialName("tuple_keys") val tupleKeys: List<TupleKey>,
)
