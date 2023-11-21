package com.caseyjbrooks.routing

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNull
import com.copperleaf.ballast.navigation.routing.directions

public data class MainNavigationItem(
    val route: ScriptureNowScreen,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector,
    val label: String,
)

@Composable
public fun MainNavigationRail(
    navigationItems: List<MainNavigationItem>,
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
    navigationItems: List<MainNavigationItem>,
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
