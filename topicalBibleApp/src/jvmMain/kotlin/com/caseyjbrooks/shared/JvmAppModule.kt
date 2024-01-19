package com.caseyjbrooks.shared

import com.caseyjbrooks.database.jvmDatabaseModule
import com.caseyjbrooks.database.realDatabaseModule
import com.caseyjbrooks.prayer.pillars.prayerPillarModule
import com.caseyjbrooks.prayer.repository.fakePrayerRepositoryModule
import com.caseyjbrooks.prayer.repository.realPrayerRepositoryModule
import org.koin.core.module.Module
import org.koin.dsl.module

public val realJvmApplicationModule: Module = module {
    includes(
        realDatabaseModule,
        jvmDatabaseModule,
        realPrayerRepositoryModule,
        prayerPillarModule,
    )
}

public val fakeJvmApplicationModule: Module = module {
    includes(
        realDatabaseModule,
        jvmDatabaseModule,
        fakePrayerRepositoryModule,
        prayerPillarModule,
    )
}
