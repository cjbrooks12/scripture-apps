package com.caseyjbrooks.bible

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.List
import com.caseyjbrooks.bible.ui.bible.BibleReaderRoute
import com.caseyjbrooks.routing.Pillar
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.ApplicationScreen

public object BibleModule : Pillar {
    override val routes: List<ApplicationScreen> = listOf(
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
