package com.caseyjbrooks.ui

import androidx.compose.runtime.Composable
import com.caseyjbrooks.routing.ApplicationStructure
import com.caseyjbrooks.ui.koin.LocalKoin
import com.caseyjbrooks.ui.koin.WithKoinApplication
import com.caseyjbrooks.ui.layout.DesktopLayout
import com.caseyjbrooks.ui.layout.PhoneLayout
import com.caseyjbrooks.ui.routing.WithRouter
import com.caseyjbrooks.ui.sizeclass.WithSizeClass
import org.koin.core.Koin

@Composable
public fun ApplicationRoot(
    koin: Koin,
    deepLinkUri: String?,
) {
    WithKoinApplication(koin) {
        WithRouter(deepLinkUri) {
            val initialPillar: ApplicationStructure = LocalKoin.current.get()
            WithSizeClass(
                desktopContent = { DesktopLayout(initialPillar) },
                phoneContent = { PhoneLayout(initialPillar) },
            )
        }
    }
}
