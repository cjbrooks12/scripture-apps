package com.caseyjbrooks.notifications

internal class FakeNotificationService : NotificationService {
    override suspend fun isPermissionGranted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun promptForPermission() {
        TODO("Not yet implemented")
    }

    override fun showNotification(title: String, message: String) {
    }
}
