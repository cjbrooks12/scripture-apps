package com.copperleaf.scripturenow.verses

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.bus.EventBusImpl
import com.copperleaf.scripturenow.ScriptureNowDatabase
import com.copperleaf.scripturenow.VerseOfTheDay
import com.copperleaf.scripturenow.VerseOfTheDayQueries
import com.copperleaf.scripturenow.common.LocalDateAdapter
import com.copperleaf.scripturenow.common.LocalDateTimeAdapter
import com.copperleaf.scripturenow.verses.ourmanna.OurMannaApi
import com.copperleaf.scripturenow.verses.ourmanna.OurMannaApiConverter
import com.copperleaf.scripturenow.verses.ourmanna.OurMannaApiConverterImpl
import com.copperleaf.scripturenow.verses.ourmanna.OurMannaApiImpl
import com.copperleaf.scripturenow.verses.repository.VerseOfTheDayRepositoryImpl
import com.copperleaf.scripturenow.verses.repository.VerseOfTheDayRepositoryInputHandler
import com.copperleaf.scripturenow.verses.sql.VerseOfTheDayDbConverter
import com.copperleaf.scripturenow.verses.sql.VerseOfTheDayDbConverterImpl
import com.copperleaf.scripturenow.verses.sql.VerseOfTheDayDbImpl
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

const val OurMannaApiBaseUrlKey = "OurMannaApiBaseUrl"
const val RepositoryCoroutineScope = "RepositoryCoroutineScope"
const val RepositoryConfigBuilder = "RepositoryConfigBuilder"

val votdModule = DI.Module(name = "VOTD") {
    // API
    bind<OurMannaApi> {
        provider {
            val baseUrl: String = instance(OurMannaApiBaseUrlKey)
            val ktorfit: Ktorfit = instance(arg = baseUrl)
            ktorfit.create<OurMannaApi>()
        }
    }
    bind<OurMannaApiConverter> {
        provider { OurMannaApiConverterImpl() }
    }
    bind<VerseOfTheDayApi> {
        singleton { OurMannaApiImpl(instance(), instance()) }
    }

    // DB
    bind<ScriptureNowDatabase> {
        singleton {
            ScriptureNowDatabase(
                driver = instance(),
                verseOfTheDayAdapter = VerseOfTheDay.Adapter(
                    dateAdapter = LocalDateAdapter,
                    created_atAdapter = LocalDateTimeAdapter,
                    updated_atAdapter = LocalDateTimeAdapter,
                )
            )
        }
    }
    bind<VerseOfTheDayQueries> {
        provider {
            instance<ScriptureNowDatabase>().verseOfTheDayQueries
        }
    }
    bind<VerseOfTheDayDbConverter> {
        provider { VerseOfTheDayDbConverterImpl() }
    }
    bind<VerseOfTheDayDb> {
        singleton { VerseOfTheDayDbImpl(instance(), instance()) }
    }

    // Repository
    bind<CoroutineScope>(tag = RepositoryCoroutineScope) {
        singleton { CoroutineScope(Dispatchers.Default + SupervisorJob()) }
    }
    bind<EventBus>() {
        singleton { EventBusImpl() }
    }
    bind<BallastViewModelConfiguration.Builder>(tag = RepositoryConfigBuilder) {
        provider {
            BallastViewModelConfiguration.Builder()
                .apply {
                    this += LoggingInterceptor()
                    logger = { PrintlnLogger(it) }
                }
        }
    }
    bind<VerseOfTheDayRepositoryInputHandler>() {
        provider {
            VerseOfTheDayRepositoryInputHandler(
                eventBus = instance(),
                api = instance(),
                db = instance(),
            )
        }
    }
    bind<VerseOfTheDayRepository> {
        singleton {
            VerseOfTheDayRepositoryImpl(
                coroutineScope = instance(tag = RepositoryCoroutineScope),
                eventBus = instance(),
                inputHandler = instance(),
                configBuilder = instance(tag = RepositoryConfigBuilder),
            )
        }
    }
}
