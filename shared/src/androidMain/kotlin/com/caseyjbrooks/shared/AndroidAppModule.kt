package com.caseyjbrooks.shared

import com.caseyjbrooks.database.androidDatabaseModule
import com.caseyjbrooks.database.databaseModule
import com.caseyjbrooks.di.routingModule
import com.caseyjbrooks.prayer.pillars.prayerPillarModule
import com.caseyjbrooks.prayer.repository.fakePrayerRepositoryModule
import com.caseyjbrooks.prayer.repository.realPrayerRepositoryModule
import org.koin.core.module.Module
import org.koin.dsl.module

public val realAndroidApplicationModule: Module = module {
    includes(
        routingModule,
        databaseModule,
        androidDatabaseModule,
        realPrayerRepositoryModule,
        prayerPillarModule,
    )
}

public val fakeAndroidApplicationModule: Module = module {
    includes(
        routingModule,
        databaseModule,
        androidDatabaseModule,
        fakePrayerRepositoryModule,
        prayerPillarModule,
    )
}
