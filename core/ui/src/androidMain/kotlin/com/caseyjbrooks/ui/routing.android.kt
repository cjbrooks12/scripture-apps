package com.caseyjbrooks.ui

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
