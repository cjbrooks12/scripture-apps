package com.caseyjbrooks.scripturenow.api.verses.impl.abs.models

import kotlinx.serialization.Serializable

@Serializable
public data class AbsBiblesResponse(
    val data: List<Data> = listOf()
) {
    @Serializable
    public data class Data(
        val id: String = "",
        val name: String = "",
        val abbreviation: String = "",
        val language: Language = Language(),
    ) {
        @Serializable
        public data class Language(
            val id: String = "",
            val name: String = "",
            val nameLocal: String = "",
            val script: String = "",
            val scriptDirection: String = ""
        )
    }
}
