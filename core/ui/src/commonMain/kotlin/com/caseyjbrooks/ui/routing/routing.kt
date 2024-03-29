package com.caseyjbrooks.ui.routing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.caseyjbrooks.routing.DetailPane
import com.caseyjbrooks.routing.ListPane
import com.caseyjbrooks.routing.RouterViewModel
import com.caseyjbrooks.routing.ApplicationScreen
import com.caseyjbrooks.ui.koin.LocalKoin
import com.copperleaf.ballast.navigation.routing.Backstack
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouterContract

public val LocalRouter: ProvidableCompositionLocal<RouterViewModel> =
    staticCompositionLocalOf<RouterViewModel> {
        error("LocalRouter not provided")
    }

@Composable
internal expect fun OnBackPressed(
    onBackPressed: () -> Unit,
)

@Composable
internal fun WithRouter(
    deepLinkUri: String?,
    content: @Composable () -> Unit,
) {
    val koin = LocalKoin.current
    val router: RouterViewModel = remember(koin) {
        koin.get()
    }

    OnBackPressed {
        router.trySend(RouterContract.Inputs.GoBack())
    }

    LaunchedEffect(deepLinkUri) {
        if (deepLinkUri != null) {
            router.trySend(RouterContract.Inputs.RestoreBackstack(listOf(deepLinkUri)))
        }
    }

    CompositionLocalProvider(
        LocalRouter provides router,
    ) {
        content()
    }
}

@Composable
public fun currentBackstack(): Backstack<ApplicationScreen> {
    val router = LocalRouter.current
    val routerState: Backstack<ApplicationScreen> by router.observeStates().collectAsState()
    return routerState
}

@Composable
public fun currentListBackstack(): Backstack<ApplicationScreen> {
    val router = LocalRouter.current
    val routerState: Backstack<ApplicationScreen> by router.observeStates().collectAsState()
    return routerState
        .filterIsInstance<Destination.Match<ApplicationScreen>>()
        .filter { ListPane in it.annotations }
}

@Composable
public fun currentDetailBackstack(): Backstack<ApplicationScreen> {
    val router = LocalRouter.current
    val routerState: Backstack<ApplicationScreen> by router.observeStates().collectAsState()
    return routerState
        .filterIsInstance<Destination.Match<ApplicationScreen>>()
        .filter { DetailPane in it.annotations }
}
