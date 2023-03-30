package com.caseyjbrooks.scripturenow.db.impl.sql

import co.touchlab.kermit.Logger
import com.caseyjbrooks.scripturenow.ScriptureNowDatabase
import com.caseyjbrooks.scripturenow.config.local.LocalAppConfig
import com.caseyjbrooks.scripturenow.db.LocalDateAdapter
import com.caseyjbrooks.scripturenow.db.LocalDateTimeAdapter
import com.caseyjbrooks.scripturenow.db.UuidAdapter
import com.copperleaf.scripturenow.Sn_memoryVerse
import com.copperleaf.scripturenow.Sn_prayer
import com.copperleaf.scripturenow.Sn_verseOfTheDay
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.logs.LogSqliteDriver

public object SqlDbProvider {
    public fun getDatabase(
        driver: SqlDriver,
        config: LocalAppConfig,
    ): ScriptureNowDatabase {
        val realDriver = if (config.logDbQueries) {
            val logger = Logger.withTag("${config.logPrefix} - SQL")
            LogSqliteDriver(driver) { logger.v(it) }
        } else {
            driver
        }

        return ScriptureNowDatabase(
            realDriver,
            sn_memoryVerseAdapter = Sn_memoryVerse.Adapter(
                uuidAdapter = UuidAdapter,
                created_atAdapter = LocalDateTimeAdapter,
                updated_atAdapter = LocalDateTimeAdapter,
            ),
            sn_prayerAdapter = Sn_prayer.Adapter(
                uuidAdapter = UuidAdapter,
                created_atAdapter = LocalDateTimeAdapter,
                updated_atAdapter = LocalDateTimeAdapter,
            ),
            sn_verseOfTheDayAdapter = Sn_verseOfTheDay.Adapter(
                uuidAdapter = UuidAdapter,
                created_atAdapter = LocalDateTimeAdapter,
                updated_atAdapter = LocalDateTimeAdapter,
                providedByAdapter = EnumColumnAdapter(),
                dateAdapter = LocalDateAdapter,
            ),
        )
    }
}
