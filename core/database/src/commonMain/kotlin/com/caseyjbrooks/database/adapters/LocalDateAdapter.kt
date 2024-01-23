package com.caseyjbrooks.database.adapters

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.LocalDate

internal object LocalDateAdapter : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String): LocalDate {
        return LocalDate.parse(databaseValue)
    }

    override fun encode(value: LocalDate): String {
        return value.toString()
    }
}
