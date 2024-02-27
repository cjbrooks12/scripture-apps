package com.caseyjbrooks.debug.screens

import com.caseyjbrooks.debug.screens.devinfo.DeveloperInfoRoute
import com.caseyjbrooks.debug.screens.logfile.LogFileRoute
import com.caseyjbrooks.debug.screens.loglist.LogListRoute
import com.caseyjbrooks.routing.ApplicationScreen
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.Pillar

public object DebugScreensModule : Pillar {
    override val routes: List<ApplicationScreen> = listOf(
        DeveloperInfoRoute,
        LogListRoute,
        LogFileRoute,
    )
    override val mainNavigationItem: NavigationItem? = null
    override val secondaryNavigationItems: List<NavigationItem> = emptyList()
}
