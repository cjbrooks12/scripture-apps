package com.copperleaf.scripturenow.ui

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.plusAssign
import com.copperleaf.scripturenow.utils.KermitBallastLogger
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

const val UiConfigBuilder = "UiConfigBuilder"

fun mainUiModule() = DI.Module(name = "UI >> Main") {
    bind<BallastViewModelConfiguration.Builder>(tag = UiConfigBuilder) {
        provider {
            BallastViewModelConfiguration.Builder()
                .apply {
                    this += LoggingInterceptor()
                    logger = { tag -> KermitBallastLogger(instance(arg = tag)) }

//                    this += BallastDebuggerInterceptor(instance<BallastDebuggerClientConnection<*>>())
                }
        }
    }
}
