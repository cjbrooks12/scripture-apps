package com.caseyjbrooks.scripturenow.api.verses.impl.abs.models

import kotlinx.serialization.Serializable

@Serializable
public data class AbsBooksResponse(
    val data: List<Data> = listOf()
) {
    @Serializable
    public data class Data(
        val id: String = "",
        val bibleId: String = "",
        val abbreviation: String = "",
        val name: String = "",
    )
}
