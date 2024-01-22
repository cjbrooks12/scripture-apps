package com.caseyjbrooks.notifications

public interface NotificationService {
    public suspend fun isPermissionGranted(): Boolean
    public fun promptForPermission()

    public fun showNotification(title: String, message: String)
}
