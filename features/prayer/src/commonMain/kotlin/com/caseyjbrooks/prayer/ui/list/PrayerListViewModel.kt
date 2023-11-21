package com.caseyjbrooks.prayer.ui.list

import com.caseyjbrooks.prayer.domain.query.QueryPrayersUseCase
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

internal typealias PrayerListViewModel = BallastViewModel<
        PrayerListContract.Inputs,
        PrayerListContract.Events,
        PrayerListContract.State,
        >

internal fun PrayerListViewModel(
    viewModelCoroutineScope: CoroutineScope,
    queryPrayersUseCase: QueryPrayersUseCase,
    router: Router<ScriptureNowScreen>,
): PrayerListViewModel {
    return BasicViewModel(
        coroutineScope = viewModelCoroutineScope,
        config = BallastViewModelConfiguration.Builder()
            .withViewModel(
                initialState = PrayerListContract.State(),
                inputHandler = PrayerListInputHandler(
                    queryPrayersUseCase,
                ),
                name = "Prayer List",
            )
            .apply {
                inputStrategy = FifoInputStrategy.typed()

                logger = ::PrintlnLogger
                this += LoggingInterceptor()

                this += BootstrapInterceptor {
                    PrayerListContract.Inputs.ObservePrayerList
                }
            }
            .dispatchers(
                inputsDispatcher = Dispatchers.Main,
                eventsDispatcher = Dispatchers.Main,
                sideJobsDispatcher = Dispatchers.Default,
                interceptorDispatcher = Dispatchers.Default,
            )
            .build(),
        eventHandler = PrayerListEventHandler(router),
    )
}
