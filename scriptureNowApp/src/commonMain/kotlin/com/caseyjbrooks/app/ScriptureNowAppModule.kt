package com.caseyjbrooks.app

import com.caseyjbrooks.database.realDatabaseModule
import com.caseyjbrooks.di.routingModule
import com.caseyjbrooks.prayer.pillars.prayerPillarModule
import com.caseyjbrooks.prayer.repository.fakePrayerRepositoryModule
import com.caseyjbrooks.prayer.repository.realPrayerRepositoryModule
import org.koin.core.module.Module
import org.koin.dsl.module

public val realScriptureNowAppModule: Module = module {
    includes(
        routingModule,
        realDatabaseModule,
        realPrayerRepositoryModule,
        prayerPillarModule,
    )
}

public val fakeScriptureNowAppModule: Module = module {
    includes(
        routingModule,
        realDatabaseModule,
        fakePrayerRepositoryModule,
        prayerPillarModule,
    )
}
