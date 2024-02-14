package com.caseyjbrooks.prayer.schedules

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.startup.Initializer
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer
import com.copperleaf.ballast.scheduler.workmanager.syncSchedulesOnStartup

@RequiresApi(Build.VERSION_CODES.O)
public class PrayerSchedulesStartup : Initializer<Unit> {
    override fun create(context: Context) {
        Log.d("Abide", "Running PrayerSchedulerStartup")
        val workManager = WorkManager.getInstance(context)

        // sync the schedules anytime the app is opened, to ensure things are handled correctly immediately
        workManager.syncSchedulesOnStartup(
            adapter = PrayerSchedulesAdapter(),
            callback = PrayerSchedulesCallback(),
            withHistory = false,
        )
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(WorkManagerInitializer::class.java)
    }
}
