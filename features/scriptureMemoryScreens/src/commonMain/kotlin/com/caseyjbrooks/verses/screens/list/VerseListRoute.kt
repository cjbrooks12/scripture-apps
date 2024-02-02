package com.caseyjbrooks.verses.screens.list

import androidx.compose.runtime.Composable
import com.caseyjbrooks.routing.ApplicationScreen
import com.caseyjbrooks.routing.ListPane
import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.screens.detail.VerseDetailRoute
import com.caseyjbrooks.verses.screens.form.VerseFormRoute
import com.caseyjbrooks.verses.screens.practice.VersePracticeRoute
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter

public object VerseListRoute : ApplicationScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/verses")
    override val annotations: Set<RouteAnnotation> = setOf(ListPane)

    public object Directions {
        public fun new(): String {
            return VerseFormRoute
                .directions()
                .build()
        }

        public fun view(verse: SavedVerse): String {
            return VerseDetailRoute
                .directions()
                .pathParameter("verseId", verse.uuid.uuid.toString())
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
        VerseListScreen.Content()
    }
}
