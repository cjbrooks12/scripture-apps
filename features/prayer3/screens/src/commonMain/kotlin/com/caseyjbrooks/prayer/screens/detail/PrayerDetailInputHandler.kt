package com.caseyjbrooks.prayer.screens.detail

import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import kotlinx.coroutines.flow.map

internal class PrayerDetailInputHandler(
    private val getByIdUseCase: GetPrayerByIdUseCase,
) : InputHandler<
        PrayerDetailContract.Inputs,
        PrayerDetailContract.Events,
        PrayerDetailContract.State,
        > {
    override suspend fun InputHandlerScope<
            PrayerDetailContract.Inputs,
            PrayerDetailContract.Events,
            PrayerDetailContract.State,
            >.handleInput(
        input: PrayerDetailContract.Inputs,
    ): Unit = when (input) {
        is PrayerDetailContract.Inputs.ObservePrayer -> {
            val currentState = updateStateAndGet { it.copy(prayerId = input.prayerId) }
            observeFlows(
                "ObservePrayer",
                getByIdUseCase(currentState.prayerId)
                    .map { PrayerDetailContract.Inputs.PrayerUpdated(it) },
            )
        }

        is PrayerDetailContract.Inputs.PrayerUpdated -> {
            updateState { it.copy(cachedPrayer = input.cachedPrayers) }
        }

        is PrayerDetailContract.Inputs.NavigateUp -> {
            postEvent(
                PrayerDetailContract.Events.NavigateTo(
                    PrayerDetailRoute.Directions.list(),
                    replaceTop = true,
                ),
            )
        }

        is PrayerDetailContract.Inputs.GoBack -> {
            postEvent(
                PrayerDetailContract.Events.NavigateBack,
            )
        }

        is PrayerDetailContract.Inputs.Edit -> {
            val currentPrayer = getCurrentState().cachedPrayer.getCachedOrNull()

            if (currentPrayer != null) {
                postEvent(
                    PrayerDetailContract.Events.NavigateTo(
                        PrayerDetailRoute.Directions.edit(currentPrayer),
                        replaceTop = false,
                    ),
                )
            } else {
                noOp()
            }
        }

        is PrayerDetailContract.Inputs.PrayNow -> {
            val currentPrayer = getCurrentState().cachedPrayer.getCachedOrNull()

            if (currentPrayer != null) {
                postEvent(
                    PrayerDetailContract.Events.NavigateTo(
                        PrayerDetailRoute.Directions.timer(currentPrayer),
                        replaceTop = false,
                    ),
                )
            } else {
                noOp()
            }
        }
    }
}
