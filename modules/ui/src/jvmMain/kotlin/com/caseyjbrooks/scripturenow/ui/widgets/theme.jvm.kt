package com.caseyjbrooks.scripturenow.ui.widgets

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
public actual fun material3Colors(darkTheme: Boolean): ColorScheme {
    return if (darkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }
}

@Composable
public actual fun material2Colors(darkTheme: Boolean): Colors {
    return if (darkTheme) {
        darkColors()
    } else {
        lightColors()
    }
}

@Composable
public actual fun ThemeWrapper(darkTheme: Boolean, content: @Composable () -> Unit) {
    content()
}
