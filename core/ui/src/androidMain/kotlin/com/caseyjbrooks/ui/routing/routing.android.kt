package com.caseyjbrooks.ui.routing

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
internal actual fun OnBackPressed(
    onBackPressed: () -> Unit,
) {
    BackHandler(enabled = true, onBack = {
        onBackPressed()
    })
}
