package com.caseyjbrooks.domain

import co.touchlab.kermit.Logger
import com.caseyjbrooks.di.KoinModule
import com.caseyjbrooks.domain.bus.EventBus
import com.caseyjbrooks.domain.bus.EventBusImpl
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

public class DomainKoinModule : KoinModule {

    override fun mainModule(): Module = module {
        single<EventBus> {
            EventBusImpl(get<Logger> { parametersOf("Event Bus") })
        }
    }
}
