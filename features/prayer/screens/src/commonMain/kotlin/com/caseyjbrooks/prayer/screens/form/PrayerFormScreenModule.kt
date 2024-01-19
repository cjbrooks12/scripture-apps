package com.caseyjbrooks.prayer.screens.form

import com.caseyjbrooks.prayer.models.PrayerId
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

public val prayerFormScreenModule: Module = module {
    factoryOf(::PrayerFormInputHandler)
    factoryOf(::PrayerFormEventHandler)

    factory<PrayerFormViewModel>(named("PrayerFormViewModel")) { (viewModelCoroutineScope: CoroutineScope, prayerId: PrayerId?) ->
        BasicViewModel(
            coroutineScope = viewModelCoroutineScope,
            config = BallastViewModelConfiguration.Builder()
                .withViewModel(
                    initialState = PrayerFormContract.State(prayerId = prayerId),
                    inputHandler = get<PrayerFormInputHandler>(),
                    name = if (prayerId != null) "Edit Prayer $prayerId" else "Create Prayer",
                )
                .apply {
                    inputStrategy = FifoInputStrategy.typed()

                    logger = ::PrintlnLogger
                    this += LoggingInterceptor()

                    this += BootstrapInterceptor {
                        PrayerFormContract.Inputs.ObservePrayer(prayerId)
                    }
                }
                .dispatchers(
                    inputsDispatcher = Dispatchers.Main,
                    eventsDispatcher = Dispatchers.Main,
                    sideJobsDispatcher = Dispatchers.Default,
                    interceptorDispatcher = Dispatchers.Default,
                )
                .build(),
            eventHandler = get<PrayerFormEventHandler>(),
        )
    }
}
