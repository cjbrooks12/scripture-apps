package com.caseyjbrooks.verses.screens.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.benasher44.uuid.uuidFrom
import com.caseyjbrooks.routing.ApplicationScreen
import com.caseyjbrooks.routing.DetailPane
import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.models.VerseId
import com.caseyjbrooks.verses.screens.form.VerseFormRoute
import com.caseyjbrooks.verses.screens.list.VerseListRoute
import com.caseyjbrooks.verses.screens.practice.VersePracticeRoute
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.copperleaf.ballast.navigation.routing.stringPath

public object VerseDetailRoute : ApplicationScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/verse/view/{verseId}")
    override val annotations: Set<RouteAnnotation> = setOf(DetailPane)

    public object Directions {
        public fun list(): String {
            return VerseListRoute
                .directions()
                .build()
        }

        public fun edit(verse: SavedVerse): String {
            return VerseFormRoute
                .directions()
                .pathParameter("verseId", verse.uuid.uuid.toString())
                .build()
        }

        public fun practice(verse: SavedVerse): String {
            return VersePracticeRoute
                .directions()
                .pathParameter("verseId", verse.uuid.uuid.toString())
                .build()
        }
    }

    @Composable
    override fun Content(destination: Destination.Match<ApplicationScreen>) {
        val verseId: String by destination.stringPath()

        key(verseId) {
            VerseDetailScreen.Content(VerseId(uuidFrom(verseId)))
        }
    }
}
