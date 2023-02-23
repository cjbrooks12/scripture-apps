package com.caseyjbrooks.scripturenow.repositories.notifications

import com.caseyjbrooks.scripturenow.models.notifications.BasicNotification
import com.caseyjbrooks.scripturenow.models.notifications.MemoryVerseNotification
import com.caseyjbrooks.scripturenow.models.notifications.PushNotification
import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepository
import com.caseyjbrooks.scripturenow.utils.referenceText
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.path
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

public class NotificationsRepositoryInputHandler(
    private val memoryVerseRepository: MemoryVerseRepository,
) : InputHandler<
        NotificationsRepositoryContract.Inputs,
        NotificationsRepositoryContract.Events,
        NotificationsRepositoryContract.State> {
    override suspend fun InputHandlerScope<
            NotificationsRepositoryContract.Inputs,
            NotificationsRepositoryContract.Events,
            NotificationsRepositoryContract.State>.handleInput(
        input: NotificationsRepositoryContract.Inputs
    ): Unit = when (input) {
        is NotificationsRepositoryContract.Inputs.Initialize -> {
            observeFlows(
                "memory verse",
                memoryVerseRepository
                    .getMainVerse(false)
                    .map { it.getCachedOrNull() }
                    .distinctUntilChanged()
                    .map {
                        if (it != null) {
                            NotificationsRepositoryContract.Inputs.ShowMemoryVerseNotification(it)
                        } else {
                            NotificationsRepositoryContract.Inputs.HideMemoryVerseNotification
                        }
                    },
            )
        }

        is NotificationsRepositoryContract.Inputs.ShowMemoryVerseNotification -> {
            postEvent(
                NotificationsRepositoryContract.Events.ShowNotification(
                    type = MemoryVerseNotification,
                    content = BasicNotification(
                        subject = input.memoryVerse.reference.referenceText,
                        message = input.memoryVerse.text,
                        deepLink = ScriptureNowRoute.MemoryVerseDetails
                            .directions()
                            .path(input.memoryVerse.uuid.toString())
                            .build(),
                    ),
                )
            )
        }

        is NotificationsRepositoryContract.Inputs.HideMemoryVerseNotification -> {
            postEvent(
                NotificationsRepositoryContract.Events.HideNotification(
                    MemoryVerseNotification,
                )
            )
        }

        is NotificationsRepositoryContract.Inputs.ShowPushNotification -> {
            postEvent(
                NotificationsRepositoryContract.Events.ShowNotification(
                    PushNotification(1),
                    BasicNotification(
                        subject = input.title,
                        message = input.body,
                        deepLink = ScriptureNowRoute.Home
                            .directions()
                            .build(),
                    )
                )
            )
        }

        is NotificationsRepositoryContract.Inputs.ShowVerseOfTheDayNotification -> {
//            postEvent(
//                NotificationsContract.Events.ShowNotification()
//            )
        }
    }
}
