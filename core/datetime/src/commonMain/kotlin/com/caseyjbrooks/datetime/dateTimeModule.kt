package com.caseyjbrooks.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import org.koin.core.module.Module
import org.koin.dsl.module

public val realDateTimeModule: Module = module {
    single<Clock> { Clock.System }
    single<TimeZone> { TimeZone.currentSystemDefault() }
}

public val fakeDateTimeModule: Module = module {
    single<Clock> { Clock.System }
    single<TimeZone> { TimeZone.UTC }
}
