package com.copperleaf.beer.models

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ResourceValue(val value: Long)

operator fun ResourceValue.minus(value: Long): ResourceValue {
    return ResourceValue(this.value - value)
}

operator fun ResourceValue.plus(value: Long): ResourceValue {
    return ResourceValue(this.value + value)
}
