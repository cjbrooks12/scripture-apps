package com.caseyjbrooks.debug.screens.logfile

import com.caseyjbrooks.ballast.buildWithViewModel
import com.caseyjbrooks.di.KoinModule
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

public class LogFileKoinModule : KoinModule {
    override fun mainModule(): Module = module {
        factoryOf(::LogFileInputHandler)
        factoryOf(::LogFileEventHandler)

        factory<LogFileViewModel> { (viewModelCoroutineScope: CoroutineScope, logFileName: String) ->
            LogFileViewModel(
                coroutineScope = viewModelCoroutineScope,
                config = buildWithViewModel(
                    initialState = LogFileContract.State(),
                    inputHandler = get<LogFileInputHandler>(),
                    name = "Log File",
                ) {
                    LogFileContract.Inputs.Initialize(logFileName)
                },
                eventHandler = get<LogFileEventHandler>(),
            )
        }
    }
}
