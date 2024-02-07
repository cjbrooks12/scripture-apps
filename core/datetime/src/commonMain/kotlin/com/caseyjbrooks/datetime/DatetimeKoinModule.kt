package com.caseyjbrooks.datetime

import com.caseyjbrooks.di.KoinModule
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import org.koin.core.module.Module
import org.koin.dsl.module

public class DatetimeKoinModule : KoinModule {

    override fun mainModule(): Module = module {
        single<Clock> { Clock.System }
        single<TimeZone> { TimeZone.currentSystemDefault() }
    }
}
