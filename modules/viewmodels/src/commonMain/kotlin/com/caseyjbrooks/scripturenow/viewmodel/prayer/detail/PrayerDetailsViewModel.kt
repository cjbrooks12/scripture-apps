package com.caseyjbrooks.scripturenow.viewmodel.prayer.detail

import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

public typealias PrayerDetailsViewModel = BasicViewModel<
        PrayerDetailsContract.Inputs,
        PrayerDetailsContract.Events,
        PrayerDetailsContract.State>

public fun interface PrayerDetailsViewModelProvider {
    public fun getPrayerDetailsViewModel(
        coroutineScope: CoroutineScope,
        prayerId: String,
    ): PrayerDetailsViewModel
}
