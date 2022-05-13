package com.caseyjbrooks

import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.caseyjbrooks.app.utils.ComposeActivity
import com.caseyjbrooks.app.utils.theme.BrandIcons
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.isLoading
import com.copperleaf.scripturenow.ScriptureNowDatabase
import com.copperleaf.scripturenow.di.Injector
import com.copperleaf.scripturenow.votd.VerseOfTheDay
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ktor.client.engine.okhttp.OkHttp
import java.util.Locale

class MainActivity : ComposeActivity() {

    sealed class BottomBarScreen(val route: String, val icon: ImageVector) {
        object Home : BottomBarScreen("home", BrandIcons.material.Home)
        object Topics : BottomBarScreen("topics", BrandIcons.material.Home)
        object Favorites : BottomBarScreen("favorites", BrandIcons.material.Favorite)
    }

    private val injector: Injector = Injector(
        OkHttp,
        AndroidSqliteDriver(ScriptureNowDatabase.Schema, this, "scripture_now.db")
    )
    private val verseOfTheDayRepository = injector.verseOfTheDayRepository

    @Composable
    override fun ScreenContent() {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                val items = listOf(
                    BottomBarScreen.Home,
                    BottomBarScreen.Topics,
                    BottomBarScreen.Favorites,
                )
                BottomNavigation {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->
                        BottomNavigationItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.route.capitalize(Locale.getDefault())) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            },
            content = {
                NavHost(navController = navController, startDestination = "home") {
                    composable(BottomBarScreen.Home.route) {
                        Column {
                            Text("Home")
                            val query = remember { verseOfTheDayRepository.getVerseOfTheDay(false) }
                            val cachedVotd by query.collectAsState(Cached.NotLoaded())

                            val votd = cachedVotd.getCachedOrNull()

                            if(cachedVotd.isLoading()) {
                                CircularProgressIndicator()
                            } else if(votd == null) {
                                val error = (cachedVotd as Cached.FetchingFailed<VerseOfTheDay>).error
                                Text("${error.message}")
                            } else {
                                Text(votd.text)
                                Text(votd.reference)
                            }
                        }
                    }
                    composable(BottomBarScreen.Topics.route) { Text("Topics") }
                    composable(BottomBarScreen.Favorites.route) { Text("Favorites") }
                }
            }
        )
    }
}

