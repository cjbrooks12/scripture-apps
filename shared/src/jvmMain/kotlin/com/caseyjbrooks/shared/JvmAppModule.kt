package com.caseyjbrooks.shared

import com.caseyjbrooks.database.databaseModule
import com.caseyjbrooks.database.jvmDatabaseModule
import com.caseyjbrooks.prayer.pillars.prayerPillarModule
import com.caseyjbrooks.prayer.repository.fakePrayerRepositoryModule
import com.caseyjbrooks.prayer.repository.realPrayerRepositoryModule
import org.koin.core.module.Module
import org.koin.dsl.module

public val realJvmApplicationModule: Module = module {
    includes(
        databaseModule,
        jvmDatabaseModule,
        realPrayerRepositoryModule,
        prayerPillarModule,
    )
}

public val fakeJvmApplicationModule: Module = module {
    includes(
        databaseModule,
        jvmDatabaseModule,
        fakePrayerRepositoryModule,
        prayerPillarModule,
    )
}
