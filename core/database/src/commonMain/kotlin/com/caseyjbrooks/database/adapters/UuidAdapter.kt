package com.caseyjbrooks.database.adapters

import app.cash.sqldelight.ColumnAdapter
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom

internal object UuidAdapter : ColumnAdapter<Uuid, String> {
    override fun decode(databaseValue: String): Uuid {
        return uuidFrom(databaseValue)
    }

    override fun encode(value: Uuid): String {
        return value.toString()
    }
}
