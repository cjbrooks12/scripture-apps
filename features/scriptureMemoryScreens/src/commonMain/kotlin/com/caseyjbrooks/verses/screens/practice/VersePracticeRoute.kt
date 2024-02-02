package com.caseyjbrooks.verses.screens.practice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.benasher44.uuid.uuidFrom
import com.caseyjbrooks.routing.ApplicationScreen
import com.caseyjbrooks.routing.DetailPane
import com.caseyjbrooks.verses.models.VerseId
import com.caseyjbrooks.verses.screens.list.VerseListRoute
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.stringPath

public object VersePracticeRoute : ApplicationScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/verse/practice/{verseId}")
    override val annotations: Set<RouteAnnotation> = setOf(DetailPane)

    public object Directions {
        public fun list(): String {
            return VerseListRoute
                .directions()
                .build()
        }
    }

    @Composable
    override fun Content(destination: Destination.Match<ApplicationScreen>) {
        val verseId: String by destination.stringPath()

        key(verseId) {
            VersePracticeScreen.Content(VerseId(uuidFrom(verseId)))
        }
    }
}
