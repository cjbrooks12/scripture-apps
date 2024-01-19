package com.caseyjbrooks.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.caseyjbrooks.di.Pillar
import com.caseyjbrooks.routing.MainNavigationBar
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.caseyjbrooks.routing.currentBackstack
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination

@Composable
internal fun PhoneLayout(pillar: Pillar) {
    Scaffold(
        topBar = { },
        bottomBar = {
            MainNavigationBar(
                pillar.mainNavigationItems
                    .sortedBy { it.order },
            )
        },
    ) {
        Column(Modifier.padding(it)) {
            currentBackstack().renderCurrentDestination(
                route = { appScreen: ScriptureNowScreen ->
                    appScreen.Content(this)
                },
                notFound = {},
            )
        }
    }
}

