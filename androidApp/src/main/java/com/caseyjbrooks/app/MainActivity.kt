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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.caseyjbrooks.app.ui.home.HomeScreen
import com.caseyjbrooks.app.ui.settings.SettingsScreen
import com.caseyjbrooks.app.ui.verses.EditVerseScreen
import com.caseyjbrooks.app.ui.verses.NewVerseScreen
import com.caseyjbrooks.app.ui.verses.VerseDetailsScreen
import com.caseyjbrooks.app.ui.verses.VerseListScreen
import com.caseyjbrooks.app.ui.votd.VotdScreen
import com.caseyjbrooks.app.utils.ComposeActivity
import com.caseyjbrooks.app.utils.ComposeScreen
import com.caseyjbrooks.app.utils.theme.LocalInjector
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.currentDestination
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNotFound
import com.copperleaf.scripturenow.ui.Destinations
import com.copperleaf.scripturenow.ui.bottomBarDestinations

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
            if(routerState.backstack.filterIsInstance<Destination>().isNotEmpty()) {
                // if there's screens in the backstack, go back
                routerViewModel.trySend(
                    RouterContract.Inputs.GoBack
                )
            } else {
                // otherwise, close the app
                this@MainActivity.finish()
            }
        }

        val composeScreens = listOf(
            HomeScreen(),
            VotdScreen(),
            SettingsScreen(),
            VerseListScreen(),
            NewVerseScreen(),
            VerseDetailsScreen(),
            EditVerseScreen(),
        )
        Scaffold(
            bottomBar = {
                BottomNavigation {
                    bottomBarDestinations.forEach { screen ->
                        BottomNavigationItem(
                            icon = {
                                when (screen.route) {
                                    Destinations.App.Home -> {
                                        Icon(Icons.Default.Home, contentDescription = "Home")
                                    }
                                    Destinations.App.VerseOfTheDay -> {
                                        Icon(Icons.Default.Home, contentDescription = "VOTD")
                                    }
                                    Destinations.App.Verses.List -> {
                                        Icon(Icons.Default.Home, contentDescription = "Verses")
                                    }
                                    Destinations.App.Settings -> {
                                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                                    }
                                    else -> {
                                        Text("Error, not a valid top-level route")
                                    }
                                }
                            },
                            label = {
                                when (screen.route) {
                                    Destinations.App.Home -> {
                                        Text("Home")
                                    }
                                    Destinations.App.VerseOfTheDay -> {
                                        Text("VOTD")
                                    }
                                    Destinations.App.Verses.List -> {
                                        Text("Verses")
                                    }
                                    Destinations.App.Settings -> {
                                        Text("Settings")
                                    }
                                    else -> {
                                        Text("Error, not a valid top-level route")
                                    }
                                }
                            },
                            selected = screen.route == routerState.currentDestination?.originalRoute,
                            onClick = {
                                routerViewModel.trySend(
                                    RouterContract.Inputs.ReplaceTopDestination(screen.target)
                                )
                            }
                        )
                    }
                }
            },
            content = {
                routerState.currentDestinationOrNotFound?.let {
                    AnimatedContent(it) { token ->
                        val destination = token as? Destination
                        val currentScreen: ComposeScreen? = when (token) {
                            is Destination -> {
                                token
                                    .originalRoute
                                    .let { route ->
                                        composeScreens.firstOrNull { screen ->
                                            screen.route == route
                                        }
                                    }
                            }
                            else -> {
                                null
                            }
                        }

                        if(currentScreen != null && destination != null){
                            currentScreen.ScreenContent(destination)
                        }
                    }
                }
            }
        )
    }
}
