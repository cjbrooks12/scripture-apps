package com.caseyjbrooks.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun getRealPlatformDatabaseModule(): Module = module {
    factory<SqlDriver> {
        AndroidSqliteDriver(
            schema = ScriptureNowDatabase.Schema,
            context = get(),
            name = "scriptureNow.db",
            callback = object : AndroidSqliteDriver.Callback(ScriptureNowDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.setForeignKeyConstraintsEnabled(true)
                }
            }
        )
    }
    single<Settings> {
        SharedPreferencesSettings.Factory(get<Context>().applicationContext).create("scriptureNow")
    }
}

internal actual fun getFakePlatformDatabaseModule(): Module = module {
    factory<SqlDriver> {
        AndroidSqliteDriver(
            schema = ScriptureNowDatabase.Schema,
            context = get(),
            name = "scriptureNow.db",
            callback = object : AndroidSqliteDriver.Callback(ScriptureNowDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.setForeignKeyConstraintsEnabled(true)
                }
            }
        )
    }
    single<Settings> {
        SharedPreferencesSettings.Factory(get<Context>().applicationContext).create("scriptureNow")
    }
}
