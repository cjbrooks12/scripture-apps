package com.caseyjbrooks.scripturenow.utils

import kotlinx.datetime.*

public fun LocalDateTime.Companion.now(
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): LocalDateTime {
    return clock.now().toLocalDateTime(timeZone)
}

public fun LocalDate.Companion.now(
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): LocalDate {
    return clock.todayIn(timeZone)
}

public fun LocalDate.atStartOfDay(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): LocalDateTime {
    return this
        .atStartOfDayIn(timeZone)
        .toLocalDateTime(timeZone)
}
