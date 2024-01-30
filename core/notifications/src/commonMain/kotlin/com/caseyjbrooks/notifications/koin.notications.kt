package com.caseyjbrooks.notifications

import org.koin.core.module.Module
import org.koin.dsl.module

internal expect fun getRealPlatformNotificationModule(): Module
public val realNotificationModule: Module = module {
    includes(getRealPlatformNotificationModule())
}

internal expect fun getFakePlatformNotificationModule(): Module
public val fakeNotificationModule: Module = module {
    includes(getFakePlatformNotificationModule())
}
