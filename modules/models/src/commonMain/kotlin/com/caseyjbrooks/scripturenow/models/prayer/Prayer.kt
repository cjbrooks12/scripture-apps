package com.caseyjbrooks.scripturenow.models.prayer

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.caseyjbrooks.scripturenow.utils.UuidSerializer
import com.caseyjbrooks.scripturenow.utils.now
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
public data class Prayer(
    val uuid: @Serializable(with = UuidSerializer::class) Uuid = uuid4(),
    val prayerDescription: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
