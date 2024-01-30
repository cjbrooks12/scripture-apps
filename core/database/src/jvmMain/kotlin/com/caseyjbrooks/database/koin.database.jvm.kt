package com.caseyjbrooks.database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File
import java.util.*

internal actual fun getRealPlatformDatabaseModule(): Module = module {
    single<SqlDriver> {

        val dbFilePath = System.getProperty("user.home") + File.separator + ".scriptureNow/scriptureNow.db"

        val dbFile = File(dbFilePath).absoluteFile.canonicalFile

        val dbExists = dbFile.exists()

        if (!dbExists) {
            dbFile.parentFile.mkdirs()
            dbFile.createNewFile()
        }

        val connectionString = "jdbc:sqlite:${dbFile.canonicalPath}"
        println("Opening DB: $connectionString")

        JdbcSqliteDriver(
            url = connectionString,
            properties = Properties().apply { put("foreign_keys", "true") },
            schema = ScriptureNowDatabase.Schema.synchronous(),
            callbacks = emptyArray()
        )
    }
    single<Settings> {
        PreferencesSettings.Factory().create("scriptureNow")
    }
}

internal actual fun getFakePlatformDatabaseModule(): Module = module {
    single<SqlDriver> {
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
