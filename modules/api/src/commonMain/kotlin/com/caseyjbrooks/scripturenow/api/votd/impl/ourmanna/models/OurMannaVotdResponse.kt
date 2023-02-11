package com.caseyjbrooks.scripturenow.api.votd.impl.ourmanna.models

import kotlinx.serialization.Serializable

@Serializable
public data class OurMannaVotdResponse(
    val verse: Verse = Verse(),
) {
    @Serializable
    public data class Verse(
        val details: Details = Details(),
        val notice: String = "",
    ) {
        @Serializable
        public data class Details(
            val text: String = "",
            val reference: String = "",
            val version: String = "",
            val verseurl: String = "",
        )
    }
}
