package com.caseyjbrooks.scripturenow.models.memory

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.VerseReference
import kotlinx.datetime.LocalDateTime

public data class MemoryVerse(
    val uuid: Uuid,
    val main: Boolean,
    val text: String,
    val reference: VerseReference,
    val version: String,
    val verseUrl: String,
    val notice: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
