package com.caseyjbrooks.prayer

import com.caseyjbrooks.prayer.ui.detail.PrayerDetailRoute
import com.caseyjbrooks.prayer.ui.form.PrayerFormRoute
import com.caseyjbrooks.prayer.ui.list.PrayerListRoute
import com.caseyjbrooks.prayer.ui.timer.PrayerTimerRoute
import com.caseyjbrooks.routing.ScriptureNowScreen

public object PrayerModule {
    public fun routes(): List<ScriptureNowScreen> = listOf(
        PrayerListRoute,
        PrayerDetailRoute,
        PrayerFormRoute,
        PrayerTimerRoute,
    )
}
