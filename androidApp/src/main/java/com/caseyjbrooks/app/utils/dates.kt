package com.caseyjbrooks.app.utils

import java.io.Serializable
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

var UI_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
var API_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

var YEAR_MONTH_UI_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
var YEAR_MONTH_API_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
var YEAR_MONTH_CC_EXPIRATION_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/yyyy")

/**
 * A bug in Jetpack Navigation NavArgs crashes when LocalDate is used as an argument, on devices
 * where it is not available natively and must be desugared (< 26). By wrapping the LocalDate in
 * another class, NavArgs isn't passing a desugared class, so it seems to work.
 *
 * See this ticket for more context: https://issuetracker.google.com/u/0/issues/160257645
 */
class LocalDateNavArg(
    val wrapped: LocalDate
) : Serializable

fun String.toLocalDate(format: DateTimeFormatter): LocalDate {
    return LocalDate.parse(this, format)
}

fun String.toYearMonth(format: DateTimeFormatter): YearMonth {
    return YearMonth.parse(this, format)
}

fun String.toLocalDateSafe(format: DateTimeFormatter): LocalDate? {
    return try {
        LocalDate.parse(this, format)
    } catch (e: Exception) {
        null
    }
}

fun LocalDate.toYearMonth(): YearMonth {
    return YearMonth.of(this.year, this.month)
}

fun LocalDate.atBeginningOfMonth(): LocalDate {
    return this.withDayOfMonth(1)
}

fun LocalDate.atEndOfMonth(): LocalDate {
    return this.withDayOfMonth(this.lengthOfMonth())
}
