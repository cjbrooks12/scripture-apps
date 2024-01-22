package com.caseyjbrooks.ui

import com.caseyjbrooks.notifications.NotificationService
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

public val androidNotificationModule: Module = module {
    factory<NotificationService> {
        AndroidNotificationService(
            get(),
            "scripture_now",
            "scripture_now",
            get { parametersOf("Notifications") },
        )
    }
}
