package com.caseyjbrooks.scripturenow.repositories.notifications

import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.models.notifications.NotificationContent
import com.caseyjbrooks.scripturenow.models.notifications.NotificationType
import com.copperleaf.ballast.repository.cache.Cached

public object NotificationsRepositoryContract {
    public data class State(
        public val memoryVerse: Cached<MemoryVerse> = Cached.NotLoaded(),
        public val showMemoryVerse: Boolean = false,
    )

    public sealed class Inputs {
        public object Initialize : Inputs()

        public data class MemoryVerseChanged(val memoryVerse: Cached<MemoryVerse>) : Inputs()
        public data class ShowMemoryVerseNotificationSettingChanged(val showMemoryVerse: Boolean) : Inputs()
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
