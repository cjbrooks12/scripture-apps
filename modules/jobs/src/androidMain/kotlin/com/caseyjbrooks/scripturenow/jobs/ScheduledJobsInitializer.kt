package com.caseyjbrooks.scripturenow.jobs

import android.content.Context
import androidx.startup.Initializer
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer
import com.caseyjbrooks.scripturenow.jobs.votd.VerseOfTheDayPrefetchWorker

class ScheduledJobsInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        println("WorkManagerInitializer.create()")
        val workManager = WorkManager.getInstance(context)

        VerseOfTheDayPrefetchWorker.scheduleWork(workManager)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(
            WorkManagerInitializer::class.java
        )
    }
}
