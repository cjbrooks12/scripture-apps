package com.caseyjbrooks.prayer.screens.timer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.benasher44.uuid.uuidFrom
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.screens.list.PrayerListRoute
import com.caseyjbrooks.routing.DetailPane
import com.caseyjbrooks.routing.ApplicationScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.stringPath

public object PrayerTimerRoute : ApplicationScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/prayer/prayNow/{prayerId}")
    override val annotations: Set<RouteAnnotation> = setOf(DetailPane)

    public object Directions {
        public fun list(): String {
            return PrayerListRoute
                .directions()
                .build()
        }
    }

    @Composable
    override fun Content(destination: Destination.Match<ApplicationScreen>) {
        val prayerId: String by destination.stringPath()
        key(prayerId) {
            PrayerTimerScreen.Content(PrayerId(uuidFrom(prayerId)))
        }
    }
}
