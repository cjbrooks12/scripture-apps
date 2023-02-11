package com.caseyjbrooks.scripturenow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
public fun ScriptureNowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            ScriptureNowDarkColorScheme
        } else {
            ScriptureNowLightColorScheme
        },
        typography = ScriptureNowTypography,
        shapes = ScriptureNowShapes,
        content = {
            StatusBarColor()
            content()
        }
    )
}

/**
 * Set the StatusBar color to the same as the default TopAppBar color.
 */
@Composable
private fun StatusBarColor() {

}
