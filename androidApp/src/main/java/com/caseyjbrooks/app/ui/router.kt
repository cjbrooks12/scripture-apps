package com.caseyjbrooks.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.copperleaf.scripturenow.di.Injector
import com.copperleaf.scripturenow.routing.Destinations
import com.copperleaf.scripturenow.routing.Route

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RouterContent(route: Route, injector: Injector) {
    AnimatedContent(route) {
        when(it) {
            is Destinations.App.Home -> {
                Column {
                    Text("Home")
                }
            }
            is Destinations.App.VerseOfTheDay -> {
                Column {
                    Text("Verse of the Day")
                }
            }
            is Destinations.App.Verses -> {
                Column {
                    Text("Verse List")
                }
            }
            is Destinations.App.ViewVerse -> {
                Column {
                    Text("Verse: ${it.verseId}")
                }
            }
            is Destinations.App.EditVerse -> {
                Column {
                    Text("Edit Verse: ${it.verseId}")
                }
            }
        }
    }
}
