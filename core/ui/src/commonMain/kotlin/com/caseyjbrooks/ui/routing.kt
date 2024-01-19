package com.caseyjbrooks.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.caseyjbrooks.routing.LocalKoin
import com.caseyjbrooks.routing.LocalRouter
import com.caseyjbrooks.routing.RouterViewModel
import com.copperleaf.ballast.navigation.routing.RouterContract

@Composable
internal expect fun OnBackPressed(
    onBackPressed: () -> Unit,
)

@Composable
internal fun WithRouter(
    content: @Composable () -> Unit,
) {
    val koin = LocalKoin.current
    val router: RouterViewModel = remember(koin) {
        koin.get()
    }

    OnBackPressed {
        router.trySend(RouterContract.Inputs.GoBack())
    }

    CompositionLocalProvider(
        LocalRouter provides router,
    ) {
        content()
    }
}
