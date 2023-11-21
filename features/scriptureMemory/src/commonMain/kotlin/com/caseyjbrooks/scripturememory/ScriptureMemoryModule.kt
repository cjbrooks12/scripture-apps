package com.caseyjbrooks.scripturememory

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import com.caseyjbrooks.di.FeatureModule
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.caseyjbrooks.scripturememory.ui.list.SavedVersesListRoute

public class ScriptureMemoryModule : FeatureModule {
    override val routes: List<ScriptureNowScreen> = listOf(
        SavedVersesListRoute,
    )
    override val initialRoute: ScriptureNowScreen? = null
    override val mainNavigationItems: List<NavigationItem> = listOf(
        NavigationItem(
            route = SavedVersesListRoute,
            iconFilled = Icons.Filled.Star,
            iconOutlined = Icons.Outlined.Star,
            label = "Verses",
            order = 20,
        ),
    )
    override val secondaryNavigationItems: List<NavigationItem> = listOf()
}
