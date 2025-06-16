package com.caseyjbrooks.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AppResource(val openfgaObjectName: String) {
    @SerialName("course") Course("course");

    @Serializable
    enum class Role(val openfgaRelationName: String) {
        @SerialName("creator") Creator("creator"),
        @SerialName("editor") Editor("editor"),
        @SerialName("viewer") Viewer("viewer"),
    }
}
