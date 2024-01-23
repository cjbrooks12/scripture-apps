package com.caseyjbrooks.prayer.pillars

import com.caseyjbrooks.votd.domain.verseOfTheDayDomainModule
import com.caseyjbrooks.votd.realVerseOfTheDayDataModule
import com.caseyjbrooks.votd.schedules.verseOfTheDaySchedulesModule
import org.koin.core.module.Module
import org.koin.dsl.module

public val verseOfTheDayPillarModule: Module = module {
    includes(
        realVerseOfTheDayDataModule,
        verseOfTheDayDomainModule,
        verseOfTheDaySchedulesModule,
    )
}
