package com.caseyjbrooks.prayer.ui.timer

import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import kotlinx.coroutines.delay

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
        is PrayerTimerContract.Inputs.Initialize -> {
            updateState { it.copy(loading = true) }
            delay(1000)
            updateState { it.copy(loading = false) }
        }

        is PrayerTimerContract.Inputs.GoBack -> {
            postEvent(PrayerTimerContract.Events.NavigateUp)
        }
    }
}
