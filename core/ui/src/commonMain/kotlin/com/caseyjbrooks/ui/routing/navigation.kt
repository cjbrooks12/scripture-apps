package com.caseyjbrooks.ui.routing

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.caseyjbrooks.routing.NavigationItem
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNull
import com.copperleaf.ballast.navigation.routing.directions

@Composable
public fun MainNavigationRail(
    navigationItems: List<NavigationItem>,
) {
    val router = LocalRouter.current
    val currentListPaneDestination = currentListBackstack().currentDestinationOrNull

    NavigationRail {
        navigationItems.forEach { item ->
            val selected = item.route == currentListPaneDestination?.originalRoute
            NavigationRailItem(
                selected = selected,
                onClick = {
                    router.trySend(
                        RouterContract.Inputs.GoToDestination(
                            item.route
                                .directions()
                                .build(),
                        ),
                    )
                },
                icon = {
                    Icon(if (selected) item.iconFilled else item.iconOutlined, item.label)
                },
                label = {
                    Text(item.label)
                },
            )
        }
    }
}

@Composable
public fun MainNavigationBar(
    navigationItems: List<NavigationItem>,
) {
    val router = LocalRouter.current
    val currentListPaneDestination = currentListBackstack().currentDestinationOrNull

    NavigationBar {
        navigationItems.forEach { item ->
            val selected = item.route == currentListPaneDestination?.originalRoute
            NavigationBarItem(
                selected = selected,
                onClick = {
                    router.trySend(
                        RouterContract.Inputs.GoToDestination(
                            item.route
                                .directions()
                                .build(),
                        ),
                    )
                },
                icon = {
                    Icon(if (selected) item.iconFilled else item.iconOutlined, item.label)
                },
                label = {
                    Text(item.label)
                },
            )
        }
    }
}
