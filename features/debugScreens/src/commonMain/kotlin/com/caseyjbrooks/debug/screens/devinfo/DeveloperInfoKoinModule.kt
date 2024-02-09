package com.caseyjbrooks.debug.screens.devinfo

import com.caseyjbrooks.ballast.buildWithViewModel
import com.caseyjbrooks.di.KoinModule
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.scheduler.SchedulerInterceptor
import com.copperleaf.ballast.scheduler.schedule.FixedDelaySchedule
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

public class DeveloperInfoKoinModule : KoinModule {
    override fun mainModule(): Module = module {
        factoryOf(::DeveloperInfoInputHandler)
        factoryOf(::DeveloperInfoEventHandler)

        factory<DeveloperInfoViewModel> { (viewModelCoroutineScope: CoroutineScope) ->
            DeveloperInfoViewModel(
                coroutineScope = viewModelCoroutineScope,
                config = buildWithViewModel(
                    initialState = DeveloperInfoContract.State(),
                    inputHandler = get<DeveloperInfoInputHandler>(),
                    name = "Developer Info",
                    configureBuilder = {
                        it.apply {
                            this += SchedulerInterceptor(
                                initialSchedule = {
                                    onSchedule(
                                        "update now",
                                        schedule = FixedDelaySchedule(5.seconds),
                                        scheduledInput = { DeveloperInfoContract.Inputs.UpdateNow }
                                    )
                                }
                            )
                        }
                    }
                ) {
                    DeveloperInfoContract.Inputs.Initialize
                },
                eventHandler = get<DeveloperInfoEventHandler>(),
            )
        }
    }
}
