package com.caseyjbrooks.scripturenow.models.notifications

public sealed interface NotificationContent

public data class BasicNotification(
    val subject: String,
    val message: String,
    val deepLink: String?
) : NotificationContent
