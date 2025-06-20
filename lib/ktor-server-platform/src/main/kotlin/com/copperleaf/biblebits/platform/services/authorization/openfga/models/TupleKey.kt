package com.copperleaf.biblebits.platform.services.authorization.openfga.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class TupleKey(
    val user: String,
    val relation: String,
    @SerialName("object") val _object: String,
) {
    companion object {
        operator fun invoke(
            user: String,
            relation: String,
            objectType: String,
            objectId: String,
        ) = TupleKey(user, relation, "$objectType:$objectId")
    }
}
