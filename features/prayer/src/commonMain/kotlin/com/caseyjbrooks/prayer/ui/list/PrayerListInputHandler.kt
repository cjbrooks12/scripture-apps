package com.caseyjbrooks.prayer.ui.list

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import kotlinx.coroutines.delay

internal class PrayerListInputHandler : InputHandler<
        PrayerListContract.Inputs,
        PrayerListContract.Events,
        PrayerListContract.State,
        > {
    override suspend fun InputHandlerScope<
            PrayerListContract.Inputs,
            PrayerListContract.Events,
            PrayerListContract.State,
            >.handleInput(
        input: PrayerListContract.Inputs,
    ): Unit = when (input) {
        is PrayerListContract.Inputs.Initialize -> {
            updateState { it.copy(loading = true) }
            delay(1000)
            updateState { it.copy(loading = false) }
        }

        is PrayerListContract.Inputs.GoBack -> {
            postEvent(PrayerListContract.Events.NavigateUp)
        }
    }
}
