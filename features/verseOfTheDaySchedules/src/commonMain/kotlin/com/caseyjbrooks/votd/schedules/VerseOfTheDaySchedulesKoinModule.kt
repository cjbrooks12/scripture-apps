package com.caseyjbrooks.votd.schedules

import com.caseyjbrooks.ballast.buildWithViewModel
import com.caseyjbrooks.di.KoinModule
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

public class VerseOfTheDaySchedulesKoinModule : KoinModule {
    override fun mainModule(): Module = module {
        factoryOf(::VotdSchedulesInputHandler)
        factoryOf(::VotdSchedulesEventHandler)

        single<VotdSchedulesViewModel> {
            val applicationCoroutineScope: CoroutineScope = get()
            VotdSchedulesViewModel(
                coroutineScope = applicationCoroutineScope,
                config = buildWithViewModel(
                    initialState = VotdSchedulesContract.State(),
                    inputHandler = get<VotdSchedulesInputHandler>(),
                    name = "Votd Schedules",
                ),
                eventHandler = get<VotdSchedulesEventHandler>(),
            )
        }
    }
}
