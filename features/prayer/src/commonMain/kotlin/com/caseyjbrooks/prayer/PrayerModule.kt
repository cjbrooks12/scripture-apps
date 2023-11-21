package com.caseyjbrooks.prayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import com.caseyjbrooks.prayer.ui.detail.PrayerDetailRoute
import com.caseyjbrooks.prayer.ui.form.PrayerFormRoute
import com.caseyjbrooks.prayer.ui.list.PrayerListRoute
import com.caseyjbrooks.prayer.ui.timer.PrayerTimerRoute
import com.caseyjbrooks.routing.MainNavigationItem
import com.caseyjbrooks.routing.ScriptureNowScreen

public object PrayerModule {
    public fun routes(): List<ScriptureNowScreen> = listOf(
        PrayerListRoute,
        PrayerDetailRoute,
        PrayerFormRoute,
        PrayerTimerRoute,
    )

    public val mainNavigationItem: MainNavigationItem = MainNavigationItem(
        route = PrayerListRoute,
        iconFilled = Icons.Filled.ThumbUp,
        iconOutlined = Icons.Outlined.ThumbUp,
        label = "Prayer",
    )
}
