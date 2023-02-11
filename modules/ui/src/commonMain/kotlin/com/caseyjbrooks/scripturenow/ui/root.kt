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
import com.caseyjbrooks.scripturenow.viewmodel.memory.detail.MemoryVerseDetailsContract
import com.caseyjbrooks.scripturenow.viewmodel.memory.edit.CreateOrEditMemoryVerseContract
import com.caseyjbrooks.scripturenow.viewmodel.memory.list.MemoryVerseListContract
import com.caseyjbrooks.scripturenow.viewmodel.prayer.detail.PrayerDetailsContract
import com.caseyjbrooks.scripturenow.viewmodel.prayer.edit.CreateOrEditPrayerContract
import com.caseyjbrooks.scripturenow.viewmodel.prayer.list.PrayerListContract
import com.caseyjbrooks.scripturenow.viewmodel.settings.SettingsContract
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
            SettingsScreen(SettingsContract.State()) { }
        }

        // Prayers
        ScriptureNowRoute.MemoryVerseList -> {
            MemoryVerseListScreen(MemoryVerseListContract.State()) { }
        }

        ScriptureNowRoute.MemoryVerseDetails -> {
            val verseId by stringPath()
            MemoryVerseDetailsScreen(MemoryVerseDetailsContract.State()) { }
        }

        ScriptureNowRoute.MemoryVerseCreate -> {
            EditMemoryVerseScreen(CreateOrEditMemoryVerseContract.State()) { }
        }

        ScriptureNowRoute.MemoryVerseEdit -> {
            val verseId by stringPath()
            EditMemoryVerseScreen(CreateOrEditMemoryVerseContract.State()) { }
        }

        // Prayers
        ScriptureNowRoute.PrayerList -> {
            PrayerListScreen(PrayerListContract.State()) { }
        }

        ScriptureNowRoute.PrayerDetails -> {
            val prayerId by stringPath()
            PrayerDetailsScreen(PrayerDetailsContract.State()) { }
        }

        ScriptureNowRoute.PrayerCreate -> {
            EditPrayerScreen(CreateOrEditPrayerContract.State()) { }
        }

        ScriptureNowRoute.PrayerEdit -> {
            val prayerId by stringPath()
            EditPrayerScreen(CreateOrEditPrayerContract.State()) { }
        }
    }
}
