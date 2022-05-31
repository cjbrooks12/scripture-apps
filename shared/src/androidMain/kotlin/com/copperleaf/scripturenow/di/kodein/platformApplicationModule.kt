package com.copperleaf.scripturenow.di.kodein

import com.copperleaf.scripturenow.ScriptureNowDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

const val ApplicationContext = "ApplicationContext"

actual fun platformApplicationModule(): DI.Module = DI.Module(name = "Application >> Android") {
    bind<HttpClientEngineFactory<*>> {
        singleton { OkHttp }
    }
    bind<SqlDriver> {
        singleton {
            AndroidSqliteDriver(ScriptureNowDatabase.Schema, instance(tag = ApplicationContext), "scripture_now.db")
        }
    }

    bind<CoroutineDispatcher>(tag = MainDispatcher) {
        provider { Dispatchers.Main }
    }
    bind<CoroutineDispatcher>(tag = BackgroundDispatcher) {
        provider { Dispatchers.IO }
    }
}
