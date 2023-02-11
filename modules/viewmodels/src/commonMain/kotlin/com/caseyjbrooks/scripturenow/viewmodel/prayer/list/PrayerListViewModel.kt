package com.caseyjbrooks.scripturenow.viewmodel.prayer.list

import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

public typealias PrayerListViewModel = BasicViewModel<
        PrayerListContract.Inputs,
        PrayerListContract.Events,
        PrayerListContract.State>

public fun interface PrayerListViewModelProvider {
    public fun getPrayerListViewModel(coroutineScope: CoroutineScope): PrayerListViewModel
}
