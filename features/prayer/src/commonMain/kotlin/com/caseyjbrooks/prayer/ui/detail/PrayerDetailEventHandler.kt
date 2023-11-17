package com.caseyjbrooks.prayer.ui.detail

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class PrayerDetailEventHandler : EventHandler<
        PrayerDetailContract.Inputs,
        PrayerDetailContract.Events,
        PrayerDetailContract.State,> {
    override suspend fun EventHandlerScope<
            PrayerDetailContract.Inputs,
            PrayerDetailContract.Events,
            PrayerDetailContract.State,>.handleEvent(
        event: PrayerDetailContract.Events,
    ): Unit = when (event) {
        is PrayerDetailContract.Events.NavigateUp -> {
        }
    }
}
