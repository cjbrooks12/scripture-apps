package com.caseyjbrooks.scripturenow.notifications.push

import android.util.Log
import androidx.core.app.NotificationCompat
import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.notifications.NotificationChannelDescription
import com.caseyjbrooks.scripturenow.notifications.NotificationDescription
import com.caseyjbrooks.scripturenow.notifications.showNotification
import com.caseyjbrooks.scripturenow.repositories.RepositoriesInjectorProvider
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ScriptureNowFirebaseNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        val mainInjector = (applicationContext as RepositoriesInjectorProvider).getRepositoriesInjector()
        mainInjector.getAuthRepository().firebaseTokenUpdated(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(it)
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage.Notification) {
        notificationDescription.showNotification(
            applicationContext,
            deepLinkPath = ScriptureNowRoute.Home
                .directions()
                .build(),
        ) {
            remoteMessage.title?.let { setContentTitle(it) }
            remoteMessage.body?.let { setContentText(it) }
            setAutoCancel(true)
        }
    }

    companion object {
        internal const val TAG = "SN FB Notifications"
        private val notificationDescription = NotificationDescription(
            channel = NotificationChannelDescription(
                id = "scripture now push",
                name = "Push Notifications",
                description = "Scripture Now Push Notifications",
            ),
            notificationId = 1,
        ) {
            setPriority(NotificationCompat.PRIORITY_DEFAULT)
            setCategory(NotificationCompat.CATEGORY_PROMO)
        }
    }
}
