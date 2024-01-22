package com.caseyjbrooks.prayer.pillars

import com.caseyjbrooks.votd.schedules.votdSchedulesModule
import org.koin.core.module.Module
import org.koin.dsl.module

public val verseOfTheDayPillarModule: Module = module {
    includes(
        votdSchedulesModule
    )
}
