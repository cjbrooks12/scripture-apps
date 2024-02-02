package com.caseyjbrooks.foryou.ui.dashboard

import com.caseyjbrooks.ballast.buildWithViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

public val forYouDashboardScreenModule: Module = module {
    factoryOf(::ForYouDashboardInputHandler)
    factoryOf(::ForYouDashboardEventHandler)

    factory<ForYouDashboardViewModel> { (viewModelCoroutineScope: CoroutineScope) ->
        ForYouDashboardViewModel(
            coroutineScope = viewModelCoroutineScope,
            config = buildWithViewModel(
                initialState = ForYouDashboardContract.State(),
                inputHandler = get<ForYouDashboardInputHandler>(),
                name = "Prayer List",
            ) {
                ForYouDashboardContract.Inputs.Initialize
            },
            eventHandler = get<ForYouDashboardEventHandler>(),
        )
    }
}
