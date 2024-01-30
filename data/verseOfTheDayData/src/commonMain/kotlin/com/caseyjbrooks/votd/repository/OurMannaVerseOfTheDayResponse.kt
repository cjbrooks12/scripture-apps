package com.caseyjbrooks.votd.repository

import kotlinx.serialization.Serializable

@Serializable
public data class OurMannaVerseOfTheDayResponse(
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
