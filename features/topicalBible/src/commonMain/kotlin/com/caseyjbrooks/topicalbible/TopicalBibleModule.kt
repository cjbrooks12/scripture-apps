package com.caseyjbrooks.topicalbible

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import com.caseyjbrooks.di.FeatureModule
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.caseyjbrooks.topicalbible.ui.topics.BibleTopicsRoute

public class TopicalBibleModule : FeatureModule {
    override val routes: List<ScriptureNowScreen> = listOf(
        BibleTopicsRoute,
    )
    override val initialRoute: ScriptureNowScreen? = null
    override val mainNavigationItems: List<NavigationItem> = listOf()
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
