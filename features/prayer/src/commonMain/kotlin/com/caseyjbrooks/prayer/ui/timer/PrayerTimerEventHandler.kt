package com.caseyjbrooks.prayer.ui.timer

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class PrayerTimerEventHandler : EventHandler<
        PrayerTimerContract.Inputs,
        PrayerTimerContract.Events,
        PrayerTimerContract.State> {
    override suspend fun EventHandlerScope<
            PrayerTimerContract.Inputs,
            PrayerTimerContract.Events,
            PrayerTimerContract.State>.handleEvent(
        event: PrayerTimerContract.Events
    ): Unit = when (event) {
        is PrayerTimerContract.Events.NavigateUp -> {
        }
    }
}
