package com.caseyjbrooks.prayer.screens.list

import androidx.compose.runtime.Composable
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.screens.detail.PrayerDetailRoute
import com.caseyjbrooks.prayer.screens.form.PrayerFormRoute
import com.caseyjbrooks.prayer.screens.timer.PrayerTimerRoute
import com.caseyjbrooks.routing.ListPane
import com.caseyjbrooks.routing.ApplicationScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter

public object PrayerListRoute : ApplicationScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/prayers")
    override val annotations: Set<RouteAnnotation> = setOf(ListPane)

    public object Directions {
        public fun new(): String {
            return PrayerFormRoute
                .directions()
                .build()
        }

        public fun view(prayer: SavedPrayer): String {
            return PrayerDetailRoute
                .directions()
                .pathParameter("prayerId", prayer.uuid.uuid.toString())
                .build()
        }

        public fun edit(prayer: SavedPrayer): String {
            return PrayerFormRoute
                .directions()
                .pathParameter("prayerId", prayer.uuid.uuid.toString())
                .build()
        }

        public fun timer(prayer: SavedPrayer): String {
            return PrayerTimerRoute
                .directions()
                .pathParameter("prayerId", prayer.uuid.uuid.toString())
                .build()
        }
    }

    @Composable
    override fun Content(destination: Destination.Match<ApplicationScreen>) {
        PrayerListScreen.Content()
    }
}
