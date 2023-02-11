package com.caseyjbrooks.scripturenow.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public inline fun <T, U> Flow<List<T>>.mapEach(crossinline fn: (T) -> U): Flow<List<U>> {
    return this
        .map { list ->
            list.map(fn)
        }
}

public inline fun <T, U> Flow<T?>.mapIfNotNull(crossinline fn: (T) -> U): Flow<U?> {
    return this
        .map { value ->
            if (value != null) fn(value) else null
        }
}
