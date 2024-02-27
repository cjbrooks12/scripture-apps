package com.caseyjbrooks.ui.logging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import co.touchlab.kermit.Logger
import com.caseyjbrooks.ui.koin.LocalKoin
import org.koin.core.parameter.parametersOf

public val LocalLogger: ProvidableCompositionLocal<Logger> =
    staticCompositionLocalOf<Logger> {
        error("LocalLogger not provided")
    }

@Composable
internal fun WithLogger(
    content: @Composable () -> Unit,
) {
    val koin = LocalKoin.current
    val logger: Logger = remember(koin) {
        koin.get { parametersOf("UI") }
    }

    CompositionLocalProvider(
        LocalLogger provides logger,
    ) {
        content()
    }
}
