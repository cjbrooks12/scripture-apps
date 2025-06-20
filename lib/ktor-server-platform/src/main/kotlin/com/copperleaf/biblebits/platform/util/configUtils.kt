package com.copperleaf.biblebits.platform.util

import io.ktor.server.config.ApplicationConfig


inline fun <reified T: Enum<T>> ApplicationConfig.enumProperty(name: String): T {
    val stringValue = property(name).getString()

    return T::class.java.enumConstants!!.first { it.name == stringValue }
}
