package com.caseyjbrooks.app

import com.caseyjbrooks.database.realDatabaseModule
import com.caseyjbrooks.datetime.realDateTimeModule
import com.caseyjbrooks.di.routingModule
import com.caseyjbrooks.logging.loggingModule
import com.caseyjbrooks.prayer.pillars.prayerPillarModule
import com.caseyjbrooks.prayer.pillars.verseOfTheDayPillarModule
import org.koin.core.module.Module
import org.koin.dsl.module

public val commonApplicationModule: Module = module {
    includes(
        routingModule,
        realDateTimeModule,
        loggingModule,
        realDatabaseModule,
    )
}


public val realScriptureNowAppModule: Module = module {
    includes(
        prayerPillarModule,
        verseOfTheDayPillarModule,
    )
}
