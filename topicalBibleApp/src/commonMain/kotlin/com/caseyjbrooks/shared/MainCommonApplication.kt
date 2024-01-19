package com.caseyjbrooks.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.di.Pillar
import com.caseyjbrooks.routing.LocalKoin
import com.caseyjbrooks.routing.LocalRouter
import com.caseyjbrooks.routing.LocalSizeClass
import com.caseyjbrooks.routing.MainNavigationBar
import com.caseyjbrooks.routing.MainNavigationRail
import com.caseyjbrooks.routing.RouterViewModel
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.caseyjbrooks.routing.currentBackstack
import com.caseyjbrooks.routing.currentDetailBackstack
import com.caseyjbrooks.routing.currentListBackstack
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination
import org.koin.core.Koin
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.awaitAllStartJobs

@Composable
public fun MainCommonApplication(
    koin: Koin,
    deepLinkUri: String?,
) {
    WithKoinApplication(koin) {
        WithRouter {
            val initialPillar: Pillar = LocalKoin.current.get()
            WithSizeClass(
                desktopContent = { DesktopLayout(initialPillar) },
                phoneContent = { PhoneLayout(initialPillar) },
            )
        }
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
private fun WithKoinApplication(
    koin: Koin,
    loadingContent: @Composable () -> Unit = { CircularProgressIndicator() },
    readyContent: @Composable () -> Unit,
) {
    val koinIsReady: Boolean by produceState(false) {
        koin.awaitAllStartJobs()
        value = true
    }

    if (!koinIsReady) {
        loadingContent()
    } else {
        CompositionLocalProvider(
            LocalKoin provides koin,
        ) {
            readyContent()
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun WithSizeClass(
    desktopContent: @Composable () -> Unit,
    phoneContent: @Composable () -> Unit,
) {
    val windowSizeClass = calculateWindowSizeClass()
    CompositionLocalProvider(
        LocalSizeClass provides windowSizeClass.widthSizeClass,
    ) {
        when (LocalSizeClass.current) {
            WindowWidthSizeClass.Compact,
            WindowWidthSizeClass.Medium,
            -> {
                phoneContent()
            }

            WindowWidthSizeClass.Expanded -> {
                desktopContent()
            }
        }
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
private fun WithRouter(
    content: @Composable () -> Unit,
) {
    val koin = LocalKoin.current
    val router: RouterViewModel = remember(koin) {
        koin.get()
    }
    CompositionLocalProvider(
        LocalRouter provides router,
    ) {
        content()
    }
}

@Composable
private fun DesktopLayout(pillar: Pillar) {
    Column {
        Row {
            Column(Modifier.wrapContentWidth()) {
                MainNavigationRail(
                    (pillar.mainNavigationItems + pillar.secondaryNavigationItems)
                        .sortedBy { it.order },
                )
            }
            Column(Modifier.width(240.dp)) {
                currentListBackstack().renderCurrentDestination(
                    route = { appScreen: ScriptureNowScreen ->
                        appScreen.Content(this)
                    },
                    notFound = {},
                )
            }
            Column(Modifier.weight(1f)) {
                currentDetailBackstack().renderCurrentDestination(
                    route = { appScreen: ScriptureNowScreen ->
                        appScreen.Content(this)
                    },
                    notFound = {},
                )
            }
        }
    }
}

@Composable
private fun PhoneLayout(pillar: Pillar) {
    Scaffold(
        topBar = { },
        bottomBar = {
            MainNavigationBar(
                pillar.mainNavigationItems
                    .sortedBy { it.order },
            )
        },
    ) {
        Column(Modifier.padding(it)) {
            currentBackstack().renderCurrentDestination(
                route = { appScreen: ScriptureNowScreen ->
                    appScreen.Content(this)
                },
                notFound = {},
            )
        }
    }
}
