package com.caseyjbrooks.prayer.ui.timer

import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCase
import com.caseyjbrooks.prayer.ui.detail.PrayerDetailRoute
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

internal class PrayerTimerInputHandler(
    private val getByIdUseCase: GetPrayerByIdUseCase,
) : InputHandler<
        PrayerTimerContract.Inputs,
        PrayerTimerContract.Events,
        PrayerTimerContract.State,
        > {
    override suspend fun InputHandlerScope<
            PrayerTimerContract.Inputs,
            PrayerTimerContract.Events,
            PrayerTimerContract.State,
            >.handleInput(
        input: PrayerTimerContract.Inputs,
    ): Unit = when (input) {
        is PrayerTimerContract.Inputs.ObservePrayer -> {
            val currentState = updateStateAndGet { it.copy(prayerId = input.prayerId) }
            observeFlows(
                "ObservePrayer",
                getByIdUseCase(currentState.prayerId)
                    .map { PrayerTimerContract.Inputs.PrayerUpdated(it) },
            )
        }

        is PrayerTimerContract.Inputs.PrayerUpdated -> {
            updateState { it.copy(cachedPrayer = input.cachedPrayers) }
        }

        is PrayerTimerContract.Inputs.NavigateUp -> {
            postEvent(
                PrayerTimerContract.Events.NavigateTo(
                    PrayerDetailRoute.Directions.list(),
                    replaceTop = true,
                ),
            )
        }

        is PrayerTimerContract.Inputs.GoBack -> {
            postEvent(
                PrayerTimerContract.Events.NavigateBack,
            )
        }
    }
}
