package com.caseyjbrooks.scripturenow.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri

data class NotificationChannelDescription(
    public val id: String,
    public val name: String,
    public val description: String,
    public val configureForChannel: NotificationCompat.Builder.() -> Unit = { },
)

data class NotificationDescription(
    public val channel: NotificationChannelDescription,
    public val notificationId: Int,
    public val configureForNotification: NotificationCompat.Builder.() -> Unit = { },
)

fun NotificationDescription.cancelNotification(
    applicationContext: Context,
) {
    NotificationManagerCompat
        .from(applicationContext)
        .cancel(notificationId)
}

fun NotificationDescription.showNotification(
    applicationContext: Context,
    deepLinkPath: String,
    configure: NotificationCompat.Builder.() -> Unit = { },
) {

    NotificationManagerCompat
        .from(applicationContext)
        .notify(
            notificationId,
            NotificationCompat
                .Builder(applicationContext, channel.id)
                .setSmallIcon(R.drawable.image_placeholder)
                .setContentIntent(
                    PendingIntent
                        .getActivity(
                            applicationContext,
                            0,
                            Intent(
                                Intent.ACTION_VIEW,
                                "scripture://now$deepLinkPath".toUri()
                            ).apply {
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            },
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                        )
                )
                .apply(channel.configureForChannel)
                .apply(configureForNotification)
                .apply(configure)
                .build()
        )
}
