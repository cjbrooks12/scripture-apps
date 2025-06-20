package com.copperleaf.biblebits.platform.services.authorization.openfga.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFgaListUsersRequest(
    @SerialName("object") val _object: OpenFgaObject,
    val relation: String,
    @SerialName("user_filters") val userFilters: List<Filter>,
    @SerialName("authorization_model_id") val authorizationModelId: String,
)
