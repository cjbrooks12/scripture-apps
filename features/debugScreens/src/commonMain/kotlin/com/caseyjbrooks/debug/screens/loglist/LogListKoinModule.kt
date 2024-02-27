package com.caseyjbrooks.debug.screens.loglist

import com.caseyjbrooks.ballast.buildWithViewModel
import com.caseyjbrooks.di.KoinModule
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

public class LogListKoinModule : KoinModule {
    override fun mainModule(): Module = module {
        factoryOf(::LogListInputHandler)
        factoryOf(::LogListEventHandler)

        factory<LogListViewModel> { (viewModelCoroutineScope: CoroutineScope) ->
            LogListViewModel(
                coroutineScope = viewModelCoroutineScope,
                config = buildWithViewModel(
                    initialState = LogListContract.State(),
                    inputHandler = get<LogListInputHandler>(),
                    name = "Log List",
                ) {
                    LogListContract.Inputs.Initialize
                },
                eventHandler = get<LogListEventHandler>(),
            )
        }
    }
}
