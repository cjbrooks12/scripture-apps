package com.caseyjbrooks.scripturememory

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import com.caseyjbrooks.di.Pillar
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.caseyjbrooks.scripturememory.ui.list.SavedVersesListRoute

public object ScriptureMemoryModule : Pillar {
    override val routes: List<ScriptureNowScreen> = listOf(
        SavedVersesListRoute,
    )
    override val mainNavigationItem: NavigationItem = NavigationItem(
        route = SavedVersesListRoute,
        iconFilled = Icons.Filled.Star,
        iconOutlined = Icons.Outlined.Star,
        label = "Verses",
        order = 20,
    )
    override val secondaryNavigationItems: List<NavigationItem> = listOf()
}
