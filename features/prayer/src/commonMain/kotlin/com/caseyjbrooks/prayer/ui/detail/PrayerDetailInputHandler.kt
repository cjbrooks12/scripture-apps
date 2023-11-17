package com.caseyjbrooks.prayer.ui.detail

import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import kotlinx.coroutines.delay

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
        is PrayerDetailContract.Inputs.Initialize -> {
            updateState { it.copy(loading = true) }
            delay(1000)
            updateState { it.copy(loading = false) }
        }

        is PrayerDetailContract.Inputs.GoBack -> {
            postEvent(PrayerDetailContract.Events.NavigateUp)
        }
    }
}
