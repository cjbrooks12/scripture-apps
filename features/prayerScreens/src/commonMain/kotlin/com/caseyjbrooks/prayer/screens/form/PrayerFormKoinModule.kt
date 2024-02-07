package com.caseyjbrooks.prayer.screens.form

import com.caseyjbrooks.ballast.buildWithViewModel
import com.caseyjbrooks.di.KoinModule
import com.caseyjbrooks.prayer.models.PrayerId
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal class PrayerFormKoinModule : KoinModule {
    override fun mainModule(): Module = module {
        factoryOf(::PrayerFormInputHandler)
        factoryOf(::PrayerFormEventHandler)

        factory<PrayerFormViewModel> { (viewModelCoroutineScope: CoroutineScope, prayerId: PrayerId?) ->
            PrayerFormViewModel(
                coroutineScope = viewModelCoroutineScope,
                config = buildWithViewModel(
                    initialState = PrayerFormContract.State(prayerId = prayerId),
                    inputHandler = get<PrayerFormInputHandler>(),
                    name = if (prayerId != null) "Edit Prayer $prayerId" else "Create Prayer",
                ) {
                    PrayerFormContract.Inputs.ObservePrayer(prayerId)
                },
                eventHandler = get<PrayerFormEventHandler>(),
            )
        }
    }
}
