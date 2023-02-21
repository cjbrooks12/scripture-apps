package com.caseyjbrooks.scripturenow.jobs.votd

import android.content.Context
import androidx.work.*
import com.caseyjbrooks.scripturenow.repositories.RepositoriesInjectorProvider
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.awaitValue
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.TimeUnit

class VerseOfTheDayPrefetchWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        val result = (applicationContext as RepositoriesInjectorProvider)
            .getRepositoriesInjector()
            .getVerseOfTheDayRepository()
            .getCurrentVerseOfTheDay()
            .awaitValue()

        // when the Repo is no longer loading, we know the VOTD has been cached in the local DB
        when (result) {
            is Cached.Value<*> -> {
                Result.success()
            }

            is Cached.FetchingFailed<*> -> {
                Result.failure()
            }

            else -> {
                // these are the loading states, it should never get to this block
                Result.failure()
            }
        }
    }

    companion object {
        fun scheduleWork(workManager: WorkManager) {
            workManager
                .enqueueUniquePeriodicWork(
                    VerseOfTheDayPrefetchWorker::class.java.simpleName,
                    ExistingPeriodicWorkPolicy.KEEP,
                    PeriodicWorkRequestBuilder<VerseOfTheDayPrefetchWorker>(6, TimeUnit.HOURS)
                        .setConstraints(
                            Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.UNMETERED)
                                .setRequiresCharging(false)
                                .build()
                        )
                        .build()
                )
        }
    }
}
