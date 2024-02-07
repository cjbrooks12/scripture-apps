package com.caseyjbrooks.app

import com.caseyjbrooks.api.ApiKoinModule
import com.caseyjbrooks.ballast.BallastKoinModule
import com.caseyjbrooks.database.DatabaseKoinModule
import com.caseyjbrooks.datetime.DatetimeKoinModule
import com.caseyjbrooks.di.KoinModule
import com.caseyjbrooks.domain.DomainKoinModule
import com.caseyjbrooks.foryou.ui.dashboard.ForYouDashboardKoinModule
import com.caseyjbrooks.logging.LoggingKoinModule
import com.caseyjbrooks.notifications.NotificationsKoinModule
import com.caseyjbrooks.prayer.pillars.PrayerPillarKoinModule
import com.caseyjbrooks.routing.ApplicationStructure
import com.caseyjbrooks.routing.RoutingKoinModule
import com.caseyjbrooks.verses.pillars.ScriptureMemoryPillarKoinModule
import com.caseyjbrooks.votd.pillars.VerseOfTheDayPillarKoinModule
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public class AbideApplicationKoinModule(
    private val applicationCoroutineScope: CoroutineScope
) : KoinModule {
    override fun relatedModules(): List<KoinModule> = listOf(
        // common modules from :core
        ApiKoinModule(),
        BallastKoinModule(),
        DatabaseKoinModule(),
        DatetimeKoinModule(),
        DomainKoinModule(),
        LoggingKoinModule(),
        NotificationsKoinModule(),
        RoutingKoinModule(),

        // modules for this app variant's features
        PrayerPillarKoinModule(),
        ScriptureMemoryPillarKoinModule(),
        VerseOfTheDayPillarKoinModule(),
        ForYouDashboardKoinModule(),
    )

    override fun mainModule(): Module = module {
        single<CoroutineScope> { applicationCoroutineScope }
        singleOf(::AbideApplicationStructure).bind<ApplicationStructure>()
    }
}
