package com.caseyjbrooks.prayer.screens.form

import com.caseyjbrooks.prayer.domain.createFromText.CreatePrayerFromTextUseCase
import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCase
import com.caseyjbrooks.prayer.domain.update.UpdatePrayerUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import kotlinx.coroutines.flow.map

internal class PrayerFormInputHandler(
    private val getByIdUseCase: GetPrayerByIdUseCase,
    private val createPrayerUseCase: CreatePrayerFromTextUseCase,
    private val updatePrayerUseCase: UpdatePrayerUseCase,
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
                updateState { it.copy(cachedPrayer = Cached.FetchingFailed(NullPointerException(), null)) }
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

        is PrayerFormContract.Inputs.CreatePrayer -> {
            val newPrayer = createPrayerUseCase(input.text)
            postEvent(
                PrayerFormContract.Events.NavigateTo(
                    PrayerFormRoute.Directions.details(newPrayer),
                    replaceTop = true,
                ),
            )
        }

        is PrayerFormContract.Inputs.UpdatePrayer -> {
            val currentPrayer = getCurrentState().cachedPrayer.getCachedOrThrow()
            val updatedPrayer = currentPrayer.copy(text = input.text)
            val newPrayer = updatePrayerUseCase(updatedPrayer)
            postEvent(
                PrayerFormContract.Events.NavigateTo(
                    PrayerFormRoute.Directions.details(newPrayer),
                    replaceTop = true,
                ),
            )
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
