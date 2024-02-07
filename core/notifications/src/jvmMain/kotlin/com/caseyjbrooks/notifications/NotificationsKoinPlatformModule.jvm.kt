package com.caseyjbrooks.notifications

import com.caseyjbrooks.di.KoinModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

public actual class NotificationsKoinPlatformModule : KoinModule {
    override fun mainModule(): Module = module {
        factoryOf(::FakeNotificationService).bind<NotificationService>()
    }
}
