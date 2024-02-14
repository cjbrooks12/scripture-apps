package com.caseyjbrooks.prayer.utils

import com.caseyjbrooks.database.DatabaseKoinModule
import com.caseyjbrooks.database.HardcodedUuidFactory
import com.caseyjbrooks.di.Variant
import com.caseyjbrooks.di.getModulesForVariant
import com.caseyjbrooks.domain.DomainKoinModule
import com.caseyjbrooks.logging.LoggingKoinModule
import com.caseyjbrooks.prayer.PrayerDataKoinModule
import com.caseyjbrooks.prayer.domain.PrayerDomainKoinModule
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
        val variant = Variant(Variant.Environment.Test, Variant.BuildType.Debug)
        modules(
            *DatabaseKoinModule().getModulesForVariant(variant).toTypedArray(),
            *DomainKoinModule().getModulesForVariant(variant).toTypedArray(),
            *LoggingKoinModule().getModulesForVariant(variant).toTypedArray(),
            *PrayerDataKoinModule().getModulesForVariant(variant).toTypedArray(),
            *PrayerDomainKoinModule().getModulesForVariant(variant).toTypedArray(),
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
