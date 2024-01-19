package com.caseyjbrooks.ui

import androidx.compose.runtime.Composable
import com.caseyjbrooks.di.ApplicationStructure
import com.caseyjbrooks.di.Pillar
import com.caseyjbrooks.routing.LocalKoin
import org.koin.core.Koin

@Composable
public fun ApplicationRoot(
    koin: Koin,
    deepLinkUri: String?,
) {
    WithKoinApplication(koin) {
        WithRouter {
            val initialPillar: ApplicationStructure = LocalKoin.current.get()
            WithSizeClass(
                desktopContent = { DesktopLayout(initialPillar) },
                phoneContent = { PhoneLayout(initialPillar) },
            )
        }
    }
}
