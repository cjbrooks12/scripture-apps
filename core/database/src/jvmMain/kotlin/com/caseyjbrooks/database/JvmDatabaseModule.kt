package com.caseyjbrooks.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module

public val jvmDatabaseModule: Module = module {
    factory<SqlDriver> {
        JdbcSqliteDriver("jdbc:sqlite:scriptureNow.db")
    }
    single<Settings> {
        PreferencesSettings.Factory().create("scriptureNow")
    }
}
