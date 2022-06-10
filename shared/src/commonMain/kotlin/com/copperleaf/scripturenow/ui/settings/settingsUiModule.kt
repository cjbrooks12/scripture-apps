package com.copperleaf.scripturenow.ui.settings

import com.copperleaf.scripturenow.ui.UiConfigBuilder
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

fun settingsUiModule() = DI.Module(name = "UI > Settings") {
    bind<SettingsViewModel> {
        factory { arg: CoroutineScope ->
            SettingsViewModel(
                coroutineScope = arg,
                configBuilder = instance(tag = UiConfigBuilder),
                inputHandler = SettingsInputHandler(instance()),
                eventHandler = instance(),
            )
        }
    }
}
