package com.caseyjbrooks.di

import com.caseyjbrooks.routing.ListRoutingTable
import com.caseyjbrooks.routing.RouterViewModel
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.navigation.vm.withRouter
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.dsl.module

public val routingModule: Module = module {
    single<RouterViewModel> {
        val applicationCoroutineScope: CoroutineScope = get()
        val initialPillar: Pillar = get()

        val initialRoute = initialPillar.initialRoute!!
        val allRoutes = initialPillar.routes

        val routesSortedByWeight: List<ScriptureNowScreen> = allRoutes
            .sortedByDescending { it.matcher.weight }

        RouterViewModel(
            config = BallastViewModelConfiguration.Builder()
                .withRouter(ListRoutingTable(routesSortedByWeight), initialRoute)
                .build(),
            coroutineScope = applicationCoroutineScope,
        )
    }
}
