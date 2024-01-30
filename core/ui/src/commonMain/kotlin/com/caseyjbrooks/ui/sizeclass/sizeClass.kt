package com.caseyjbrooks.ui.sizeclass

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

public val LocalSizeClass: ProvidableCompositionLocal<WindowWidthSizeClass> =
    staticCompositionLocalOf<WindowWidthSizeClass> {
        error("LocalSizeClass not provided")
    }

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun WithSizeClass(
    desktopContent: @Composable () -> Unit,
    phoneContent: @Composable () -> Unit,
) {
    val windowSizeClass = calculateWindowSizeClass()
    CompositionLocalProvider(
        LocalSizeClass provides windowSizeClass.widthSizeClass,
    ) {
        when (LocalSizeClass.current) {
            WindowWidthSizeClass.Compact,
            WindowWidthSizeClass.Medium,
            -> {
                phoneContent()
            }

            WindowWidthSizeClass.Expanded -> {
                desktopContent()
            }
        }
    }
}
