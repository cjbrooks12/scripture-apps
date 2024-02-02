package com.caseyjbrooks.app

import com.caseyjbrooks.api.realApiModule
import com.caseyjbrooks.ballast.realBallastModule
import com.caseyjbrooks.bible.BibleModule
import com.caseyjbrooks.database.realDatabaseModule
import com.caseyjbrooks.datetime.realDateTimeModule
import com.caseyjbrooks.domain.realDomainModule
import com.caseyjbrooks.foryou.ForYouModule
import com.caseyjbrooks.foryou.ui.dashboard.forYouDashboardScreenModule
import com.caseyjbrooks.logging.realLoggingModule
import com.caseyjbrooks.notifications.realNotificationModule
import com.caseyjbrooks.prayer.pillars.PrayerPillar
import com.caseyjbrooks.prayer.pillars.prayerPillarModule
import com.caseyjbrooks.routing.ApplicationScreen
import com.caseyjbrooks.routing.ApplicationStructure
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.Pillar
import com.caseyjbrooks.routing.realRoutingModule
import com.caseyjbrooks.settings.SettingsModule
import com.caseyjbrooks.topicalbible.TopicalBibleModule
import com.caseyjbrooks.verses.pillars.ScriptureMemoryPillar
import com.caseyjbrooks.verses.pillars.versePillarModule
import com.caseyjbrooks.votd.pillars.verseOfTheDayPillarModule
import org.koin.core.module.Module
import org.koin.dsl.module

class AbideApplicationStructure : ApplicationStructure {

    override val pillars: List<Pillar> = listOf(
        ForYouModule,
        PrayerPillar,
        ScriptureMemoryPillar,
        SettingsModule,
        TopicalBibleModule,
        BibleModule,
    )

    override val initialRoute: ApplicationScreen = ForYouModule.mainNavigationItem.route

    override val mainNavigationItems: List<NavigationItem> = listOf(
        ForYouModule.mainNavigationItem,
        ScriptureMemoryPillar.mainNavigationItem,
        PrayerPillar.mainNavigationItem,
        SettingsModule.mainNavigationItem,
    )

    companion object {
        public val koinModule: Module = module {
            // common modules from :core
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

            // modules for this app variant's features
            includes(
                prayerPillarModule,
                versePillarModule,
                verseOfTheDayPillarModule,
                forYouDashboardScreenModule,
            )
        }
    }
}
