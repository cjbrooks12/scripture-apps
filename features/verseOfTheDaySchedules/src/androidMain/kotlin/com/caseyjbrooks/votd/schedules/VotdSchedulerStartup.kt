package com.caseyjbrooks.votd.schedules

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.startup.Initializer
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer
import com.copperleaf.ballast.scheduler.workmanager.scheduleWork

@RequiresApi(Build.VERSION_CODES.O)
public class VotdSchedulerStartup : Initializer<Unit> {
    override fun create(context: Context) {
        Log.d("Scripture Now", "Running VotdSchedulerStartup")
        val workManager = WorkManager.getInstance(context)

        workManager.scheduleWork(
            VotdSchedulerAdapter(),
            VotdSchedulerCallback(),
            false
        )
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(WorkManagerInitializer::class.java)
    }
}
