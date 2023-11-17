package com.caseyjbrooks.prayer.ui.form

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import kotlinx.coroutines.delay

internal class PrayerFormInputHandler : InputHandler<
        PrayerFormContract.Inputs,
        PrayerFormContract.Events,
        PrayerFormContract.State> {
    override suspend fun InputHandlerScope<
            PrayerFormContract.Inputs,
            PrayerFormContract.Events,
            PrayerFormContract.State>.handleInput(
        input: PrayerFormContract.Inputs
    ): Unit = when (input) {
        is PrayerFormContract.Inputs.Initialize -> {
            updateState { it.copy(loading = true) }
            delay(1000)
            updateState { it.copy(loading = false) }
        }

        is PrayerFormContract.Inputs.GoBack -> {
            postEvent(PrayerFormContract.Events.NavigateUp)
        }
    }
}
