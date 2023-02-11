package com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso.models

import kotlinx.serialization.Serializable

@Serializable
public data class TheySaidSoBibleVerseResponse(
    val contents: Contents = Contents(),
    val success: Success = Success()
) {
    @Serializable
    public data class Contents(
        val book: String = "",
        val bookid: String = "",
        val chapter: Int = 0,
        val number: Int = 0,
        val testament: String = "",
        val uuid: String? = null,
        val verse: String = ""
    )

    @Serializable
    public data class Success(
        val total: Int = 0
    )
}
