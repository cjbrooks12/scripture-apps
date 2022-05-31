package com.caseyjbrooks.app

import android.content.Context
import androidx.activity.compose.BackHandler
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
import com.caseyjbrooks.app.ui.RouterContent
import com.caseyjbrooks.app.utils.ComposeActivity
import com.copperleaf.ballast.router.RouterContract
import com.copperleaf.scripturenow.di.Injector
import com.copperleaf.scripturenow.di.kodein.ApplicationContext
import com.copperleaf.scripturenow.di.kodein.KodeinInjector
import com.copperleaf.scripturenow.ui.Destinations
import com.copperleaf.scripturenow.ui.currentDestination
import com.copperleaf.scripturenow.ui.currentRoute
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

class MainActivity : ComposeActivity() {

    private val injector: Injector = KodeinInjector.create(
        onBackstackEmptied = {
            this@MainActivity.finish()
        },
        additionalModule = DI.Module("Android Context") {
            bind<Context>(tag = ApplicationContext) {
                singleton { this@MainActivity.applicationContext }
            }
        }
    )

    @Composable
    override fun ScreenContent() {
        val routerViewModel = injector.mainRouter

        val routerState by routerViewModel.observeStates().collectAsState()

        BackHandler {
            routerViewModel.trySend(
                RouterContract.Inputs.GoBack
            )
        }

        Scaffold(
            bottomBar = {
                val items = listOf(
                    Destinations.App.Home,
                    Destinations.App.VerseOfTheDay,
                    Destinations.App.Verses,
                )
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
                    RouterContent(it, injector)
                }
            }
        )
    }
}
