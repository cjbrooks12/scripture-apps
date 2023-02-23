package com.caseyjbrooks.scripturenow.repositories.notifications

public fun interface NotificationsRepositoryProvider {
    public fun getNotificationsRepository(): NotificationsRepository
}
