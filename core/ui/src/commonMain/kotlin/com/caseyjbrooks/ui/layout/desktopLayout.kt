package com.caseyjbrooks.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.routing.ApplicationStructure
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.caseyjbrooks.ui.routing.MainNavigationRail
import com.caseyjbrooks.ui.routing.currentDetailBackstack
import com.caseyjbrooks.ui.routing.currentListBackstack
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination

@Composable
internal fun DesktopLayout(pillar: ApplicationStructure) {
    Column {
        Row {
            Column(Modifier.wrapContentWidth()) {
                MainNavigationRail(
                    (pillar.mainNavigationItems + pillar.secondaryNavigationItems)
                        .sortedBy { it.order },
                )
            }
            Column(Modifier.width(240.dp)) {
                currentListBackstack().renderCurrentDestination(
                    route = { appScreen: ScriptureNowScreen ->
                        appScreen.Content(this)
                    },
                    notFound = {},
                )
            }
            Column(Modifier.weight(1f)) {
                currentDetailBackstack().renderCurrentDestination(
                    route = { appScreen: ScriptureNowScreen ->
                        appScreen.Content(this)
                    },
                    notFound = {},
                )
            }
        }
    }
}
