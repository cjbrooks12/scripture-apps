package com.caseyjbrooks.notifications

import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

internal actual fun getRealPlatformNotificationModule(): Module = module {
    factory<NotificationService> {
        AndroidNotificationService(
            get(),
            "scripture_now",
            "scripture_now",
            get { parametersOf("Notifications") },
        )
    }
}

internal actual fun getFakePlatformNotificationModule(): Module = module {
}
