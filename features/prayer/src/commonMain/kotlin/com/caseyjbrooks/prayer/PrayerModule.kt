package com.caseyjbrooks.prayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import com.caseyjbrooks.di.FeatureModule
import com.caseyjbrooks.prayer.ui.detail.PrayerDetailRoute
import com.caseyjbrooks.prayer.ui.form.PrayerFormRoute
import com.caseyjbrooks.prayer.ui.list.PrayerListRoute
import com.caseyjbrooks.prayer.ui.timer.PrayerTimerRoute
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.ScriptureNowScreen

public class PrayerModule : FeatureModule {
    override val routes: List<ScriptureNowScreen> = listOf(
        PrayerListRoute,
        PrayerDetailRoute,
        PrayerFormRoute,
        PrayerTimerRoute,
    )
    override val initialRoute: ScriptureNowScreen = PrayerListRoute
    override val mainNavigationItems: List<NavigationItem> = listOf(
        NavigationItem(
            route = PrayerListRoute,
            iconFilled = Icons.Filled.ThumbUp,
            iconOutlined = Icons.Outlined.ThumbUp,
            label = "Prayers",
            order = 30,
        ),
    )
    override val secondaryNavigationItems: List<NavigationItem> = listOf()
}
