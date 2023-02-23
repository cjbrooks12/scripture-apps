package com.caseyjbrooks.scripturenow.repositories.notifications

import android.app.NotificationChannel
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.caseyjbrooks.scripturenow.notifications.R
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

public class AndroidNotificationsEventHandler(
    private val applicationContext: Context,
) : EventHandler<
        NotificationsRepositoryContract.Inputs,
        NotificationsRepositoryContract.Events,
        NotificationsRepositoryContract.State> {
    override suspend fun EventHandlerScope<
            NotificationsRepositoryContract.Inputs,
            NotificationsRepositoryContract.Events,
            NotificationsRepositoryContract.State>.handleEvent(
        event: NotificationsRepositoryContract.Events
    ) = when (event) {
        is NotificationsRepositoryContract.Events.ShowNotification -> {
            // create the notification channel
            NotificationManagerCompat
                .from(applicationContext)
                .createNotificationChannel(
                    NotificationChannel(
                        event.type.channelId,
                        event.type.channelName,
                        event.type.importance,
                    ).also {
                        it.description = event.type.channelDescription
                    }
                )

            // show the notification in that channel
            NotificationManagerCompat
                .from(applicationContext)
                .notify(
                    1,
                    NotificationCompat
                        .Builder(applicationContext, event.type.channelId)
                        .setSmallIcon(R.drawable.image_placeholder)
                        .applyNotificationType(event.type)
                        .applyNotificationContent(applicationContext, event.content)
                        .build()
                )
        }

        is NotificationsRepositoryContract.Events.HideNotification -> {
            NotificationManagerCompat
                .from(applicationContext)
                .cancel(event.notificationType.notificationId)
        }
    }
}
