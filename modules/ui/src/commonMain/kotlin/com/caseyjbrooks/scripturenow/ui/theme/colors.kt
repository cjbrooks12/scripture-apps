package com.caseyjbrooks.scripturenow.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
public class BrandColors(
    public val BrandPrimary: Color,
    public val BrandPrimaryVariant: Color,
    public val BrandPrimaryLight: Color,
    public val BrandSecondary: Color,
    public val BrandSecondaryVariant: Color,
    public val BrandSecondaryLight: Color,
    public val BrandBackground: Color,
)

public val ScriptureNowLightColorScheme: ColorScheme = lightColorScheme()
public val ScriptureNowDarkColorScheme: ColorScheme = darkColorScheme()
