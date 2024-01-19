package com.caseyjbrooks.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.di.Pillar
import com.caseyjbrooks.routing.MainNavigationRail
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.caseyjbrooks.routing.currentDetailBackstack
import com.caseyjbrooks.routing.currentListBackstack
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination

@Composable
internal fun DesktopLayout(pillar: Pillar) {
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
