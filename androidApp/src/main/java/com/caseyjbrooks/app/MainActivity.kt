package com.caseyjbrooks.app

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.caseyjbrooks.app.ui.home.HomeScreen
import com.caseyjbrooks.app.ui.home.NotFoundScreen
import com.caseyjbrooks.app.ui.verses.EditVerseScreen
import com.caseyjbrooks.app.ui.verses.NewVerseScreen
import com.caseyjbrooks.app.ui.verses.VerseListScreen
import com.caseyjbrooks.app.ui.verses.ViewVerseScreen
import com.caseyjbrooks.app.ui.votd.VotdScreen
import com.caseyjbrooks.app.utils.ComposeActivity
import com.caseyjbrooks.app.utils.ComposeScreen
import com.caseyjbrooks.app.utils.theme.LocalInjector
import com.copperleaf.ballast.router.RouterContract
import com.copperleaf.scripturenow.ui.Destinations
import com.copperleaf.scripturenow.ui.currentDestination
import com.copperleaf.scripturenow.ui.currentRoute
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class MainActivity : ComposeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    override fun ScreenContent() {
        val routerViewModel = LocalInjector.current.mainRouter

        val routerState by routerViewModel.observeStates().collectAsState()

        BackHandler {
            routerViewModel.trySend(
                RouterContract.Inputs.GoBack
            )
        }

        val composeScreens = listOf(
            HomeScreen(),
            VotdScreen(),
            VerseListScreen(),
            NewVerseScreen(),
            ViewVerseScreen(),
            EditVerseScreen(),
        )
        val items = listOf(
            Destinations.App.Home,
            Destinations.App.VerseOfTheDay,
            Destinations.App.Verses,
        )

        Scaffold(
            bottomBar = {
                BottomNavigation {
                    items.forEach { screen ->
                        BottomNavigationItem(
                            icon = {
                                when (screen) {
                                    is Destinations.App.Home -> {
                                        Icon(Icons.Default.Home, contentDescription = "Home")
                                    }
                                    is Destinations.App.VerseOfTheDay -> {
                                        Icon(Icons.Default.Home, contentDescription = "VOTD")
                                    }
                                    is Destinations.App.Verses -> {
                                        Icon(Icons.Default.Home, contentDescription = "Verses")
                                    }
                                    else -> {
                                        Text("Error, not a valid top-level route")
                                    }
                                }
                            },
                            label = {
                                when (screen) {
                                    is Destinations.App.Home -> {
                                        Text("Home")
                                    }
                                    is Destinations.App.VerseOfTheDay -> {
                                        Text("VOTD")
                                    }
                                    is Destinations.App.Verses -> {
                                        Text("Verses")
                                    }
                                    else -> {
                                        Text("Error, not a valid top-level route")
                                    }
                                }
                            },
                            selected = screen.destination() == routerState.currentDestination,
                            onClick = {
                                routerViewModel.trySend(
                                    RouterContract.Inputs.GoToDestination(screen.destination())
                                )
                            }
                        )
                    }
                }
            },
            content = {
                routerState.currentDestination?.currentRoute?.let {
                    AnimatedContent(it) {route ->
                        val currentScreen: ComposeScreen = composeScreens
                            .firstOrNull { it.matchesRoute(route) }
                            ?: NotFoundScreen()

                        Firebase.analytics.logEvent("screen_view") {
                            param(FirebaseAnalytics.Param.SCREEN_NAME, currentScreen.screenName)
                            param(FirebaseAnalytics.Param.SCREEN_CLASS, currentScreen::class.java.name)
                        }

                        currentScreen.ScreenContent()
                    }
                }
            }
        )
    }
}
