package com.caseyjbrooks.scripturenow.models.memory

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.caseyjbrooks.scripturenow.models.VerseReference
import com.caseyjbrooks.scripturenow.utils.UuidSerializer
import com.caseyjbrooks.scripturenow.utils.now
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
public data class MemoryVerse(
    val uuid: @Serializable(with = UuidSerializer::class) Uuid = uuid4(),
    val main: Boolean = false,
    val text: String,
    val reference: VerseReference,
    val version: String = "",
    val verseUrl: String = "",
    val notice: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
