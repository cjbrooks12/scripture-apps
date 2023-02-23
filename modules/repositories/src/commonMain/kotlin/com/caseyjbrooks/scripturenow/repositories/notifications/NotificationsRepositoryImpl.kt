package com.caseyjbrooks.scripturenow.repositories.notifications

import com.copperleaf.ballast.*
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.FifoInputStrategy
import kotlinx.coroutines.CoroutineScope

public class NotificationsRepositoryImpl(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: NotificationsRepositoryInputHandler,
    eventHandler: EventHandler<
            NotificationsRepositoryContract.Inputs,
            NotificationsRepositoryContract.Events,
            NotificationsRepositoryContract.State>,
) : BasicViewModel<
        NotificationsRepositoryContract.Inputs,
        NotificationsRepositoryContract.Events,
        NotificationsRepositoryContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .apply {
            this += BootstrapInterceptor {
                NotificationsRepositoryContract.Inputs.Initialize
            }
            inputStrategy = FifoInputStrategy()
        }
        .withViewModel(
            inputHandler = inputHandler,
            initialState = NotificationsRepositoryContract.State(),
            name = "Notifications Repository",
        )
        .build(),
    eventHandler = eventHandler,
), NotificationsRepository {

    override fun showPushNotification(title: String, body: String) {
        trySend(NotificationsRepositoryContract.Inputs.ShowPushNotification(title, body))
    }
}
