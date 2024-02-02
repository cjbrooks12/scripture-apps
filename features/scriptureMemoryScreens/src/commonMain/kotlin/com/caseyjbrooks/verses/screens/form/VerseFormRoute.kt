package com.caseyjbrooks.verses.screens.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.benasher44.uuid.uuidFrom
import com.caseyjbrooks.verses.models.VerseId
import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.screens.detail.VerseDetailRoute
import com.caseyjbrooks.verses.screens.list.VerseListRoute
import com.caseyjbrooks.routing.DetailPane
import com.caseyjbrooks.routing.ApplicationScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.optionalStringPath
import com.copperleaf.ballast.navigation.routing.pathParameter

public object VerseFormRoute : ApplicationScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/verse/form/{verseId?}")
    override val annotations: Set<RouteAnnotation> = setOf(DetailPane)

    public object Directions {
        public fun list(): String {
            return VerseListRoute
                .directions()
                .build()
        }

        public fun details(verse: SavedVerse): String {
            return VerseDetailRoute
                .directions()
                .pathParameter("verseId", verse.uuid.uuid.toString())
                .build()
        }
    }

    @Composable
    override fun Content(destination: Destination.Match<ApplicationScreen>) {
        val verseId: String? by destination.optionalStringPath()
        key(verseId) {
            VerseFormScreen.Content(verseId?.let { VerseId(uuidFrom(it)) })
        }
    }
}
