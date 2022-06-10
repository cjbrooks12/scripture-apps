package com.copperleaf.scripturenow.ui.settings

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.plusAssign
import com.copperleaf.scripturenow.utils.BootstrapInterceptor
import kotlinx.coroutines.CoroutineScope

class SettingsViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: SettingsInputHandler,
    eventHandler: EventHandler<
        SettingsContract.Inputs,
        SettingsContract.Events,
        SettingsContract.State>,
) : BasicViewModel<
    SettingsContract.Inputs,
    SettingsContract.Events,
    SettingsContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .apply {
            this += BootstrapInterceptor {
                SettingsContract.Inputs.Initialize(false)
            }
        }
        .forViewModel(
            inputHandler = inputHandler,
            initialState = SettingsContract.State(),
            name = "Settings",
        ),
    eventHandler = eventHandler,
)
