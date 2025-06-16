package com.caseyjbrooks.platform.util


public inline fun <T> Iterable<T>.indexOfSingle(predicate: (T) -> Boolean): Int {
    var singleIndex: Int? = null
    for ((index, element) in this.withIndex()) {
        if (predicate(element)) {
            if (singleIndex != null) throw IllegalArgumentException("Collection contains more than one matching element.")
            singleIndex = index
        }
    }
    if (singleIndex == null) throw NoSuchElementException("Collection contains no element matching the predicate.")
    return singleIndex
}

fun <T> MutableList<T>.replaceSingle(predicate: (T) -> Boolean, update: (T) -> T): T {
    val oldValueIndex: Int = this.indexOfSingle(predicate)
    val oldValue: T = this[oldValueIndex]
    val newValue: T = update(oldValue)
    this[oldValueIndex] = newValue
    return newValue
}

fun <T> MutableList<T>.removeSingle(predicate: (T) -> Boolean): T {
    val oldValueIndex: Int = this.indexOfSingle(predicate)
    return this.removeAt(oldValueIndex)
}

fun <T, U> matchIdentity(getIdentity: (T) -> U): (T) -> ((T) -> Boolean) {
    return { subject: T ->
        { compare: T ->
            getIdentity(compare) == getIdentity(subject)
        }
    }
}
