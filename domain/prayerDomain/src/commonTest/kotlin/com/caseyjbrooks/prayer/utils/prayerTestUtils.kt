package com.caseyjbrooks.prayer.utils

import com.caseyjbrooks.database.HardcodedUuidFactory
import com.caseyjbrooks.database.fakeDatabaseModule
import com.caseyjbrooks.prayer.domain.prayerDomainModule
import com.caseyjbrooks.prayer.fakePrayerDataModule
import com.caseyjbrooks.prayer.models.DailyPrayer
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.PrayerUser
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
            fakeDatabaseModule,
            fakePrayerDataModule,
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

fun PrayerId(id: Int): PrayerId {
    return PrayerId(
        HardcodedUuidFactory(id).getNewUuid()
    )
}
