package com.caseyjbrooks.scripturenow.api.verses.impl.abs.models

import kotlinx.serialization.Serializable

@Serializable
public data class AbsVerseContentResponse(
    val data: Data = Data(),
    val meta: Meta = Meta()
) {
    @Serializable
    public data class Data(
        val id: String = "",
        val bibleId: String = "",
        val bookId: String = "",
        val chapterId: String = "",
        val reference: String = "",
        val content: String = "",
        val copyright: String = "",
    )

    @Serializable
    public data class Meta(
        val fums: String = "",
        val fumsId: String = "",
        val fumsJs: String = "",
        val fumsJsInclude: String = "",
        val fumsNoScript: String = ""
    )
}
