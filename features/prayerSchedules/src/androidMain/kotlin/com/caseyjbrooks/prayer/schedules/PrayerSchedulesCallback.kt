package com.caseyjbrooks.prayer.schedules

import com.caseyjbrooks.di.GlobalKoinApplication
import com.copperleaf.ballast.scheduler.workmanager.SchedulerCallback

internal class PrayerSchedulesCallback : SchedulerCallback<PrayerSchedulesContract.Inputs> {

    override suspend fun dispatchInput(input: PrayerSchedulesContract.Inputs) {
        val vm: PrayerSchedulesViewModel = GlobalKoinApplication.get()
        vm.sendAndAwaitCompletion(input)
    }
}
