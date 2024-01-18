package com.caseyjbrooks.shared

import com.caseyjbrooks.bible.BibleModule
import com.caseyjbrooks.di.CombinedFeatureModule
import com.caseyjbrooks.foryou.ForYouModule
import com.caseyjbrooks.prayer.pillars.PrayerModule
import com.caseyjbrooks.scripturememory.ScriptureMemoryModule
import com.caseyjbrooks.settings.SettingsModule
import com.caseyjbrooks.topicalbible.TopicalBibleModule

public class MainAppModule : CombinedFeatureModule(
    ForYouModule(),
    ScriptureMemoryModule(),
    PrayerModule(),
    SettingsModule(),
    TopicalBibleModule(),
    BibleModule(),

    initialRoute = ForYouModule().initialRoute,
)
