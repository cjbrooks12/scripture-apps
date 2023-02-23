package com.caseyjbrooks.scripturenow.models.notifications

public sealed interface NotificationType

public object VerseOfTheDayNotification : NotificationType
public object MemoryVerseNotification : NotificationType
public data class PushNotification(val id: Int) : NotificationType
