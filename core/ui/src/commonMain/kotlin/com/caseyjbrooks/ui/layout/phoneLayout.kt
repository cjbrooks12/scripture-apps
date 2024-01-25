package com.caseyjbrooks.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.caseyjbrooks.routing.ApplicationStructure
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.caseyjbrooks.ui.routing.MainNavigationBar
import com.caseyjbrooks.ui.routing.currentBackstack
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination

@Composable
internal fun PhoneLayout(pillar: ApplicationStructure) {
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

