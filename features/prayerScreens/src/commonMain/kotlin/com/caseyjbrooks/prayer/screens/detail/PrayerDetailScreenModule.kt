package com.caseyjbrooks.prayer.screens.detail

import com.caseyjbrooks.ballast.buildWithViewModel
import com.caseyjbrooks.prayer.models.PrayerId
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

public val prayerDetailScreenModule: Module = module {
    factoryOf(::PrayerDetailInputHandler)
    factoryOf(::PrayerDetailEventHandler)

    factory<PrayerDetailViewModel> { (viewModelCoroutineScope: CoroutineScope, prayerId: PrayerId) ->
        PrayerDetailViewModel(
            coroutineScope = viewModelCoroutineScope,
            config = buildWithViewModel(
                initialState = PrayerDetailContract.State(prayerId = prayerId),
                inputHandler = get<PrayerDetailInputHandler>(),
                name = "Prayer Detail",
            ) {
                PrayerDetailContract.Inputs.ObservePrayer(prayerId)
            },
            eventHandler = get<PrayerDetailEventHandler>(),
        )
    }
}
