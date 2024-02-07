package com.caseyjbrooks.routing

import com.caseyjbrooks.di.KoinModule
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.navigation.vm.withRouter
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.dsl.module

public class RoutingKoinModule : KoinModule {
    override fun mainModule(): Module = module {
        single<RouterViewModel> {
            val applicationCoroutineScope: CoroutineScope = get()
            val initialPillar: ApplicationStructure = get()

            val initialRoute = initialPillar.initialRoute
            val allRoutes = initialPillar.allRoutes

            val routesSortedByWeight: List<ApplicationScreen> = allRoutes
                .sortedByDescending { it.matcher.weight }

            RouterViewModel(
                config = BallastViewModelConfiguration.Builder()
                    .withRouter(ListRoutingTable(routesSortedByWeight), initialRoute)
                    .build(),
                coroutineScope = applicationCoroutineScope,
            )
        }
    }
}
