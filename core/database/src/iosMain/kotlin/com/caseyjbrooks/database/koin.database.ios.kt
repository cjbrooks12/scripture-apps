package com.caseyjbrooks.database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.cash.sqldelight.driver.native.wrapConnection
import co.touchlab.sqliter.DatabaseConfiguration
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun getRealPlatformDatabaseModule(): Module = module {
    factory<SqlDriver> {
        NativeSqliteDriver(
            schema = ScriptureNowDatabase.Schema.synchronous(),
            name = "scriptureNow.db",
            onConfiguration = { config ->
                config.copy(
                    extendedConfig = DatabaseConfiguration.Extended(
                        foreignKeyConstraints = true,
                    )
                )
            }
        ).also {
            ScriptureNowDatabase.Schema.create(it)
        }
    }
    single<Settings> {
        NSUserDefaultsSettings.Factory().create("scriptureNow")
    }
}

internal actual fun getFakePlatformDatabaseModule(): Module = module {
    factory<SqlDriver> {
        val schema = ScriptureNowDatabase.Schema.synchronous()
        NativeSqliteDriver(
            DatabaseConfiguration(
                name = null,
                inMemory = true,
                version = if (schema.version > Int.MAX_VALUE) error("Schema version is larger than Int.MAX_VALUE: ${schema.version}.") else schema.version.toInt(),
                create = { connection ->
                    wrapConnection(connection) { schema.create(it) }
                },
                upgrade = { connection, oldVersion, newVersion ->
                    wrapConnection(connection) { schema.migrate(it, oldVersion.toLong(), newVersion.toLong()) }
                },
                extendedConfig = DatabaseConfiguration.Extended(
                    foreignKeyConstraints = true,
                )
            )
        )
    }
    single<Settings> {
        NSUserDefaultsSettings.Factory().create("scriptureNow")
    }
}
