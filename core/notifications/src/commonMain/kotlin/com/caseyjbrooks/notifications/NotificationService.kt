package com.caseyjbrooks.notifications

public interface NotificationService {
    public fun isPermissionGranted(): Boolean
    public suspend fun promptForPermission()

    public fun showNotification(
        channelId: String,
        notificationId: String,
        title: String,
        message: String,
    )
}
