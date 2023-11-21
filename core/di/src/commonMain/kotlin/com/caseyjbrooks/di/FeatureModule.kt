package com.caseyjbrooks.di

import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.ScriptureNowScreen

public interface FeatureModule {
    public val routes: List<ScriptureNowScreen>
    public val initialRoute: ScriptureNowScreen?
    public val mainNavigationItems: List<NavigationItem>
    public val secondaryNavigationItems: List<NavigationItem>
}
