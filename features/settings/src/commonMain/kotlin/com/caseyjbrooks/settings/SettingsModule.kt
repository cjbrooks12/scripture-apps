package com.caseyjbrooks.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import com.caseyjbrooks.routing.Pillar
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.ApplicationScreen
import com.caseyjbrooks.settings.ui.settings.SettingsRoute

public object SettingsModule : Pillar {
    override val routes: List<ApplicationScreen> = listOf(
        SettingsRoute,
    )
    override val mainNavigationItem: NavigationItem = NavigationItem(
        route = SettingsRoute,
        iconFilled = Icons.Filled.Settings,
        iconOutlined = Icons.Outlined.Settings,
        label = "Settings",
        order = 40,
    )
    override val secondaryNavigationItems: List<NavigationItem> = listOf()
}
