package com.caseyjbrooks.scripturenow.models

import com.caseyjbrooks.scripturenow.utils.VerseReferenceSerializer
import kotlinx.serialization.Serializable

@Serializable(with = VerseReferenceSerializer::class)
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
