package com.caseyjbrooks.di

import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.ScriptureNowScreen

public open class CombinedPillar(
    private vararg val modules: Pillar,
    override val initialRoute: ScriptureNowScreen? = modules.firstNotNullOfOrNull { it.initialRoute },
) : Pillar {
    override val routes: List<ScriptureNowScreen> = modules.flatMap { it.routes }
    override val mainNavigationItems: List<NavigationItem> = modules.flatMap { it.mainNavigationItems }
    override val secondaryNavigationItems: List<NavigationItem> = modules.flatMap { it.secondaryNavigationItems }
}
