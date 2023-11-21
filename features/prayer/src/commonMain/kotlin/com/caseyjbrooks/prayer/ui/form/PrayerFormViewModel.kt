package com.caseyjbrooks.prayer.ui.form

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

internal typealias PrayerFormViewModel = BallastViewModel<
        PrayerFormContract.Inputs,
        PrayerFormContract.Events,
        PrayerFormContract.State,
        >

internal fun PrayerFormViewModel(
    viewModelCoroutineScope: CoroutineScope,
    getByIdUseCase: GetPrayerByIdUseCase,
    router: Router<ScriptureNowScreen>,
    prayerId: PrayerId?,
): PrayerFormViewModel {
    return BasicViewModel(
        coroutineScope = viewModelCoroutineScope,
        config = BallastViewModelConfiguration.Builder()
            .withViewModel(
                initialState = PrayerFormContract.State(prayerId = prayerId),
                inputHandler = PrayerFormInputHandler(
                    getByIdUseCase,
                ),
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
        eventHandler = PrayerFormEventHandler(router),
    )
}
