package com.copperleaf.scripturenow.db

import com.copperleaf.scripturenow.ScriptureNowDatabase
import com.copperleaf.scripturenow.Sn_memoryVerse
import com.copperleaf.scripturenow.Sn_verseOfTheDay
import com.copperleaf.scripturenow.db.utils.LocalDateAdapter
import com.copperleaf.scripturenow.db.utils.LocalDateTimeAdapter
import com.copperleaf.scripturenow.db.utils.UuidAdapter
import com.squareup.sqldelight.db.SqlDriver
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun mainDbModule() = DI.Module(name = "DB >> Main") {
    bind<ScriptureNowDatabase> {
        singleton {
            ScriptureNowDatabase(
                driver = instance<SqlDriver>(),
                sn_verseOfTheDayAdapter = Sn_verseOfTheDay.Adapter(
                    uuidAdapter = UuidAdapter,
                    dateAdapter = LocalDateAdapter,
                    created_atAdapter = LocalDateTimeAdapter,
                    updated_atAdapter = LocalDateTimeAdapter,
                ),
                sn_memoryVerseAdapter = Sn_memoryVerse.Adapter(
                    uuidAdapter = UuidAdapter,
                    created_atAdapter = LocalDateTimeAdapter,
                    updated_atAdapter = LocalDateTimeAdapter,
                )
            )
        }
    }
}
