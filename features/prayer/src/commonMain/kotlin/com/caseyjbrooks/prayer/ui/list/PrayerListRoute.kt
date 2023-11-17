package com.caseyjbrooks.prayer.ui.list

import androidx.compose.runtime.Composable
import com.caseyjbrooks.prayer.models.PrayerRoute
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.ui.detail.PrayerDetailRoute
import com.caseyjbrooks.prayer.ui.form.PrayerFormRoute
import com.caseyjbrooks.prayer.ui.timer.PrayerTimerRoute
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter

public object PrayerListRoute : ScriptureNowScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/prayers")
    override val annotations: Set<RouteAnnotation> = setOf(PrayerRoute)

    public object Directions {
        public fun new(): String {
            return PrayerFormRoute
                .directions()
                .build()
        }

        public fun view(prayer: SavedPrayer): String {
            return PrayerDetailRoute
                .directions()
                .pathParameter("prayerId", prayer.uuid.uuid)
                .build()
        }

        public fun edit(prayer: SavedPrayer): String {
            return PrayerFormRoute
                .directions()
                .pathParameter("prayerId", prayer.uuid.uuid)
                .build()
        }

        public fun timer(prayer: SavedPrayer): String {
            return PrayerTimerRoute
                .directions()
                .pathParameter("prayerId", prayer.uuid.uuid)
                .build()
        }
    }

    @Composable
    override fun Content(destination: Destination.Match<ScriptureNowScreen>) {
        PrayerListUi.Content()
    }
}
