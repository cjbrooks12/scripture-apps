package com.caseyjbrooks.debug.screens.devinfo

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import io.github.copper_leaf.debugScreens.APP_VERSION
import io.github.copper_leaf.debugScreens.GIT_SHA
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone

internal class DeveloperInfoInputHandler(
    private val timeZone: TimeZone,
    private val clock: Clock,
) : InputHandler<
        DeveloperInfoContract.Inputs,
        DeveloperInfoContract.Events,
        DeveloperInfoContract.State> {
    override suspend fun InputHandlerScope<
            DeveloperInfoContract.Inputs,
            DeveloperInfoContract.Events,
            DeveloperInfoContract.State>.handleInput(
        input: DeveloperInfoContract.Inputs
    ): Unit = when (input) {
        is DeveloperInfoContract.Inputs.Initialize -> {
            val workManagerJobs = getWorkManagerJobInfo()
            updateState {
                it.copy(
                    timeZone = timeZone,
                    clock = clock,
                    appVersion = APP_VERSION,
                    gitSha = GIT_SHA,
                    workManagerJobs = workManagerJobs.map { it to false },
                )
            }
        }

        is DeveloperInfoContract.Inputs.UpdateNow -> {
            updateState { it.copy(now = it.clock.now()) }
        }

        is DeveloperInfoContract.Inputs.TestWorkManagerJob -> {
            // mark this job in progress
            updateState {
                it.copy(
                    workManagerJobs = it.workManagerJobs
                        .toMutableList()
                        .apply {
                            val index = this.indexOfFirst { it.first === input.info }
                            this[index] = input.info to true
                        }
                )
            }

            // try to run the job now
            testWorkManagerJob(input.info)

            // mark this job as progress completed
            updateState {
                it.copy(
                    workManagerJobs = it.workManagerJobs
                        .toMutableList()
                        .apply {
                            val index = this.indexOfFirst { it.first === input.info }
                            this[index] = input.info to false
                        }
                )
            }

            // refresh the workmanager job info
            val workManagerJobs = getWorkManagerJobInfo()
            updateState {
                it.copy(
                    workManagerJobs = workManagerJobs.map { it to false },
                )
            }
        }

        is DeveloperInfoContract.Inputs.NavigateUp -> {
            postEvent(DeveloperInfoContract.Events.NavigateUp)
        }
    }
}
