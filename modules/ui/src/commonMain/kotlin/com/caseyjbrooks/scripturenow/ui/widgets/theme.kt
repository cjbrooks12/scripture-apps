package com.caseyjbrooks.scripturenow.ui.widgets

import androidx.compose.material.Colors
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
public expect fun material3Colors(darkTheme: Boolean): ColorScheme

@Composable
public expect fun material2Colors(darkTheme: Boolean): Colors

@Composable
public expect fun ThemeWrapper(darkTheme: Boolean, content: @Composable () -> Unit)
