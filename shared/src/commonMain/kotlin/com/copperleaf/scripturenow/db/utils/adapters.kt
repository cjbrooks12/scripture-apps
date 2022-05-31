package com.copperleaf.scripturenow.db.utils

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import com.squareup.sqldelight.ColumnAdapter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

object LocalDateAdapter : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String): LocalDate {
        return LocalDate.parse(databaseValue)
    }

    override fun encode(value: LocalDate): String {
        return value.toString()
    }
}

object LocalDateTimeAdapter : ColumnAdapter<LocalDateTime, String> {
    override fun decode(databaseValue: String): LocalDateTime {
        return LocalDateTime.parse(databaseValue)
    }

    override fun encode(value: LocalDateTime): String {
        return value.toString()
    }
}

object UuidAdapter : ColumnAdapter<Uuid, String> {
    override fun decode(databaseValue: String): Uuid {
        return uuidFrom(databaseValue)
    }

    override fun encode(value: Uuid): String {
        return value.toString()
    }
}
