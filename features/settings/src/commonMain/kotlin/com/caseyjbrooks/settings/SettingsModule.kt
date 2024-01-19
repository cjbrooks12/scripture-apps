package com.caseyjbrooks.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import com.caseyjbrooks.di.Pillar
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.caseyjbrooks.settings.ui.settings.SettingsRoute

public class SettingsModule : Pillar {
    override val routes: List<ScriptureNowScreen> = listOf(
        SettingsRoute,
    )
    override val initialRoute: ScriptureNowScreen? = null
    override val mainNavigationItems: List<NavigationItem> = listOf(
        NavigationItem(
            route = SettingsRoute,
            iconFilled = Icons.Filled.Settings,
            iconOutlined = Icons.Outlined.Settings,
            label = "Settings",
            order = 40,
        ),
    )
    override val secondaryNavigationItems: List<NavigationItem> = listOf()
}
