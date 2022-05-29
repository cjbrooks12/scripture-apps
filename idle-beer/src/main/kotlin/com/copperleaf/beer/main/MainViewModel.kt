package com.copperleaf.beer.main

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.savedstate.BallastSavedStateInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class MainViewModel(
    coroutineScope: CoroutineScope,
) : BasicViewModel<
    MainContract.Inputs,
    MainContract.Events,
    MainContract.State>(
    coroutineScope = coroutineScope,
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += BallastSavedStateInterceptor(
                MainSavedStateAdapter()
            )
//            this += BallastDebuggerInterceptor(
//                BallastDebuggerClientConnection(OkHttp, coroutineScope)
//            )
        }
        .forViewModel(
            inputHandler = MainInputHandler(),
            initialState = MainContract.State(),
            name = "Main",
        ),
    eventHandler = MainEventHandler(),
)
