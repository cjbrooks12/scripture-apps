package com.caseyjbrooks.app.di

import com.caseyjbrooks.scripturenow.ScriptureNowDatabase
import com.caseyjbrooks.scripturenow.api.HttpClientProvider
import com.caseyjbrooks.scripturenow.api.auth.Session
import com.caseyjbrooks.scripturenow.api.auth.SessionProvider
import com.caseyjbrooks.scripturenow.api.votd.VerseOfTheDayApi
import com.caseyjbrooks.scripturenow.api.votd.VerseOfTheDayApiProvider
import com.caseyjbrooks.scripturenow.config.LocalAppConfig
import com.caseyjbrooks.scripturenow.db.impl.sql.SqlDbProvider
import com.caseyjbrooks.scripturenow.db.memory.MemoryVerseDb
import com.caseyjbrooks.scripturenow.db.memory.MemoryVerseDbProvider
import com.caseyjbrooks.scripturenow.db.prayer.PrayerDb
import com.caseyjbrooks.scripturenow.db.votd.VerseOfTheDayDb
import com.caseyjbrooks.scripturenow.db.votd.VerseOfTheDayDbProvider
import com.caseyjbrooks.scripturenow.models.auth.SessionService
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.engine.okhttp.*

class DataSourcesInjectorImpl(
    private val appInjector: AppInjector,
) {
    public val localAppConfig = LocalAppConfig.get()

    private val sqlDriver: SqlDriver = AndroidSqliteDriver(
        ScriptureNowDatabase.Schema,
        appInjector.applicationContext,
        "scripture_now.db",
    )
    private val sqlDatabase = SqlDbProvider.getDatabase(
        driver = sqlDriver,
        config = localAppConfig,
    )
    private val httpClient = HttpClientProvider.get(OkHttp, localAppConfig)

    public fun getSession(): Session {
        return SessionProvider.get(SessionService.Firebase, localAppConfig, httpClient)
    }

    public fun getMemoryVerseDb(): MemoryVerseDb {
        return MemoryVerseDbProvider.get(sqlDatabase)
    }

    public fun getPrayerDb(): PrayerDb {
        TODO()
    }

    public fun getVerseOfTheDayApi(): VerseOfTheDayApi {
        return VerseOfTheDayApiProvider.get(
            service = VerseOfTheDayService.OurManna,
            config = localAppConfig,
            client = httpClient,
        )
    }

    public fun getVerseOfTheDayDb(): VerseOfTheDayDb {
        return VerseOfTheDayDbProvider.get(sqlDatabase)
    }
}
