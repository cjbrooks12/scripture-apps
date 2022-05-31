package com.copperleaf.scripturenow.ui.votd

import com.copperleaf.scripturenow.ui.UiConfigBuilder
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

fun votdVmModule() = DI.Module(name = "UI > VOTD") {
    bind<VotdViewModel> {
        factory { arg: CoroutineScope ->
            VotdViewModel(
                coroutineScope = arg,
                configBuilder = instance(tag = UiConfigBuilder),
                inputHandler = VotdInputHandler(instance()),
                eventHandler = VotdEventHandler(instance()),
            )
        }
    }
}
