package com.caseyjbrooks.prayer.ui.list

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class PrayerListEventHandler : EventHandler<
        PrayerListContract.Inputs,
        PrayerListContract.Events,
        PrayerListContract.State> {
    override suspend fun EventHandlerScope<
            PrayerListContract.Inputs,
            PrayerListContract.Events,
            PrayerListContract.State>.handleEvent(
        event: PrayerListContract.Events
    ): Unit = when (event) {
        is PrayerListContract.Events.NavigateUp -> {
        }
    }
}
