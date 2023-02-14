package com.caseyjbrooks.scripturenow.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.ui.screens.home.HomeScreen
import com.caseyjbrooks.scripturenow.ui.screens.memory.detail.MemoryVerseDetailsScreen
import com.caseyjbrooks.scripturenow.ui.screens.memory.edit.EditMemoryVerseScreen
import com.caseyjbrooks.scripturenow.ui.screens.memory.list.MemoryVerseListScreen
import com.caseyjbrooks.scripturenow.ui.screens.prayer.detail.PrayerDetailsScreen
import com.caseyjbrooks.scripturenow.ui.screens.prayer.edit.EditPrayerScreen
import com.caseyjbrooks.scripturenow.ui.screens.prayer.list.PrayerListScreen
import com.caseyjbrooks.scripturenow.ui.screens.settings.SettingsScreen
import com.caseyjbrooks.scripturenow.ui.screens.votd.VerseOfTheDayScreen
import com.caseyjbrooks.scripturenow.ui.theme.ScriptureNowTheme
import com.caseyjbrooks.scripturenow.viewmodel.ViewModelsInjector
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination
import com.copperleaf.ballast.navigation.routing.stringPath
import com.copperleaf.ballast.navigation.vm.Router

@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun ApplicationRoot(
    router: Router<ScriptureNowRoute>,
    injector: ViewModelsInjector,
) {
    val routerState by router.observeStates().collectAsState()

    ScriptureNowTheme {
        CompositionLocalProvider(
            LocalRouter provides router,
            LocalInjector provides injector,
        ) {
            routerState.renderCurrentDestination(
                route = {
//                    AnimatedContent(this to it) { (match, route) ->
                    RouteContent(it)
//                    }
                },
                notFound = { },
            )
        }
    }
}

@Composable
private fun Destination.Match<ScriptureNowRoute>.RouteContent(
    route: ScriptureNowRoute
) {
    when (route) {
        // Main Screens
        ScriptureNowRoute.Home -> {
            HomeScreen()
        }

        ScriptureNowRoute.VerseOfTheDay -> {
            VerseOfTheDayScreen()
        }

        ScriptureNowRoute.Settings -> {
            SettingsScreen()
        }

        // Prayers
        ScriptureNowRoute.MemoryVerseList -> {
            MemoryVerseListScreen()
        }

        ScriptureNowRoute.MemoryVerseDetails -> {
            val verseId by stringPath()
            MemoryVerseDetailsScreen(verseId)
        }

        ScriptureNowRoute.MemoryVerseCreate -> {
            EditMemoryVerseScreen(null)
        }

        ScriptureNowRoute.MemoryVerseEdit -> {
            val verseId by stringPath()
            EditMemoryVerseScreen(verseId)
        }

        // Prayers
        ScriptureNowRoute.PrayerList -> {
            PrayerListScreen()
        }

        ScriptureNowRoute.PrayerDetails -> {
            val prayerId by stringPath()
            PrayerDetailsScreen(prayerId)
        }

        ScriptureNowRoute.PrayerCreate -> {
            EditPrayerScreen(null)
        }

        ScriptureNowRoute.PrayerEdit -> {
            val prayerId by stringPath()
            EditPrayerScreen(prayerId)
        }
    }
}
