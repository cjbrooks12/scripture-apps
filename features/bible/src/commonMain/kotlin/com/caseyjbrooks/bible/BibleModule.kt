package com.caseyjbrooks.bible

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.List
import com.caseyjbrooks.bible.ui.bible.BibleReaderRoute
import com.caseyjbrooks.di.FeatureModule
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.ScriptureNowScreen

public class BibleModule : FeatureModule {
    override val routes: List<ScriptureNowScreen> = listOf(
        BibleReaderRoute,
    )
    override val initialRoute: ScriptureNowScreen? = null
    override val mainNavigationItems: List<NavigationItem> = listOf()
    override val secondaryNavigationItems: List<NavigationItem> = listOf(
        NavigationItem(
            route = BibleReaderRoute,
            iconFilled = Icons.Filled.List,
            iconOutlined = Icons.Outlined.List,
            label = "Bible",
            order = 32,
        ),
    )
}
