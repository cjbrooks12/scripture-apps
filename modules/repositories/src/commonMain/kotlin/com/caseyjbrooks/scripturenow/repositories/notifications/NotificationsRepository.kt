package com.caseyjbrooks.scripturenow.repositories.notifications

public interface NotificationsRepository {
    public fun showPushNotification(
        title: String,
        body: String,
    )
}
