package com.caseyjbrooks.scripturenow.notifications.memory

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepositoryContract
import com.caseyjbrooks.scripturenow.utils.referenceText
import com.copperleaf.ballast.*
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

public class MemoryVerseNotificationInterceptor(
    private val applicationContext: Context,
) : BallastInterceptor<
        MemoryVerseRepositoryContract.Inputs,
        MemoryVerseRepositoryContract.Events,
        MemoryVerseRepositoryContract.State> {

    final override fun BallastInterceptorScope<
            MemoryVerseRepositoryContract.Inputs,
            MemoryVerseRepositoryContract.Events,
            MemoryVerseRepositoryContract.State>.start(
        notifications: Flow<BallastNotification<
                MemoryVerseRepositoryContract.Inputs,
                MemoryVerseRepositoryContract.Events,
                MemoryVerseRepositoryContract.State>>
    ) {
        launch(start = CoroutineStart.UNDISPATCHED) {
            notifications.awaitViewModelStart()
            notifications
                .states { it }
                .map { it.mainMemoryVerse.getCachedOrNull() }
                .distinctUntilChanged()
                .onEach {
                    if (it != null) {
                        showNotification(it)
                    } else {
                        removeNotification()
                    }
                }
                .collect()
        }
    }

    private fun BallastInterceptorScope<
            MemoryVerseRepositoryContract.Inputs,
            MemoryVerseRepositoryContract.Events,
            MemoryVerseRepositoryContract.State>.removeNotification() {
        logger.debug("Removing Notification")
        NotificationManagerCompat
            .from(applicationContext)
            .cancel(MEMORY_VERSE_NOTIFICATION_ID)
    }

    private fun BallastInterceptorScope<
            MemoryVerseRepositoryContract.Inputs,
            MemoryVerseRepositoryContract.Events,
            MemoryVerseRepositoryContract.State>.showNotification(memoryVerse: MemoryVerse) {
        logger.debug("Updating Notification")

        // create the notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            applicationContext
                .getSystemService(NOTIFICATION_SERVICE)
                .let { it as NotificationManager }
                .createNotificationChannel(
                    NotificationChannel(
                        MEMORY_VERSE_NOTIFICATION_CHANNEL,
                        "Memory Verse",
                        NotificationManager.IMPORTANCE_DEFAULT,
                    )
                        .apply { description = "Show an ongoing notification with your main memory verse" }
                )
        }

        // show the notification in that channel
        NotificationManagerCompat
            .from(applicationContext)
            .notify(
                MEMORY_VERSE_NOTIFICATION_ID,
                NotificationCompat
                    .Builder(applicationContext, MEMORY_VERSE_NOTIFICATION_CHANNEL)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_STATUS)
                    .setOnlyAlertOnce(true)
                    .setOngoing(true)
                    .setSmallIcon(android.R.drawable.ic_notification_clear_all)
                    .setContentTitle(memoryVerse.reference.referenceText)
                    .setContentText(memoryVerse.text)
                    .setStyle(
                        NotificationCompat
                            .BigTextStyle()
                            .bigText(memoryVerse.text)
                    )
                    .build()
            )
    }

    companion object {
        const val MEMORY_VERSE_NOTIFICATION_ID = 1
        const val MEMORY_VERSE_NOTIFICATION_CHANNEL = "memory verse"
    }
}
