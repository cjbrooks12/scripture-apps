package com.caseyjbrooks.votd.schedules

import com.caseyjbrooks.di.GlobalKoinApplication
import com.copperleaf.ballast.scheduler.workmanager.SchedulerCallback

internal class VotdSchedulerCallback : SchedulerCallback<VotdSchedulesContract.Inputs> {

    override suspend fun dispatchInput(input: VotdSchedulesContract.Inputs) {
        val vm: VotdSchedulesViewModel = GlobalKoinApplication.koinApplication.koin.get()
        vm.sendAndAwaitCompletion(input)
    }
}
