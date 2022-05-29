package com.copperleaf.scripturenow.api.votd.ourmanna

import kotlinx.serialization.Serializable

@Serializable
data class OurMannaVotdResponse(
    val verse: Verse = Verse(),
) {
    @Serializable
    data class Verse(
        val details: Details = Details(),
        val notice: String = "",
    ) {
        @Serializable
        data class Details(
            val text: String = "",
            val reference: String = "",
            val version: String = "",
            val verseurl: String = "",
        )
    }
}
