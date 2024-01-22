package com.caseyjbrooks.prayer.schedules

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class PrayerSchedulesEventHandler : EventHandler<
        PrayerSchedulesContract.Inputs,
        PrayerSchedulesContract.Events,
        PrayerSchedulesContract.State> {
    override suspend fun EventHandlerScope<
            PrayerSchedulesContract.Inputs,
            PrayerSchedulesContract.Events,
            PrayerSchedulesContract.State>.handleEvent(
        event: PrayerSchedulesContract.Events
    ) {
    }
}
