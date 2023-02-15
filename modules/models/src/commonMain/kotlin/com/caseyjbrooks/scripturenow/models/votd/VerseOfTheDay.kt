package com.caseyjbrooks.scripturenow.models.votd

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.VerseReference
import com.caseyjbrooks.scripturenow.utils.UuidSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
public data class VerseOfTheDay(
    val uuid: @Serializable(with = UuidSerializer::class) Uuid,
    val providedBy: VerseOfTheDayService,
    val date: LocalDate,
    val text: String,
    val reference: VerseReference,
    val version: String,
    val verseUrl: String,
    val notice: String,
)
