package com.caseyjbrooks.prayer.screens.detail

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

internal typealias PrayerDetailViewModel = BallastViewModel<
        PrayerDetailContract.Inputs,
        PrayerDetailContract.Events,
        PrayerDetailContract.State,
        >

internal fun PrayerDetailViewModel(
    viewModelCoroutineScope: CoroutineScope,
    getByIdUseCase: GetPrayerByIdUseCase,
    router: Router<ScriptureNowScreen>,
    prayerId: PrayerId,
): PrayerDetailViewModel {
    return BasicViewModel(
        coroutineScope = viewModelCoroutineScope,
        config = BallastViewModelConfiguration.Builder()
            .withViewModel(
                initialState = com.caseyjbrooks.prayer.screens.detail.PrayerDetailContract.State(prayerId = prayerId),
                inputHandler = PrayerDetailInputHandler(getByIdUseCase),
                name = "Prayer Detail",
            )
            .apply {
                inputStrategy = FifoInputStrategy.typed()

                logger = ::PrintlnLogger
                this += LoggingInterceptor()

                this += BootstrapInterceptor {
                    PrayerDetailContract.Inputs.ObservePrayer(prayerId)
                }
            }
            .dispatchers(
                inputsDispatcher = Dispatchers.Main,
                eventsDispatcher = Dispatchers.Main,
                sideJobsDispatcher = Dispatchers.Default,
                interceptorDispatcher = Dispatchers.Default,
            )
            .build(),
        eventHandler = PrayerDetailEventHandler(router),
    )
}
