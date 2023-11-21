package com.caseyjbrooks.shared

import com.caseyjbrooks.prayer.PrayerModule
import com.caseyjbrooks.prayer.ui.list.PrayerListRoute
import com.caseyjbrooks.routing.MainNavigationItem
import com.caseyjbrooks.routing.ScriptureNowScreen

public object MainAppModule {
    public val allRoutes: List<ScriptureNowScreen> = listOf(
        PrayerModule.routes(),
    ).flatten()

    public val initialRoute: ScriptureNowScreen = PrayerListRoute

    public val mainNavigationItems: List<MainNavigationItem> = listOf(
        PrayerModule.mainNavigationItem,
    )
}
