package com.caseyjbrooks.prayer.schedules

import androidx.work.OneTimeWorkRequest
import androidx.work.OutOfQuotaPolicy
import com.caseyjbrooks.di.GlobalKoinApplication
import com.copperleaf.ballast.scheduler.workmanager.SchedulerCallback

internal class PrayerSchedulesCallback : SchedulerCallback<PrayerSchedulesContract.Inputs> {

    override suspend fun dispatchInput(input: PrayerSchedulesContract.Inputs) {
        val vm: PrayerSchedulesViewModel = GlobalKoinApplication.get()
        vm.sendAndAwaitCompletion(input)
    }

    override fun configureWorkRequest(builder: OneTimeWorkRequest.Builder): OneTimeWorkRequest.Builder {
        return builder
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
    }
}
