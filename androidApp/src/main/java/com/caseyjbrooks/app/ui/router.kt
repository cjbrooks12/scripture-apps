package com.caseyjbrooks.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.caseyjbrooks.app.ui.votd.VerseOfTheDayScreen
import com.copperleaf.scripturenow.di.Injector
import com.copperleaf.scripturenow.ui.Destinations
import com.copperleaf.scripturenow.ui.Route

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RouterContent(route: Route, injector: Injector) {
    AnimatedContent(route) {r ->
        when(r) {
            is Destinations.App.Home -> {
                Column {
                    Text("Home")
                }
            }
            is Destinations.App.VerseOfTheDay -> {
                VerseOfTheDayScreen(injector)
            }
            is Destinations.App.Verses -> {
                Column {
                    Text("Verse List")
                }
            }
            is Destinations.App.ViewVerse -> {
                Column {
                    Text("Verse: ${r.verseId}")
                }
            }
            is Destinations.App.EditVerse -> {
                Column {
                    Text("Edit Verse: ${r.verseId}")
                }
            }
        }
    }
}
