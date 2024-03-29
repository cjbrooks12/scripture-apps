package com.caseyjbrooks.ui.koin

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import org.koin.core.Koin
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.awaitAllStartJobs

public val LocalKoin: ProvidableCompositionLocal<Koin> =
    compositionLocalOf<Koin> {
        error("LocalKoin not provided")
    }

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun WithKoinApplication(
    koin: Koin,
    loadingContent: @Composable () -> Unit = { CircularProgressIndicator() },
    readyContent: @Composable () -> Unit,
) {
    val koinIsReady: Boolean by produceState(false) {
        koin.awaitAllStartJobs()
        value = true
    }

    if (!koinIsReady) {
        loadingContent()
    } else {
        CompositionLocalProvider(
            LocalKoin provides koin,
        ) {
            readyContent()
        }
    }
}
