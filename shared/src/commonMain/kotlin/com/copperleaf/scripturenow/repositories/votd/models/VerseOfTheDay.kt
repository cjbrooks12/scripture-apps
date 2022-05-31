package com.copperleaf.scripturenow.repositories.votd.models

import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDate

data class VerseOfTheDay(
    val uuid: Uuid,
    val date: LocalDate,
    val text: String,
    val reference: String,
    val version: String,
    val verseUrl: String,
    val notice: String,
)
