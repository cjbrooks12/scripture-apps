package com.caseyjbrooks.notifications

import com.caseyjbrooks.di.KoinModule
import org.koin.core.module.Module
import org.koin.dsl.module

public class NotificationsKoinModule : KoinModule {

    override fun platformKoinModule(): KoinModule? = NotificationsKoinPlatformModule()

    override fun mainModule(): Module = module {
    }
}
