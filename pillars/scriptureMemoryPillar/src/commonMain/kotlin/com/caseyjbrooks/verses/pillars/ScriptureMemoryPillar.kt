package com.caseyjbrooks.verses.pillars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import com.caseyjbrooks.routing.ApplicationScreen
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.Pillar
import com.caseyjbrooks.verses.screens.detail.VerseDetailRoute
import com.caseyjbrooks.verses.screens.form.VerseFormRoute
import com.caseyjbrooks.verses.screens.list.VerseListRoute
import com.caseyjbrooks.verses.screens.practice.VersePracticeRoute

public object ScriptureMemoryPillar : Pillar {
    override val routes: List<ApplicationScreen> = listOf(
        VerseListRoute,
        VerseDetailRoute,
        VerseFormRoute,
        VersePracticeRoute,
    )
    override val mainNavigationItem: NavigationItem = NavigationItem(
        route = VerseListRoute,
        iconFilled = Icons.Filled.Star,
        iconOutlined = Icons.Outlined.Star,
        label = "Verses",
        order = 20,
    )
    override val secondaryNavigationItems: List<NavigationItem> = listOf()
}
