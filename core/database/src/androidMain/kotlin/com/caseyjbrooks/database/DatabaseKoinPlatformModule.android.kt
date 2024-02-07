package com.caseyjbrooks.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.caseyjbrooks.di.KoinModule
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.module.Module
import org.koin.dsl.module

public actual class DatabaseKoinPlatformModule : KoinModule {
    override fun mainModule(): Module = module {
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
}
