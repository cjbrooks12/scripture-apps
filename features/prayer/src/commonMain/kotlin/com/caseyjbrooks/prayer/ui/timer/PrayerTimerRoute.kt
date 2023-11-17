package com.caseyjbrooks.prayer.ui.timer

import androidx.compose.runtime.Composable
import com.caseyjbrooks.prayer.models.PrayerRoute
import com.caseyjbrooks.prayer.ui.list.PrayerListRoute
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.stringPath

public object PrayerTimerRoute : ScriptureNowScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/prayer/{prayerId}/now")
    override val annotations: Set<RouteAnnotation> = setOf(PrayerRoute)

    public object Directions {
        public fun list(): String {
            return PrayerListRoute
                .directions()
                .build()
        }
    }

    @Composable
    override fun Content(destination: Destination.Match<ScriptureNowScreen>) {
        val prayerId: String by destination.stringPath()
        PrayerTimerUi.Content()
    }
}
