package com.caseyjbrooks.prayer.screens.timer

import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCase
import com.caseyjbrooks.prayer.screens.detail.PrayerDetailRoute
import com.caseyjbrooks.prayer.screens.timer.PrayerTimerScheduleAdapter.Companion.SCHEDULE_KEY
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.postInput
import com.copperleaf.ballast.scheduler.SchedulerInterceptor
import com.copperleaf.ballast.scheduler.vm.SchedulerContract
import kotlinx.coroutines.flow.map
import kotlin.math.max

@Suppress("UNCHECKED_CAST")
internal class PrayerTimerInputHandler(
    private val getByIdUseCase: GetPrayerByIdUseCase,
) : InputHandler<
        PrayerTimerContract.Inputs,
        PrayerTimerContract.Events,
        PrayerTimerContract.State,
        > {
    override suspend fun InputHandlerScope<
            PrayerTimerContract.Inputs,
            PrayerTimerContract.Events,
            PrayerTimerContract.State,
            >.handleInput(
        input: PrayerTimerContract.Inputs,
    ): Unit = when (input) {
        is PrayerTimerContract.Inputs.ObservePrayer -> {
            val currentState = updateStateAndGet { it.copy(prayerId = input.prayerId) }
            observeFlows(
                "ObservePrayer",
                getByIdUseCase(currentState.prayerId)
                    .map { PrayerTimerContract.Inputs.PrayerUpdated(it) },
            )
        }

        is PrayerTimerContract.Inputs.PrayerUpdated -> {
            updateState { it.copy(cachedPrayer = input.cachedPrayers) }
        }

        is PrayerTimerContract.Inputs.OnTimerTick -> {
            val previousState = getCurrentState()
            val currentState = updateStateAndGet { it.copy(currentTime = max(it.currentTime - 1, 0)) }

            if (previousState.currentTime > 0 && currentState.currentTime == 0) {
                postInput(PrayerTimerContract.Inputs.StopTimer)
                postInput(PrayerTimerContract.Inputs.TimerCompleted)
            } else {
                noOp()
            }
        }

        is PrayerTimerContract.Inputs.StartTimer -> {
            updateState {
                it.copy(
                    currentTime = it.totalTime,
                    running = true,
                )
            }
            sendToScheduler(
                "StartTimer",
                SchedulerContract.Inputs.StartSchedules(PrayerTimerScheduleAdapter())
            )
        }

        is PrayerTimerContract.Inputs.PauseTimer -> {
            updateState {
                it.copy(
                    running = false,
                )
            }
            sendToScheduler(
                "PauseTimer",
                SchedulerContract.Inputs.PauseSchedule(SCHEDULE_KEY)
            )
        }

        is PrayerTimerContract.Inputs.ResumeTimer -> {
            updateState {
                it.copy(
                    running = true,
                )
            }
            sendToScheduler(
                "ResumeTimer",
                SchedulerContract.Inputs.ResumeSchedule(SCHEDULE_KEY)
            )
        }

        is PrayerTimerContract.Inputs.ResetTimer -> {
            updateState {
                it.copy(
                    currentTime = it.totalTime,
                    running = true,
                )
            }
            sendToScheduler(
                "ResetTimer",
                SchedulerContract.Inputs.CancelSchedule(SCHEDULE_KEY),
                SchedulerContract.Inputs.StartSchedules(PrayerTimerScheduleAdapter())
            )
        }

        is PrayerTimerContract.Inputs.StopTimer -> {
            updateState {
                it.copy(
                    currentTime = 0,
                    running = false,
                )
            }
            sendToScheduler(
                "StopTimer",
                SchedulerContract.Inputs.CancelSchedule(SCHEDULE_KEY),
            )
        }

        is PrayerTimerContract.Inputs.TimerCompleted -> {
            updateState {
                it.copy(
                    running = false,
                )
            }
            noOp()
        }

        is PrayerTimerContract.Inputs.NavigateUp -> {
            postEvent(
                PrayerTimerContract.Events.NavigateTo(
                    PrayerDetailRoute.Directions.list(),
                    replaceTop = true,
                ),
            )
        }

        is PrayerTimerContract.Inputs.GoBack -> {
            postEvent(
                PrayerTimerContract.Events.NavigateBack,
            )
        }
    }

    private fun InputHandlerScope<
            PrayerTimerContract.Inputs,
            PrayerTimerContract.Events,
            PrayerTimerContract.State,
            >.sendToScheduler(
        key: String,
        vararg inputs: SchedulerContract.Inputs<
                PrayerTimerContract.Inputs,
                PrayerTimerContract.Events,
                PrayerTimerContract.State,
                >
    ) {
        sideJob(key) {
            val schedulerController = (getInterceptor(SchedulerInterceptor.Key) as SchedulerInterceptor<
                    PrayerTimerContract.Inputs,
                    PrayerTimerContract.Events,
                    PrayerTimerContract.State,
                    >)
                .controller

            inputs.forEach { input ->
                schedulerController.sendAndAwaitCompletion(input)
            }
        }
    }
}
