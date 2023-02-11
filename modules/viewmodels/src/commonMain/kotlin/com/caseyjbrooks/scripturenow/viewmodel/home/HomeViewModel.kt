package com.caseyjbrooks.scripturenow.viewmodel.home

import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

public typealias HomeViewModel = BasicViewModel<
        HomeContract.Inputs,
        HomeContract.Events,
        HomeContract.State>

public fun interface HomeViewModelProvider {
    public fun getHomeViewModel(coroutineScope: CoroutineScope): HomeViewModel
}
