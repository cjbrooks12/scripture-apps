package com.copperleaf.scripturenow.di.kodein

import com.copperleaf.scripturenow.ScriptureNowDatabase
import com.copperleaf.scripturenow.Sn_verseOfTheDay
import com.copperleaf.scripturenow.Sn_verseOfTheDayQueries
import com.copperleaf.scripturenow.api.votd.VerseOfTheDayApi
import com.copperleaf.scripturenow.api.votd.ourmanna.OurMannaApi
import com.copperleaf.scripturenow.api.votd.ourmanna.OurMannaApiConverter
import com.copperleaf.scripturenow.api.votd.ourmanna.OurMannaApiConverterImpl
import com.copperleaf.scripturenow.api.votd.ourmanna.OurMannaApiImpl
import com.copperleaf.scripturenow.common.LocalDateAdapter
import com.copperleaf.scripturenow.common.LocalDateTimeAdapter
import com.copperleaf.scripturenow.db.votd.sql.VerseOfTheDaySqlDb
import com.copperleaf.scripturenow.db.votd.sql.VerseOfTheDaySqlDbConverter
import com.copperleaf.scripturenow.db.votd.sql.VerseOfTheDaySqlDbConverterImpl
import com.copperleaf.scripturenow.repositories.votd.VerseOfTheDayRepository
import com.copperleaf.scripturenow.repositories.votd.VerseOfTheDayRepositoryImpl
import com.copperleaf.scripturenow.repositories.votd.VerseOfTheDayRepositoryInputHandler
import com.copperleaf.scripturenow.ui.votd.VotdEventHandler
import com.copperleaf.scripturenow.ui.votd.VotdInputHandler
import com.copperleaf.scripturenow.ui.votd.VotdViewModel
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import io.github.copper_leaf.shared.BASE_URL_OURMANNA
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

fun votdApiModule() = DI.Module(name = "VOTD API") {
    bind<OurMannaApi> {
        provider {
            val ktorfit: Ktorfit = instance(arg = BASE_URL_OURMANNA)
            ktorfit.create<OurMannaApi>()
        }
    }
    bind<OurMannaApiConverter> {
        provider { OurMannaApiConverterImpl() }
    }
    bind<VerseOfTheDayApi> {
        singleton { OurMannaApiImpl(instance(), instance()) }
    }
}

fun votdDbModule() = DI.Module(name = "VOTD DB") {
    bind<ScriptureNowDatabase> {
        singleton {
            ScriptureNowDatabase(
                driver = instance(),
                sn_verseOfTheDayAdapter = Sn_verseOfTheDay.Adapter(
                    dateAdapter = LocalDateAdapter,
                    created_atAdapter = LocalDateTimeAdapter,
                    updated_atAdapter = LocalDateTimeAdapter,
                )
            )
        }
    }
    bind<Sn_verseOfTheDayQueries> {
        provider {
            instance<ScriptureNowDatabase>().sn_verseOfTheDayQueries
        }
    }
    bind<VerseOfTheDaySqlDbConverter> {
        provider { VerseOfTheDaySqlDbConverterImpl() }
    }
    bind<VerseOfTheDaySqlDb> {
        singleton { VerseOfTheDaySqlDb(instance(), instance()) }
    }
}

fun votdRepositoryModule() = DI.Module(name = "VOTD Repository") {
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

fun votdVmModule() = DI.Module(name = "VOTD VM") {
    bind<VotdViewModel> {
        factory { arg: CoroutineScope ->
            VotdViewModel(
                coroutineScope = arg,
                configBuilder = instance(tag = RepositoryConfigBuilder),
                inputHandler = VotdInputHandler(instance()),
                eventHandler = VotdEventHandler(instance()),
            )
        }
    }
}
