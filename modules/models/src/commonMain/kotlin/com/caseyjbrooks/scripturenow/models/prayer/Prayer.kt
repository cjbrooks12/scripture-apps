package com.caseyjbrooks.scripturenow.models.prayer

import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDateTime

public data class Prayer(
    val uuid: Uuid,
    val text: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
