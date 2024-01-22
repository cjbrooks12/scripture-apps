package com.caseyjbrooks.votd.schedules

import com.caseyjbrooks.di.GlobalScriptureNowKoinApplication
import com.copperleaf.ballast.scheduler.workmanager.SchedulerCallback
import org.koin.core.qualifier.named

internal class VotdSchedulerCallback : SchedulerCallback<VotdSchedulesContract.Inputs> {

    override suspend fun dispatchInput(input: VotdSchedulesContract.Inputs) {
        val vm: VotdSchedulesViewModel = GlobalScriptureNowKoinApplication.koinApplication.koin
            .get(named("VotdSchedulesViewModel"))
        vm.sendAndAwaitCompletion(input)
    }
}
