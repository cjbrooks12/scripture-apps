package com.caseyjbrooks.prayer.screens

import com.caseyjbrooks.prayer.screens.detail.prayerDetailScreenModule
import com.caseyjbrooks.prayer.screens.form.prayerFormScreenModule
import com.caseyjbrooks.prayer.screens.list.prayerListScreenModule
import com.caseyjbrooks.prayer.screens.timer.prayerTimerScreenModule
import org.koin.core.module.Module
import org.koin.dsl.module

public val prayerScreensModule: Module = module {
    includes(
        prayerDetailScreenModule,
        prayerFormScreenModule,
        prayerListScreenModule,
        prayerTimerScreenModule,
    )
}
