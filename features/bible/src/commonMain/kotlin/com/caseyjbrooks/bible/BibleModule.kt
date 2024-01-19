package com.caseyjbrooks.bible

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.List
import com.caseyjbrooks.bible.ui.bible.BibleReaderRoute
import com.caseyjbrooks.di.Pillar
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.ScriptureNowScreen

public object BibleModule : Pillar {
    override val routes: List<ScriptureNowScreen> = listOf(
        BibleReaderRoute,
    )
    override val mainNavigationItem: NavigationItem? = null
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
