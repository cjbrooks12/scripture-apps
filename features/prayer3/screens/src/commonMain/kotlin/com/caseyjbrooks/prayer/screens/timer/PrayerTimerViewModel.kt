package com.caseyjbrooks.prayer.screens.timer

import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCase
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.copperleaf.ballast.BallastViewModel
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.navigation.vm.Router
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

internal typealias PrayerTimerViewModel = BallastViewModel<
        PrayerTimerContract.Inputs,
        PrayerTimerContract.Events,
        PrayerTimerContract.State,
        >

internal fun PrayerTimerViewModel(
    viewModelCoroutineScope: CoroutineScope,
    getByIdUseCase: GetPrayerByIdUseCase,
    router: Router<ScriptureNowScreen>,
    prayerId: PrayerId,
): PrayerTimerViewModel {
    return BasicViewModel(
        coroutineScope = viewModelCoroutineScope,
        config = BallastViewModelConfiguration.Builder()
            .withViewModel(
                initialState = PrayerTimerContract.State(prayerId = prayerId),
                inputHandler = PrayerTimerInputHandler(
                    getByIdUseCase,
                ),
                name = "Prayer Timer",
            )
            .apply {
                inputStrategy = FifoInputStrategy.typed()

                logger = ::PrintlnLogger
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
        eventHandler = PrayerTimerEventHandler(router),
    )
}
