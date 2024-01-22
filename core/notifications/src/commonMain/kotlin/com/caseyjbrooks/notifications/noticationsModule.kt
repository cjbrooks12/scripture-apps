package com.caseyjbrooks.notifications

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val fakeNotificationModule: Module = module {
    factoryOf(::FakeNotificationService).bind<NotificationService>()
}
