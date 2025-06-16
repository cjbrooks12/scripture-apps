package com.copperleaf.biblebits.schedules.main

import com.copperleaf.ballast.BallastViewModel
import com.copperleaf.ballast.ktor.BallastSchedulesPluginConfiguration
import com.copperleaf.ballast.scheduler.schedule.FixedDelaySchedule
import io.ktor.server.application.Application
import io.ktor.util.AttributeKey
import kotlin.time.Duration.Companion.seconds

typealias MainServerScheduleViewModel = BallastViewModel<
        ServerSchedulesContract.Inputs,
        ServerSchedulesContract.Events,
        ServerSchedulesContract.State>

val MainServerSchedule = AttributeKey<MainServerScheduleViewModel>("MainServerSchedule")

fun BallastSchedulesPluginConfiguration.mainServerSchedule(
    application: Application,
) = with(application) {
    registerSchedule(
        attributeKey = MainServerSchedule,
        initialState = ServerSchedulesContract.State,
        inputHandler = ServerSchedulesInputHandler(),
        scheduleAdapter = {
            onSchedule(
                key = "clean cache",
                schedule = FixedDelaySchedule(30.seconds),
                scheduledInput = { ServerSchedulesContract.Inputs.CleanCache("redis") },
            )
        }
    )
}
