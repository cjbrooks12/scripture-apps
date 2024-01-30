package com.caseyjbrooks.notifications

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual fun getRealPlatformNotificationModule(): Module = module {
    factoryOf(::FakeNotificationService).bind<NotificationService>()
}

internal actual fun getFakePlatformNotificationModule(): Module = module {
    factoryOf(::FakeNotificationService).bind<NotificationService>()
}
