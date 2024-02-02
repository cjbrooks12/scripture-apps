package com.caseyjbrooks.routing

import androidx.compose.runtime.Composable
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.Route

public interface ApplicationScreen : Route {

    @Composable
    public fun Content(destination: Destination.Match<ApplicationScreen>)
}
