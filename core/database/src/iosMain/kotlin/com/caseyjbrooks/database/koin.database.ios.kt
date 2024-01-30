package com.caseyjbrooks.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun getRealPlatformDatabaseModule(): Module = module {
    factory<SqlDriver> {
        NativeSqliteDriver(ScriptureNowDatabase.Schema, "scriptureNow.db").also {
            ScriptureNowDatabase.Schema.create(it)
        }
    }
    single<Settings> {
        NSUserDefaultsSettings.Factory().create("scriptureNow")
    }
}

internal actual fun getFakePlatformDatabaseModule(): Module = module {
    factory<SqlDriver> {
        NativeSqliteDriver(ScriptureNowDatabase.Schema, "scriptureNow.db").also {
            ScriptureNowDatabase.Schema.create(it)
        }
    }
    single<Settings> {
        NSUserDefaultsSettings.Factory().create("scriptureNow")
    }
}
