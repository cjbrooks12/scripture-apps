package com.caseyjbrooks.scripturenow.viewmodel.settings

import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

public typealias SettingsViewModel = BasicViewModel<
        SettingsContract.Inputs,
        SettingsContract.Events,
        SettingsContract.State>

public fun interface SettingsViewModelProvider {
    public fun getSettingsViewModel(coroutineScope: CoroutineScope): SettingsViewModel
}
