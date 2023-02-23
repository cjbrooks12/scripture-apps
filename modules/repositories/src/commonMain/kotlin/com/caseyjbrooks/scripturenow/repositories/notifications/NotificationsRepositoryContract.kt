package com.caseyjbrooks.scripturenow.repositories.notifications

import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.models.notifications.NotificationContent
import com.caseyjbrooks.scripturenow.models.notifications.NotificationType

public object NotificationsRepositoryContract {
    public class State()

    public sealed class Inputs {
        public object Initialize : Inputs()

        public data class ShowMemoryVerseNotification(val memoryVerse: MemoryVerse) : Inputs()
        public object HideMemoryVerseNotification : Inputs()

        public object ShowVerseOfTheDayNotification : Inputs()
        public data class ShowPushNotification(
            val title: String,
            val body: String,
        ) : Inputs()
    }

    public sealed class Events {
        public data class ShowNotification(
            val type: NotificationType,
            val content: NotificationContent,
        ) : Events()

        public data class HideNotification(
            val notificationType: NotificationType,
        ) : Events()
    }
}
