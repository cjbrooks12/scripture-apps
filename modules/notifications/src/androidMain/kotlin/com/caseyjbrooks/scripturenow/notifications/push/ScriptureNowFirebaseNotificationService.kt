package com.caseyjbrooks.scripturenow.notifications.push

import com.caseyjbrooks.scripturenow.repositories.RepositoriesInjectorProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ScriptureNowFirebaseNotificationService : FirebaseMessagingService() {
    private val injector
        get() = (applicationContext as RepositoriesInjectorProvider).getRepositoriesInjector()

    override fun onNewToken(token: String) {
        injector.getAuthRepository().firebaseTokenUpdated(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            injector.getNotificationsRepository().showPushNotification(
                title = it.title ?: "",
                body = it.body ?: "",
            )
        }
    }
}
