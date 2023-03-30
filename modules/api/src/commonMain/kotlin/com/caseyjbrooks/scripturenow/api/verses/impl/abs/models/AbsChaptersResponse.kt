package com.caseyjbrooks.scripturenow.api.verses.impl.abs.models

import kotlinx.serialization.Serializable

@Serializable
public data class AbsChaptersResponse(
    val data: List<Data> = listOf()
) {
    @Serializable
    public data class Data(
        val id: String = "",
        val bibleId: String = "",
        val bookId: String = "",
        val number: String = "",
        val reference: String = ""
    )
}
