package com.caseyjbrooks.app

import com.caseyjbrooks.api.realApiModule
import com.caseyjbrooks.ballast.realBallastModule
import com.caseyjbrooks.database.realDatabaseModule
import com.caseyjbrooks.datetime.realDateTimeModule
import com.caseyjbrooks.domain.realDomainModule
import com.caseyjbrooks.foryou.ui.dashboard.forYouDashboardScreenModule
import com.caseyjbrooks.logging.realLoggingModule
import com.caseyjbrooks.notifications.realNotificationModule
import com.caseyjbrooks.prayer.pillars.prayerPillarModule
import com.caseyjbrooks.routing.realRoutingModule
import com.caseyjbrooks.votd.pillars.verseOfTheDayPillarModule
import org.koin.core.module.Module
import org.koin.dsl.module

public val commonApplicationModule: Module = module {
    includes(
        realApiModule,
        realBallastModule,
        realDatabaseModule,
        realDateTimeModule,
        realDomainModule,
        realLoggingModule,
        realNotificationModule,
        realRoutingModule,
    )
}

public val realScriptureNowAppModule: Module = module {
    includes(
        prayerPillarModule,
        verseOfTheDayPillarModule,
        forYouDashboardScreenModule,
    )
}
