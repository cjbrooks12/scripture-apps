package com.caseyjbrooks.prayer.utils

import com.caseyjbrooks.database.fakeDatabaseModule
import com.caseyjbrooks.di.routingModule
import com.caseyjbrooks.prayer.domain.prayerDomainModule
import com.caseyjbrooks.prayer.models.DailyPrayer
import com.caseyjbrooks.prayer.models.PrayerUser
import com.caseyjbrooks.prayer.repository.fakePrayerRepositoryModule
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import org.koin.core.Koin
import org.koin.dsl.bind
import org.koin.dsl.koinApplication
import org.koin.dsl.module

inline fun koinTest(
    prayerUser: PrayerUser? = null,
    dailyPrayer: DailyPrayer? = null,
    block: Koin.() -> Unit
) {
    val koinApp = koinApplication {
        modules(
            routingModule,
            fakeDatabaseModule,
            fakePrayerRepositoryModule,
            prayerDomainModule,
            module {
                factory<PrayerUser?> { prayerUser }
                factory<DailyPrayer?> { dailyPrayer }
                single<TimeZone> { TimeZone.UTC }
                single<TestClock> { TestClock() }.bind<Clock>()
            }
        )
    }
    try {
        block(koinApp.koin)
    } finally {
        koinApp.close()
    }
}
