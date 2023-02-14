package com.caseyjbrooks.scripturenow.ui.widgets

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
public actual fun backHandler(enabled: Boolean, onBackPressed: () -> Unit) {
    BackHandler(enabled) {
        onBackPressed()
    }
}
