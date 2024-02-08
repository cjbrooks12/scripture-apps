package com.caseyjbrooks.app

import com.caseyjbrooks.bible.BibleModule
import com.caseyjbrooks.debug.screens.DebugScreensModule
import com.caseyjbrooks.foryou.ForYouModule
import com.caseyjbrooks.prayer.pillars.PrayerPillar
import com.caseyjbrooks.routing.ApplicationScreen
import com.caseyjbrooks.routing.ApplicationStructure
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.Pillar
import com.caseyjbrooks.settings.SettingsModule
import com.caseyjbrooks.topicalbible.TopicalBibleModule
import com.caseyjbrooks.verses.pillars.ScriptureMemoryPillar

class AbideApplicationStructure : ApplicationStructure {

    override val pillars: List<Pillar> = listOf(
        ForYouModule,
        PrayerPillar,
        ScriptureMemoryPillar,
        SettingsModule,
        TopicalBibleModule,
        BibleModule,
        DebugScreensModule,
    )

    override val initialRoute: ApplicationScreen = ForYouModule.mainNavigationItem.route

    override val mainNavigationItems: List<NavigationItem> = listOf(
        ForYouModule.mainNavigationItem,
        ScriptureMemoryPillar.mainNavigationItem,
        PrayerPillar.mainNavigationItem,
        SettingsModule.mainNavigationItem,
    )
}
