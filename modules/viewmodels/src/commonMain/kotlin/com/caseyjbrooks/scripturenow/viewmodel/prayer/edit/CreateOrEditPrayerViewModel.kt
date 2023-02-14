package com.caseyjbrooks.scripturenow.viewmodel.prayer.edit

import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

public typealias CreateOrEditPrayerViewModel = BasicViewModel<
        CreateOrEditPrayerContract.Inputs,
        CreateOrEditPrayerContract.Events,
        CreateOrEditPrayerContract.State>

public fun interface CreateOrEditPrayerViewModelProvider {
    public fun getCreateOrEditPrayerViewModel(
        coroutineScope: CoroutineScope,
        prayerId: String?,
    ): CreateOrEditPrayerViewModel
}
