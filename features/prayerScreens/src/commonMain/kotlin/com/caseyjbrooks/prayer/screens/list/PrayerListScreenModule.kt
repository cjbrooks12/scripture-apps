package com.caseyjbrooks.prayer.screens.list

import com.caseyjbrooks.ballast.buildWithViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

public val prayerListScreenModule: Module = module {
    factoryOf(::PrayerListInputHandler)
    factoryOf(::PrayerListEventHandler)

    factory<PrayerListViewModel> { (viewModelCoroutineScope: CoroutineScope) ->
        PrayerListViewModel(
            coroutineScope = viewModelCoroutineScope,
            config = buildWithViewModel(
                initialState = PrayerListContract.State(),
                inputHandler = get<PrayerListInputHandler>(),
                name = "Prayer List",
            ) {
                PrayerListContract.Inputs.ObservePrayerList
            },
            eventHandler = get<PrayerListEventHandler>(),
        )
    }
}
