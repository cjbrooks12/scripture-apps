package com.caseyjbrooks.routing

/**
 * A Pillar is a general encapsulation of the screens and functionality of one part of the application structure.
 */
public interface Pillar {
    /**
     * A list of all routes that can be navigated to within this pillar.
     */
    public val routes: List<ScriptureNowScreen>

    /**
     * If this pillar has an item which can be placed in the main navigation (such as the Android bottom navigation
     * bar), specify it here.
     */
    public val mainNavigationItem: NavigationItem?

    /**
     * In larger formats which can display more top-level navigation items, these will also be shown. Do not re-include
     * the main navigation item, the desktop display will show items from both main and secondary navigation items.
     */
    public val secondaryNavigationItems: List<NavigationItem>
}
