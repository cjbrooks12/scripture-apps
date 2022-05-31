package com.copperleaf.scripturenow.common

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayAt

public fun LocalDateTime.Companion.now(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}

public fun LocalDate.Companion.now(): LocalDate {
    return Clock.System.todayAt(TimeZone.currentSystemDefault())
}

public fun LocalDate.atStartOfDay(): LocalDateTime {
    return this
        .atStartOfDayIn(TimeZone.currentSystemDefault())
        .toLocalDateTime(TimeZone.currentSystemDefault())
}
