package com.caseyjbrooks.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.*

internal actual fun getRealPlatformDatabaseModule(): Module = module {
    factory<SqlDriver> {
        AndroidSqliteDriver(
            schema = ScriptureNowDatabase.Schema.synchronous(),
            context = get(),
            name = "scriptureNow.db",
            callback = object : AndroidSqliteDriver.Callback(ScriptureNowDatabase.Schema.synchronous()) {
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
        JdbcSqliteDriver(
            JdbcSqliteDriver.IN_MEMORY,
            properties = Properties().apply { put("foreign_keys", "true") },
            schema = ScriptureNowDatabase.Schema.synchronous(),
            callbacks = emptyArray()
        )
    }
    single<Settings> {
        MapSettings()
    }
}
