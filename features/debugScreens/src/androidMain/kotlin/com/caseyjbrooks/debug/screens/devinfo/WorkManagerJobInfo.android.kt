package com.caseyjbrooks.debug.screens.devinfo

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import com.caseyjbrooks.di.GlobalKoinApplication
import com.copperleaf.ballast.scheduler.workmanager.testScheduleNow
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal actual suspend fun getWorkManagerJobInfo(): List<WorkManagerJobInfo> {
    val context: Context = GlobalKoinApplication.get<Context>().applicationContext
    val workManager = WorkManager.getInstance(context)

    val jobs = workManager
        .getWorkInfos(WorkQuery.fromStates(WorkInfo.State.ENQUEUED))
        .awaitInternal()

    return jobs
        .map { workInfo ->
            WorkManagerJobInfo(
                adapterClassName = workInfo.getStringFromTag("ballast::DATA_ADAPTER_CLASS::", ""),
                callbackClassName = workInfo.getStringFromTag("ballast::DATA_CALLBACK_CLASS::", ""),
                scheduleName = workInfo.getStringFromTag("ballast::DATA_KEY::", ""),
                withHistory = workInfo.getBooleanFromTag("ballast::DATA_WITH_HISTORY::", false),
                initialInstant = workInfo.getLongFromTag("ballast::DATA_INITIAL_INSTANT::", -1L)
                    .takeIf { it != -1L }
                    ?.let { Instant.fromEpochMilliseconds(it) },
                latestInstant = workInfo.getLongFromTag("ballast::DATA_LATEST_INSTANT::", -1L)
                    .takeIf { it != -1L }
                    ?.let { Instant.fromEpochMilliseconds(it) },
                nextInstant = workInfo.getLongFromTag("ballast::DATA_NEXT_INSTANT::", -1L)
                    .takeIf { it != -1L }
                    ?.let { Instant.fromEpochMilliseconds(it) },
            )
        }
}

@Suppress("BlockingMethodInNonBlockingContext", "RedundantSamConstructor")
private suspend inline fun <R : Any> ListenableFuture<R>.awaitInternal(): R {
    // Fast path
    if (isDone) {
        try {
            return get()
        } catch (e: ExecutionException) {
            throw e.cause ?: e
        }
    }
    return suspendCancellableCoroutine { cancellableContinuation ->
        addListener(
            Runnable {
                try {
                    cancellableContinuation.resume(get())
                } catch (throwable: Throwable) {
                    val cause = throwable.cause ?: throwable
                    when (throwable) {
                        is CancellationException -> cancellableContinuation.cancel(cause)
                        else -> cancellableContinuation.resumeWithException(cause)
                    }
                }
            },
            Executor {
                it.run()
            },
        )

        cancellableContinuation.invokeOnCancellation {
            cancel(false)
        }
    }
}

// Get and set values in a worker
// ---------------------------------------------------------------------------------------------------------------------

internal fun WorkInfo.getStringFromTag(property: String, defaultValue: String): String {
    return tags
        .firstOrNull { it.startsWith(property) }
        ?.removePrefix(property)
        ?: defaultValue
}

internal fun WorkInfo.getBooleanFromTag(property: String, defaultValue: Boolean): Boolean {
    return tags
        .firstOrNull { it.startsWith(property) }
        ?.removePrefix(property)
        ?.toBooleanStrictOrNull()
        ?: defaultValue
}

internal fun WorkInfo.getLongFromTag(property: String, defaultValue: Long): Long {
    return tags
        .firstOrNull { it.startsWith(property) }
        ?.removePrefix(property)
        ?.toLongOrNull()
        ?: defaultValue
}

@RequiresApi(Build.VERSION_CODES.O)
internal actual suspend fun testWorkManagerJob(info: WorkManagerJobInfo) {
    val context: Context = GlobalKoinApplication.get<Context>().applicationContext

    testScheduleNow(
        applicationContext = context,
        adapterClassName = info.adapterClassName,
        callbackClassName = info.callbackClassName,
        scheduleKey = info.scheduleName,
        withHistory = info.withHistory,
        initialInstant = info.initialInstant!!,
        latestInstant = Clock.System.now(),
    )
}
