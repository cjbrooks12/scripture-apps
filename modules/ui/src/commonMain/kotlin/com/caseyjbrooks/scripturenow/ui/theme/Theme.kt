package com.caseyjbrooks.scripturenow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.caseyjbrooks.scripturenow.ui.widgets.ThemeWrapper
import com.caseyjbrooks.scripturenow.ui.widgets.material2Colors
import com.caseyjbrooks.scripturenow.ui.widgets.material3Colors
import androidx.compose.material.MaterialTheme as Material2Theme
import androidx.compose.material3.MaterialTheme as Material3Theme

@Composable
public fun ScriptureNowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    Material3Theme(
        colorScheme = material3Colors(darkTheme),
        content = {
            Material2Theme(
                colors = material2Colors(darkTheme),
                content = {
                    Surface(Modifier.fillMaxSize()) {
                        ThemeWrapper(darkTheme) {
                            content()
                        }
                    }
                }
            )
        }
    )
}
