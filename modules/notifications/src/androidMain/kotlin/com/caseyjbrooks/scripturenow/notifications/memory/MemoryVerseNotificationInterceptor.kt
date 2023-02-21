package com.caseyjbrooks.scripturenow.notifications.memory

import android.content.Context
import androidx.core.app.NotificationCompat
import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.notifications.NotificationChannelDescription
import com.caseyjbrooks.scripturenow.notifications.NotificationDescription
import com.caseyjbrooks.scripturenow.notifications.cancelNotification
import com.caseyjbrooks.scripturenow.notifications.showNotification
import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepositoryContract
import com.caseyjbrooks.scripturenow.utils.referenceText
import com.copperleaf.ballast.*
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.path
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

    private fun removeNotification() {
        notificationDescription.cancelNotification(applicationContext)
    }

    private fun showNotification(memoryVerse: MemoryVerse) {
        notificationDescription.showNotification(
            applicationContext = applicationContext,
            deepLinkPath = ScriptureNowRoute.MemoryVerseDetails
                .directions()
                .path(memoryVerse.uuid.toString())
                .build(),
        ) {
            setContentTitle(memoryVerse.reference.referenceText)
            setContentText(memoryVerse.text)
            setStyle(
                NotificationCompat
                    .BigTextStyle()
                    .bigText(memoryVerse.text)
            )
        }
    }

    companion object {
        private val notificationDescription = NotificationDescription(
            channel = NotificationChannelDescription(
                id = "memory verse",
                name = "Memory Verse",
                description = "Show an ongoing notification with your main memory verse",
            ),
            notificationId = 1,
        ) {
            setPriority(NotificationCompat.PRIORITY_LOW)
            setCategory(NotificationCompat.CATEGORY_STATUS)
            setOnlyAlertOnce(true)
            setOngoing(true)
            setSilent(true)
        }
    }
}
