package com.caseyjbrooks.topicalbible

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import com.caseyjbrooks.di.Pillar
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.caseyjbrooks.topicalbible.ui.topics.BibleTopicsRoute

public object TopicalBibleModule : Pillar {
    override val routes: List<ScriptureNowScreen> = listOf(
        BibleTopicsRoute,
    )
    override val mainNavigationItem: NavigationItem? = null
    override val secondaryNavigationItems: List<NavigationItem> = listOf(
        NavigationItem(
            route = BibleTopicsRoute,
            iconFilled = Icons.Filled.Search,
            iconOutlined = Icons.Outlined.Search,
            label = "Topic Search",
            order = 35,
        ),
    )
}
