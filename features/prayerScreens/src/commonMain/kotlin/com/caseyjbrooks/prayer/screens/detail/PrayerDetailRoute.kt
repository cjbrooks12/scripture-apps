package com.caseyjbrooks.prayer.screens.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.benasher44.uuid.uuidFrom
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.screens.form.PrayerFormRoute
import com.caseyjbrooks.prayer.screens.list.PrayerListRoute
import com.caseyjbrooks.prayer.screens.timer.PrayerTimerRoute
import com.caseyjbrooks.routing.DetailPane
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.copperleaf.ballast.navigation.routing.stringPath

public object PrayerDetailRoute : ScriptureNowScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/prayer/view/{prayerId}")
    override val annotations: Set<RouteAnnotation> = setOf(DetailPane)

    public object Directions {
        public fun list(): String {
            return PrayerListRoute
                .directions()
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
    override fun Content(destination: Destination.Match<ScriptureNowScreen>) {
        val prayerId: String by destination.stringPath()

        key(prayerId) {
            PrayerDetailScreen.Content(PrayerId(uuidFrom(prayerId)))
        }
    }
}
