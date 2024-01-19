package com.caseyjbrooks.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.module.Module
import org.koin.dsl.module

public val androidDatabaseModule: Module = module {
    factory<SqlDriver> {
        AndroidSqliteDriver(ScriptureNowDatabase.Schema, get(), "scriptureNow.db")
    }
    single<Settings> {
        SharedPreferencesSettings.Factory(get<Context>().applicationContext).create("scriptureNow")
    }
    single<UuidFactory> { UuidFactoryImpl() }
}
