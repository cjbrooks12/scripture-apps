package com.caseyjbrooks.database

import app.cash.sqldelight.db.SqlDriver
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import kotlinx.browser.localStorage
import org.koin.core.module.Module
import org.koin.dsl.module
import org.w3c.dom.Worker

internal actual fun getRealPlatformDatabaseModule(): Module = module {
    single<SqlDriver> {
        WebWorkerDriver(
            worker = Worker(
                js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
            ),
            schema = ScriptureNowDatabase.Schema,
            callbacks = emptyArray(),
        )
    }
    single<Settings> {
        StorageSettings(localStorage)
    }
}

internal actual fun getFakePlatformDatabaseModule(): Module = module {
    single<SqlDriver> {
        WebWorkerDriver(
            worker = Worker(
                js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
            ),
            schema = ScriptureNowDatabase.Schema,
            callbacks = emptyArray(),
        )
    }
    single<Settings> {
        MapSettings()
    }
}
