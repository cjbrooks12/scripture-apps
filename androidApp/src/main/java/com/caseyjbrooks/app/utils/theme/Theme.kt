package com.caseyjbrooks.app.utils.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.caseyjbrooks.app.MainApplication
import com.copperleaf.scripturenow.di.Injector

val LocalInjector = staticCompositionLocalOf<Injector> { error("Injector not provided") }

@Composable
fun BrandTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val applicationContext = LocalContext.current.applicationContext as MainApplication
    val injector = applicationContext.injector

    MaterialTheme(
        colors = if (darkTheme) DarkColors else LightColors,
        typography = BrandTypography,
        shapes = BrandShapes,
        content = {
            CompositionLocalProvider(LocalInjector providesDefault injector) {
                StatusBarColor()
                content()
            }
        }
    )
}

/**
 * Set the StatusBar color to the same as the default TopAppBar color.
 */
@Composable
private fun StatusBarColor() {
//    val systemUiController = rememberSystemUiController()
//    val colors = MaterialTheme.colors
//
//    val elevationOverlay = LocalElevationOverlay.current
//    val absoluteElevation = LocalAbsoluteElevation.current + AppBarDefaults.TopAppBarElevation
//    val appBarSurfaceColor = elevationOverlay
//        ?.apply(MaterialTheme.colors.surface, absoluteElevation)
//        ?: MaterialTheme.colors.surface
//
//    SideEffect {
//        systemUiController.setStatusBarColor(
//            color = if(colors.isLight) colors.primary else appBarSurfaceColor,
//            darkIcons = colors.isLight
//        )
//    }
}

private val LightColors @Composable get() = lightColors(
    primary = BrandPrimary,
    primaryVariant = BrandPrimaryVariant,
    onPrimary = Color.White,

    secondary = BrandSecondary,
    secondaryVariant = BrandSecondaryVariant,
    onSecondary = Color.White,

    background = BrandBackground,
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

private val DarkColors @Composable get() = darkColors(
    primary = BrandPrimary,
    primaryVariant = BrandPrimaryVariant,
    onPrimary = Color.White,

    secondary = BrandSecondary,
    secondaryVariant = BrandSecondaryVariant,
    onSecondary = Color.White,

    onSurface = Color.White,
)