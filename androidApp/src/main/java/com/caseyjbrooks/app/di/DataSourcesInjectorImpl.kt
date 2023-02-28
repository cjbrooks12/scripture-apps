package com.caseyjbrooks.app.di

import android.content.Context
import com.caseyjbrooks.scripturenow.ScriptureNowDatabase
import com.caseyjbrooks.scripturenow.api.HttpClientProvider
import com.caseyjbrooks.scripturenow.api.auth.Session
import com.caseyjbrooks.scripturenow.api.auth.SessionProvider
import com.caseyjbrooks.scripturenow.api.votd.VerseOfTheDayApi
import com.caseyjbrooks.scripturenow.api.votd.VerseOfTheDayApiProvider
import com.caseyjbrooks.scripturenow.config.*
import com.caseyjbrooks.scripturenow.db.impl.sql.SqlDbProvider
import com.caseyjbrooks.scripturenow.db.memory.MemoryVerseDb
import com.caseyjbrooks.scripturenow.db.memory.MemoryVerseDbProvider
import com.caseyjbrooks.scripturenow.db.prayer.PrayerDb
import com.caseyjbrooks.scripturenow.db.prayer.PrayerDbProvider
import com.caseyjbrooks.scripturenow.db.preferences.AppPreferencesProvider
import com.caseyjbrooks.scripturenow.db.preferences.ObservableSettingsAppPreferences
import com.caseyjbrooks.scripturenow.db.votd.VerseOfTheDayDb
import com.caseyjbrooks.scripturenow.db.votd.VerseOfTheDayDbProvider
import com.caseyjbrooks.scripturenow.models.auth.SessionService
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.russhwolf.settings.SharedPreferencesSettings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.ktor.client.engine.okhttp.*

class DataSourcesInjectorImpl(
    private val appInjector: AppInjector,
) : LocalAppConfigProvider,
    RemoteAppConfigProvider {

    private val sqlDriver: SqlDriver = AndroidSqliteDriver(
        ScriptureNowDatabase.Schema,
        appInjector.applicationContext,
        "scripture_now.db",
    )
    private val sqlDatabase = SqlDbProvider.getDatabase(
        driver = sqlDriver,
        config = getLocalAppConfig(),
    )
    private val httpClient = HttpClientProvider.get(OkHttp, getLocalAppConfig())
    private val appPreferences = AppPreferencesProvider.get(
        SharedPreferencesSettings(
            delegate = appInjector
                .applicationContext
                .getSharedPreferences(
                    "${appInjector.applicationContext.packageName}_preferences",
                    Context.MODE_PRIVATE,
                )
        )
    )

    public fun getSession(): Session {
        return SessionProvider.get(SessionService.Firebase, getLocalAppConfig(), httpClient)
    }

    public fun getAppPreferences(): ObservableSettingsAppPreferences {
        return appPreferences
    }

    override fun getRemoteConfig(localAppConfig: LocalAppConfig): ObservableRemoteConfig {
        return FirebaseObservableRemoteAppConfig()
    }

    public fun getMemoryVerseDb(): MemoryVerseDb {
        return MemoryVerseDbProvider.get(sqlDatabase)
    }

    public fun getPrayerDb(): PrayerDb {
        return PrayerDbProvider.get(sqlDatabase)
    }

    public fun getVerseOfTheDayApi(service: VerseOfTheDayService): VerseOfTheDayApi {
        return VerseOfTheDayApiProvider.get(
            service = service,
            config = getLocalAppConfig(),
            client = httpClient,
        )
    }

    public fun getVerseOfTheDayDb(): VerseOfTheDayDb {
        return VerseOfTheDayDbProvider.get(sqlDatabase)
    }
}
