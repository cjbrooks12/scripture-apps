package com.caseyjbrooks.scripturenow.models

public sealed interface VerseReference {
    public data class KnownReference(
        val book: String,
        val chapter: Int,
        val verses: IntRange,
    ) : VerseReference

    public data class UnknownReference(
        val text: String
    ) : VerseReference

    public companion object
}
