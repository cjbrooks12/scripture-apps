package com.caseyjbrooks.prayer.schedules

import com.copperleaf.ballast.BallastLogger
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

public val prayerSchedulesScreenModule: Module = module {
    factoryOf(::PrayerSchedulesInputHandler)
    factoryOf(::PrayerSchedulesEventHandler)

    single<PrayerSchedulesViewModel>(named("PrayerSchedulesViewModel")) {
        val applicationCoroutineScope: CoroutineScope = get()
        BasicViewModel(
            coroutineScope = applicationCoroutineScope,
            config = BallastViewModelConfiguration.Builder()
                .withViewModel(
                    initialState = PrayerSchedulesContract.State(),
                    inputHandler = get<PrayerSchedulesInputHandler>(),
                    name = "Prayer Schedules",
                )
                .apply {
                    inputStrategy = FifoInputStrategy.typed()

                    logger = { tag -> get<BallastLogger> { parametersOf(tag) } }
                    this += LoggingInterceptor()
                }
                .dispatchers(
                    inputsDispatcher = Dispatchers.Main,
                    eventsDispatcher = Dispatchers.Main,
                    sideJobsDispatcher = Dispatchers.Default,
                    interceptorDispatcher = Dispatchers.Default,
                )
                .build(),
            eventHandler = get<PrayerSchedulesEventHandler>(),
        )
    }
}
