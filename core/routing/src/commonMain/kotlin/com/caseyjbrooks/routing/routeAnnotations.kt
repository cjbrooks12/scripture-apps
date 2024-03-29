package com.caseyjbrooks.routing

import com.copperleaf.ballast.navigation.routing.RouteAnnotation

/**
 * In large-format screens with a master-detail type view, denotes a route that should be displayed in the "master" pane
 */
public object ListPane : RouteAnnotation

/**
 * In large-format screens with a master-detail type view, denotes a route that should be displayed in the "detail" pane
 */
public object DetailPane : RouteAnnotation
