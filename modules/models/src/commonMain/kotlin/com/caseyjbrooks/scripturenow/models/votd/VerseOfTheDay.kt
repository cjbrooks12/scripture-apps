package com.caseyjbrooks.scripturenow.models.votd

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.VerseReference
import kotlinx.datetime.LocalDate

public data class VerseOfTheDay(
    val uuid: Uuid,
    val providedBy: VerseOfTheDayService,
    val date: LocalDate,
    val text: String,
    val reference: VerseReference,
    val version: String,
    val verseUrl: String,
    val notice: String,
)
