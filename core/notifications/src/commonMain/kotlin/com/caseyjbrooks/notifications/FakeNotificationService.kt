package com.caseyjbrooks.notifications

internal class FakeNotificationService : NotificationService {
    override fun isPermissionGranted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun promptForPermission() {
        TODO("Not yet implemented")
    }

    override fun showNotification(
        channelId: String,
        notificationId: String,
        title: String,
        message: String,
    ) {
    }
}
