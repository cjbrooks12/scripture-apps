package com.caseyjbrooks.scripturenow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.MaterialTheme as Material2Theme
import androidx.compose.material3.MaterialTheme as Material3Theme

@Composable
public fun ScriptureNowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    Material3Theme(
        colorScheme = if (darkTheme) {
            ScriptureNowDarkColorScheme
        } else {
            ScriptureNowLightColorScheme
        },
        typography = ScriptureNowTypography,
        shapes = ScriptureNowShapes,
        content = {
            Material2Theme(
                colors = if (darkTheme) {
                    darkColors()
                } else {
                    lightColors()
                },
                content = {
                    Surface(Modifier.fillMaxSize()) {
                        StatusBarColor()
                        content()
                    }
                }
            )
        }
    )
}

/**
 * Set the StatusBar color to the same as the default TopAppBar color.
 */
@Composable
private fun StatusBarColor() {

}
