package com.caseyjbrooks.routing

public interface ApplicationStructure {
    public val pillars: List<Pillar>
    public val initialRoute: ScriptureNowScreen
    public val mainNavigationItems: List<NavigationItem>

    public val secondaryNavigationItems: List<NavigationItem> get() = pillars.flatMap { it.secondaryNavigationItems }
    public val allRoutes: List<ScriptureNowScreen> get() = pillars.flatMap { it.routes }
}
