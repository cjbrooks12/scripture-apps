package com.copperleaf.biblebits.platform.services.authorization.openfga.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFgaListUsersResponse(
    val users: List<User>,
) {
    @Serializable
    data class User(
        @SerialName("object") val _object: OpenFgaObject,
    )
}
