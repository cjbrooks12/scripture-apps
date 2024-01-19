package com.caseyjbrooks.prayer.screens.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.screens.detail.PrayerDetailRoute
import com.caseyjbrooks.prayer.screens.list.PrayerListRoute
import com.caseyjbrooks.routing.DetailPane
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.optionalStringPath
import com.copperleaf.ballast.navigation.routing.pathParameter

public object PrayerFormRoute : ScriptureNowScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/prayer/form/{prayerId?}")
    override val annotations: Set<RouteAnnotation> = setOf(DetailPane)

    public object Directions {
        public fun list(): String {
            return PrayerListRoute
                .directions()
                .build()
        }

        public fun details(prayer: SavedPrayer): String {
            return PrayerDetailRoute
                .directions()
                .pathParameter("prayerId", prayer.uuid.uuid)
                .build()
        }
    }

    @Composable
    override fun Content(destination: Destination.Match<ScriptureNowScreen>) {
        val prayerId: String? by destination.optionalStringPath()
        key(prayerId) {
            PrayerFormScreen.Content(prayerId?.let { PrayerId(it) })
        }
    }
}
