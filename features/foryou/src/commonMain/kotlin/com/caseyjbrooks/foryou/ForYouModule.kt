package com.caseyjbrooks.foryou

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Face
import com.caseyjbrooks.foryou.ui.dashboard.ForYouDashboardRoute
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.Pillar
import com.caseyjbrooks.routing.ScriptureNowScreen

public object ForYouModule : Pillar {
    override val routes: List<ScriptureNowScreen> = listOf(
        ForYouDashboardRoute,
    )
    override val mainNavigationItem: NavigationItem = NavigationItem(
        route = ForYouDashboardRoute,
        iconFilled = Icons.Filled.Face,
        iconOutlined = Icons.Outlined.Face,
        label = "For You",
        order = 10,
    )
    override val secondaryNavigationItems: List<NavigationItem> = listOf()
}
