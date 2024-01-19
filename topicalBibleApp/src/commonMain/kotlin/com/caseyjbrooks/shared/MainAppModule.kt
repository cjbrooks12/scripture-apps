package com.caseyjbrooks.shared

import com.caseyjbrooks.bible.BibleModule
import com.caseyjbrooks.di.CombinedPillar
import com.caseyjbrooks.foryou.ForYouModule
import com.caseyjbrooks.prayer.pillars.PrayerPillar
import com.caseyjbrooks.scripturememory.ScriptureMemoryModule
import com.caseyjbrooks.settings.SettingsModule
import com.caseyjbrooks.topicalbible.TopicalBibleModule

public class MainAppModule : CombinedPillar(
    ForYouModule(),
    ScriptureMemoryModule(),
    PrayerPillar(),
    SettingsModule(),
    TopicalBibleModule(),
    BibleModule(),

    initialRoute = ForYouModule().initialRoute,
)
