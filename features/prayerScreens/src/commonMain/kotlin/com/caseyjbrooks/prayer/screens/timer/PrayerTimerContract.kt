package com.caseyjbrooks.prayer.screens.timer

import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.screens.detail.PrayerDetailRoute
import com.caseyjbrooks.prayer.screens.list.PrayerListRoute
import com.copperleaf.ballast.repository.cache.Cached

internal object PrayerTimerContract {
    internal data class State(
        val prayerId: PrayerId,
        val cachedPrayer: Cached<SavedPrayer> = Cached.NotLoaded(),

        val totalTime: Int = 60,
        val currentTime: Int = 0,
        val running: Boolean = false,
    ) {
        val isStopped: Boolean = !running && currentTime == 0
    }

    internal sealed interface Inputs {
        data class ObservePrayer(val prayerId: PrayerId) : Inputs
        data class PrayerUpdated(val cachedPrayers: Cached<SavedPrayer>) : Inputs

        data object StartTimer : Inputs
        data object PauseTimer : Inputs
        data object ResumeTimer : Inputs
        data object StopTimer : Inputs
        data object ResetTimer : Inputs
        data object TimerCompleted : Inputs

        data object OnTimerTick : Inputs

        /**
         * Navigate to the hierarchical parent of the [PrayerDetailRoute], which is [PrayerListRoute]
         */
        data object NavigateUp : Inputs

        /**
         * Navigate to the previous entry in the router backstack
         */
        data object GoBack : Inputs
    }

    internal sealed interface Events {
        data class NavigateTo(val destination: String, val replaceTop: Boolean = false) : Events
        data object NavigateBack : Events
    }
}
