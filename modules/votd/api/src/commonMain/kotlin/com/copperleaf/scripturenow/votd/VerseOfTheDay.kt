package com.copperleaf.scripturenow.votd

import com.copperleaf.scripturenow.common.now
import kotlinx.datetime.LocalDate

data class VerseOfTheDay(
    val date: LocalDate = LocalDate.now(),
    val text: String = "",
    val reference: String = "",
    val version: String = "",
    val verseUrl: String = "",
    val notice: String = "",
)
