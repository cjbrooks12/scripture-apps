package com.caseyjbrooks.prayer.schedules

import com.caseyjbrooks.di.GlobalKoinApplication
import com.copperleaf.ballast.scheduler.workmanager.SchedulerCallback
import org.koin.core.qualifier.named

internal class PrayerSchedulerCallback : SchedulerCallback<PrayerSchedulesContract.Inputs> {

    override suspend fun dispatchInput(input: PrayerSchedulesContract.Inputs) {
        val vm: PrayerSchedulesViewModel = GlobalKoinApplication.koinApplication.koin
            .get(named("PrayerSchedulesViewModel"))
        vm.sendAndAwaitCompletion(input)
    }
}
