package com.caseyjbrooks.prayer.screens.timer

import com.caseyjbrooks.ballast.buildWithViewModel
import com.caseyjbrooks.prayer.models.PrayerId
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

public val prayerTimerScreenModule: Module = module {
    factoryOf(::PrayerTimerInputHandler)
    factoryOf(::PrayerTimerEventHandler)

    factory<PrayerTimerViewModel> { (viewModelCoroutineScope: CoroutineScope, prayerId: PrayerId) ->
        PrayerTimerViewModel(
            coroutineScope = viewModelCoroutineScope,
            config = buildWithViewModel(
                initialState = PrayerTimerContract.State(prayerId = prayerId),
                inputHandler = get<PrayerTimerInputHandler>(),
                name = "Prayer Timer",
                withSchedules = true,
            ) {
                PrayerTimerContract.Inputs.ObservePrayer(prayerId)
            },
            eventHandler = get<PrayerTimerEventHandler>(),
        )
    }
}
