package com.caseyjbrooks.scripturenow.repositories.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_DEFAULT
import androidx.core.net.toUri
import com.caseyjbrooks.scripturenow.models.notifications.*

val NotificationType.channelId: String
    get() = when (this) {
        is MemoryVerseNotification -> "memory verse"
        is PushNotification -> ""
        is VerseOfTheDayNotification -> ""
    }

val NotificationType.channelName: String
    get() = when (this) {
        is MemoryVerseNotification -> "Memory Verse"
        is PushNotification -> ""
        is VerseOfTheDayNotification -> ""
    }

val NotificationType.channelDescription: String
    get() = when (this) {
        is MemoryVerseNotification -> "Show an ongoing notification with your main memory verse"
        is PushNotification -> ""
        is VerseOfTheDayNotification -> ""
    }

val NotificationType.importance: Int
    get() = when (this) {
        is MemoryVerseNotification -> IMPORTANCE_DEFAULT
        is PushNotification -> IMPORTANCE_DEFAULT
        is VerseOfTheDayNotification -> IMPORTANCE_DEFAULT
    }

val NotificationType.notificationId: Int
    get() = when (this) {
        is MemoryVerseNotification -> 1
        is PushNotification -> 2
        is VerseOfTheDayNotification -> 3
    }

// Notification Builder Utils
// ---------------------------------------------------------------------------------------------------------------------

public fun NotificationCompat.Builder.applyNotificationType(
    notificationType: NotificationType
): NotificationCompat.Builder = when (notificationType) {
    is MemoryVerseNotification -> applyMemoryVerseNotificationType()
    is PushNotification -> applyPushNotificationType()
    is VerseOfTheDayNotification -> applyVerseOfTheDayNotificationType()
}

public fun NotificationCompat.Builder.applyMemoryVerseNotificationType(): NotificationCompat.Builder = apply {
    setPriority(NotificationCompat.PRIORITY_LOW)
    setCategory(NotificationCompat.CATEGORY_STATUS)
    setOnlyAlertOnce(true)
    setOngoing(true)
    setSilent(true)
}

public fun NotificationCompat.Builder.applyPushNotificationType(): NotificationCompat.Builder = apply {
    setOngoing(false)
    setOnlyAlertOnce(false)
}

public fun NotificationCompat.Builder.applyVerseOfTheDayNotificationType(): NotificationCompat.Builder = apply {
    setOngoing(false)
    setOnlyAlertOnce(false)
}

public fun NotificationCompat.Builder.applyNotificationContent(
    applicationContext: Context,
    notification: NotificationContent
): NotificationCompat.Builder = when (notification) {
    is BasicNotification -> applyBasicNotificationContent(applicationContext, notification)
}

private fun NotificationCompat.Builder.applyBasicNotificationContent(
    applicationContext: Context,
    notification: BasicNotification
): NotificationCompat.Builder = apply {
    setContentTitle(notification.subject)
    setContentText(notification.message)
    setStyle(
        NotificationCompat
            .BigTextStyle()
            .bigText(notification.message)
    )
    if (notification.deepLink != null) {
        addDeepLink(applicationContext, notification.deepLink)
    }
}

public fun NotificationCompat.Builder.addDeepLink(
    applicationContext: Context,
    deepLinkPath: String?,
): NotificationCompat.Builder = apply {
    if (deepLinkPath != null) {
        setContentIntent(
            PendingIntent
                .getActivity(
                    applicationContext,
                    0,
                    Intent(
                        Intent.ACTION_VIEW,
                        "scripture://now${deepLinkPath}".toUri()
                    ).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
        )
    }
}
