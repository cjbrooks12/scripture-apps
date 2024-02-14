package com.caseyjbrooks.prayer.schedules

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.WorkManager
import com.caseyjbrooks.di.GlobalKoinApplication
import com.caseyjbrooks.domain.bus.EventBus
import com.caseyjbrooks.prayer.domain.PrayerDomainEvents
import com.copperleaf.ballast.ExperimentalBallastApi
import com.copperleaf.ballast.scheduler.workmanager.syncSchedulesOnStartup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

public actual class PrayerSchedulesEventBusSubscription : EventBus.Subscription {
    @OptIn(ExperimentalBallastApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun CoroutineScope.startSubscription(bus: EventBus) {
        bus.events
            .filterIsInstance<PrayerDomainEvents.PrayerAddedOrChanged>()
            .onEach {
                Log.d("PrayerSchedules", "Receiving event: $it")
                WorkManager.getInstance(
                    GlobalKoinApplication.koinApplication.koin.get()
                ).syncSchedulesOnStartup(
                    adapter = PrayerSchedulesAdapter(),
                    callback = PrayerSchedulesCallback(),
                    withHistory = false,
                )
            }
            .launchIn(this)
    }
}
