package com.caseyjbrooks.votd.pillars

import com.caseyjbrooks.votd.domain.verseOfTheDayDomainModule
import com.caseyjbrooks.votd.realVerseOfTheDayDataModule
import com.caseyjbrooks.votd.schedules.verseOfTheDaySchedulesModule
import com.caseyjbrooks.votd.screens.verseOfTheDayScreensModule
import com.caseyjbrooks.votd.widgets.verseOfTheDayWidgetsModule
import org.koin.core.module.Module
import org.koin.dsl.module

public val verseOfTheDayPillarModule: Module = module {
    includes(
        realVerseOfTheDayDataModule,
        verseOfTheDayDomainModule,
        verseOfTheDaySchedulesModule,
        verseOfTheDayScreensModule,
        verseOfTheDayWidgetsModule,
    )
}
