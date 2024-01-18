package com.caseyjbrooks.prayer.screens.form

import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

internal class PrayerFormInputHandler(
    private val getByIdUseCase: GetPrayerByIdUseCase,
) : InputHandler<
        PrayerFormContract.Inputs,
        PrayerFormContract.Events,
        PrayerFormContract.State,
        > {
    override suspend fun InputHandlerScope<
            PrayerFormContract.Inputs,
            PrayerFormContract.Events,
            PrayerFormContract.State,
            >.handleInput(
        input: PrayerFormContract.Inputs,
    ): Unit = when (input) {
        is PrayerFormContract.Inputs.ObservePrayer -> {
            val currentState = updateStateAndGet { it.copy(prayerId = input.prayerId) }

            if (currentState.prayerId == null) {
                cancelSideJob("ObservePrayer")
            } else {
                observeFlows(
                    "ObservePrayer",
                    getByIdUseCase(currentState.prayerId)
                        .map { PrayerFormContract.Inputs.PrayerUpdated(it) },
                )
            }
        }

        is PrayerFormContract.Inputs.PrayerUpdated -> {
            updateState { it.copy(cachedPrayer = input.cachedPrayers) }
        }

        is PrayerFormContract.Inputs.NavigateUp -> {
            postEvent(
                PrayerFormContract.Events.NavigateTo(
                    PrayerFormRoute.Directions.list(),
                    replaceTop = true,
                ),
            )
        }

        is PrayerFormContract.Inputs.GoBack -> {
            postEvent(
                PrayerFormContract.Events.NavigateBack,
            )
        }
    }
}
