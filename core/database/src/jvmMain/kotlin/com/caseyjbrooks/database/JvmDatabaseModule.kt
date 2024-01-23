package com.caseyjbrooks.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver.Companion.IN_MEMORY
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.*

public val jvmDatabaseModule: Module = module {
    single<SqlDriver> {
        JdbcSqliteDriver(
            IN_MEMORY,
            properties = Properties().apply { put("foreign_keys", "true") }
        ).also {
            ScriptureNowDatabase.Schema.create(it)
        }
    }
    single<Settings> {
        PreferencesSettings.Factory().create("scriptureNow")
    }
}
