package com.caseyjbrooks.prayer.screens.timer

import com.caseyjbrooks.prayer.models.PrayerId
import com.copperleaf.ballast.BallastLogger
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.BootstrapInterceptor
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

public val prayerTimerScreenModule: Module = module {
    factoryOf(::PrayerTimerInputHandler)
    factoryOf(::PrayerTimerEventHandler)

    factory<PrayerTimerViewModel>(named("PrayerTimerViewModel")) { (viewModelCoroutineScope: CoroutineScope, prayerId: PrayerId) ->
        BasicViewModel(
            coroutineScope = viewModelCoroutineScope,
            config = BallastViewModelConfiguration.Builder()
                .withViewModel(
                    initialState = PrayerTimerContract.State(prayerId = prayerId),
                    inputHandler = get<PrayerTimerInputHandler>(),
                    name = "Prayer Timer",
                )
                .apply {
                    inputStrategy = FifoInputStrategy.typed()

                    logger = { tag -> get<BallastLogger> { parametersOf(tag) } }
                    this += LoggingInterceptor()

                    this += BootstrapInterceptor {
                        PrayerTimerContract.Inputs.ObservePrayer(prayerId)
                    }
                }
                .dispatchers(
                    inputsDispatcher = Dispatchers.Main,
                    eventsDispatcher = Dispatchers.Main,
                    sideJobsDispatcher = Dispatchers.Default,
                    interceptorDispatcher = Dispatchers.Default,
                )
                .build(),
            eventHandler = get<PrayerTimerEventHandler>(),
        )
    }
}
