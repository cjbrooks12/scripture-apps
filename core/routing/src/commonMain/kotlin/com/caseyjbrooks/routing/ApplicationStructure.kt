package com.caseyjbrooks.routing

public interface ApplicationStructure {
    public val pillars: List<Pillar>
    public val initialRoute: ApplicationScreen
    public val mainNavigationItems: List<NavigationItem>

    public val secondaryNavigationItems: List<NavigationItem> get() = pillars.flatMap { it.secondaryNavigationItems }
    public val allRoutes: List<ApplicationScreen> get() = pillars.flatMap { it.routes }
}
