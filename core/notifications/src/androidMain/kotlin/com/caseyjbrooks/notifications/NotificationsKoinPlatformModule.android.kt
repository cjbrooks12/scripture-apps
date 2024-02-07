package com.caseyjbrooks.notifications

import com.caseyjbrooks.di.KoinModule
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

public actual class NotificationsKoinPlatformModule : KoinModule {
    override fun mainModule(): Module = module {
        factory<NotificationService> {
            AndroidNotificationService(
                get(),
                get { parametersOf("Notifications") },
            )
        }
    }
}
