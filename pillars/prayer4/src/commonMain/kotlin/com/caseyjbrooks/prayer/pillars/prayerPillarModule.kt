package com.caseyjbrooks.prayer.pillars

import com.caseyjbrooks.prayer.domain.prayerDomainModule
import com.caseyjbrooks.prayer.domain.realPrayerDomainModule
import com.caseyjbrooks.prayer.repository.realPrayerRepositoryModule
import com.caseyjbrooks.prayer.screens.prayerScreensModule
import org.koin.core.module.Module
import org.koin.dsl.module

public val prayerPillarModule: Module = module {
    includes(
        realPrayerRepositoryModule,
        prayerDomainModule,
        realPrayerDomainModule,
        prayerScreensModule,
    )
}
