package com.caseyjbrooks.scripturenow.ui.widgets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
public actual fun material3Colors(darkTheme: Boolean): ColorScheme {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Dynamic color is available on Android 12+
        dynamicColors(darkTheme)
    } else {
        notDynamicColors(darkTheme)
    }
}

@Composable
@RequiresApi(Build.VERSION_CODES.S)
private fun dynamicColors(darkTheme: Boolean): ColorScheme {
    return if (darkTheme) {
        dynamicDarkColorScheme(LocalContext.current)
    } else {
        dynamicLightColorScheme(LocalContext.current)
    }
}

@Composable
private fun notDynamicColors(darkTheme: Boolean): ColorScheme {
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
    StatusBarColor(darkTheme)
    content()
}

@Composable
private fun StatusBarColor(darkTheme: Boolean) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !darkTheme
    val color = Color.Transparent

    DisposableEffect(systemUiController, useDarkIcons, color) {
        systemUiController.setSystemBarsColor(
            color = color,
            darkIcons = useDarkIcons
        )
        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = useDarkIcons
        )
        systemUiController.setNavigationBarColor(
            color = color,
            darkIcons = useDarkIcons
        )

        onDispose {}
    }
}
