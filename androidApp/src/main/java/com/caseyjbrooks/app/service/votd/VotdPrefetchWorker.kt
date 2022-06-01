package com.caseyjbrooks.app.service.votd

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.caseyjbrooks.app.MainApplication
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.isLoading
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

class VotdPrefetchWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        Firebase.analytics.logEvent("workStarted") {
            param("worker_name", "Verse of the Day")
            param("worker_class", VotdPrefetchWorker::class.java.name)
        }
        val mainApplication = applicationContext as MainApplication

        val vm = mainApplication.injector.votdViewModel(this)

        val loadingFinishedState = vm
            .observeStates()
            .first { !it.verseOfTheDay.isLoading() }
            .verseOfTheDay

        // when the Repo is no longer loading, we know the VOTD has been cached in the local DB
        when (loadingFinishedState) {
            is Cached.Value<*> -> {
                Firebase.analytics.logEvent("workStarted") {
                    param("worker_name", "Verse of the Day")
                    param("worker_class", VotdPrefetchWorker::class.java.name)
                }
                Result.success()
            }
            is Cached.FetchingFailed<*> -> {
                Firebase.analytics.logEvent("workFailed") {
                    param("worker_name", "Verse of the Day")
                    param("worker_class", VotdPrefetchWorker::class.java.name)
                }
                Firebase.crashlytics.recordException(loadingFinishedState.error)
                Result.failure()
            }
            else -> {
                // these are the loading states, it should never get to this block
                Result.failure()
            }
        }
    }

    companion object {
        fun scheduleWork(application: MainApplication) {
            WorkManager
                .getInstance(application)
                .enqueueUniquePeriodicWork(
                    VotdPrefetchWorker::class.java.simpleName,
                    ExistingPeriodicWorkPolicy.KEEP,
                    PeriodicWorkRequestBuilder<VotdPrefetchWorker>(6, TimeUnit.HOURS)
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
