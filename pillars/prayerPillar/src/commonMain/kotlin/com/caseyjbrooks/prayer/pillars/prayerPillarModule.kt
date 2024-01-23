package com.caseyjbrooks.prayer.pillars

import com.caseyjbrooks.prayer.domain.prayerDomainModule
import com.caseyjbrooks.prayer.realPrayerDataModule
import com.caseyjbrooks.prayer.schedules.prayerSchedulesModule
import com.caseyjbrooks.prayer.screens.prayerScreensModule
import org.koin.core.module.Module
import org.koin.dsl.module

public val prayerPillarModule: Module = module {
    includes(
        realPrayerDataModule,
        prayerDomainModule,
        prayerScreensModule,
        prayerSchedulesModule,
    )
}
