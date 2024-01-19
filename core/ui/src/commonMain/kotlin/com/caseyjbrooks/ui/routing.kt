package com.caseyjbrooks.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.caseyjbrooks.routing.LocalKoin
import com.caseyjbrooks.routing.LocalRouter
import com.caseyjbrooks.routing.RouterViewModel

@Composable
internal fun WithRouter(
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
