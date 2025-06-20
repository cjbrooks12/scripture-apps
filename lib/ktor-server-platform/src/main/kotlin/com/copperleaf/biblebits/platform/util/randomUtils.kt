package com.copperleaf.biblebits.platform.util

import java.math.BigInteger
import java.security.SecureRandom

fun secureRandomString(length: Int): String {
    val secureRandom = SecureRandom()
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + listOf('-', '.', '_', '~')

    return buildString {
        repeat(length) {
            val index: Int = secureRandom.nextInt(allowedChars.size)
            append(allowedChars[index])
        }
    }
}
