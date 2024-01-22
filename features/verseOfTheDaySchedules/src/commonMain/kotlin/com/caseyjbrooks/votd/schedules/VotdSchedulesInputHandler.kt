package com.caseyjbrooks.votd.schedules

import com.caseyjbrooks.notifications.NotificationService
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

internal class VotdSchedulesInputHandler(
    private val notificationService: NotificationService,
) : InputHandler<
        VotdSchedulesContract.Inputs,
        VotdSchedulesContract.Events,
        VotdSchedulesContract.State> {
    override suspend fun InputHandlerScope<
            VotdSchedulesContract.Inputs,
            VotdSchedulesContract.Events,
            VotdSchedulesContract.State>.handleInput(
        input: VotdSchedulesContract.Inputs
    ): Unit = when (input) {
        VotdSchedulesContract.Inputs.FetchVotd -> {

        }

        VotdSchedulesContract.Inputs.VotdNotification -> {
            notificationService.showNotification(
                title = "Verse of the Day",
                message = "Click to view today's verse."
            )
        }
    }
}
