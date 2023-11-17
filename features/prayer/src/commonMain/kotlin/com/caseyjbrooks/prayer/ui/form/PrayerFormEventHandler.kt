package com.caseyjbrooks.prayer.ui.form

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class PrayerFormEventHandler : EventHandler<
        PrayerFormContract.Inputs,
        PrayerFormContract.Events,
        PrayerFormContract.State> {
    override suspend fun EventHandlerScope<
            PrayerFormContract.Inputs,
            PrayerFormContract.Events,
            PrayerFormContract.State>.handleEvent(
        event: PrayerFormContract.Events
    ): Unit = when (event) {
        is PrayerFormContract.Events.NavigateUp -> {

        }
    }
}
