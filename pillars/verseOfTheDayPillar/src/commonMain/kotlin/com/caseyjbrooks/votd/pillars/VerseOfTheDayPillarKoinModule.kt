package com.caseyjbrooks.votd.pillars

import com.caseyjbrooks.di.KoinModule
import com.caseyjbrooks.votd.VerseOfTheDayDataKoinModule
import com.caseyjbrooks.votd.domain.VerseOfTheDayDomainKoinModule
import com.caseyjbrooks.votd.schedules.VerseOfTheDaySchedulesKoinModule
import com.caseyjbrooks.votd.screens.VerseOfTheDayScreensKoinModule
import com.caseyjbrooks.votd.widgets.VerseOfTheDayWidgetsKoinModule

public class VerseOfTheDayPillarKoinModule : KoinModule {
    override fun relatedModules(): List<KoinModule> = listOf(
        VerseOfTheDayDataKoinModule(),
        VerseOfTheDayDomainKoinModule(),
        VerseOfTheDaySchedulesKoinModule(),
        VerseOfTheDayScreensKoinModule(),
        VerseOfTheDayWidgetsKoinModule(),
    )
}
