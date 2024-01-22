package com.caseyjbrooks.votd.schedules

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class VotdSchedulesEventHandler : EventHandler<
        VotdSchedulesContract.Inputs,
        VotdSchedulesContract.Events,
        VotdSchedulesContract.State> {
    override suspend fun EventHandlerScope<
            VotdSchedulesContract.Inputs,
            VotdSchedulesContract.Events,
            VotdSchedulesContract.State>.handleEvent(
        event: VotdSchedulesContract.Events
    ) {
    }
}
