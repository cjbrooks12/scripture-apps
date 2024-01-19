package com.caseyjbrooks.app

import com.caseyjbrooks.bible.BibleModule
import com.caseyjbrooks.di.ApplicationStructure
import com.caseyjbrooks.di.Pillar
import com.caseyjbrooks.foryou.ForYouModule
import com.caseyjbrooks.prayer.pillars.PrayerPillar
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.caseyjbrooks.scripturememory.ScriptureMemoryModule
import com.caseyjbrooks.settings.SettingsModule
import com.caseyjbrooks.topicalbible.TopicalBibleModule

class ScriptureNowApplicationStructure : ApplicationStructure {

    override val pillars: List<Pillar> = listOf(
        ForYouModule,
        ScriptureMemoryModule,
        PrayerPillar,
        SettingsModule,
        TopicalBibleModule,
        BibleModule,
    )

    override val initialRoute: ScriptureNowScreen = ForYouModule.mainNavigationItem.route

    override val mainNavigationItems: List<NavigationItem> = listOf(
        ForYouModule.mainNavigationItem,
        ScriptureMemoryModule.mainNavigationItem,
        PrayerPillar.mainNavigationItem,
        SettingsModule.mainNavigationItem,
    )
}
