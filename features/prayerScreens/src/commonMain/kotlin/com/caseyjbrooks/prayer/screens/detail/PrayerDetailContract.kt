package com.caseyjbrooks.prayer.screens.detail

import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.screens.list.PrayerListRoute
import com.copperleaf.ballast.repository.cache.Cached

internal object PrayerDetailContract {
    internal data class State(
        val prayerId: PrayerId,
        val cachedPrayer: Cached<SavedPrayer> = Cached.NotLoaded(),
    )

    internal sealed interface Inputs {
        data class ObservePrayer(val prayerId: PrayerId) : Inputs
        data class PrayerUpdated(val cachedPrayers: Cached<SavedPrayer>) : Inputs

        /**
         * Navigate to the hierarchical parent of the [PrayerDetailRoute], which is [PrayerListRoute]
         */
        data object NavigateUp : Inputs

        /**
         * Navigate to the previous entry in the router backstack
         */
        data object GoBack : Inputs

        /**
         * Navigate to the previous entry in the router backstack
         */
        data object Edit : Inputs

        /**
         * Navigate to the previous entry in the router backstack
         */
        data object PrayNow : Inputs
    }

    internal sealed interface Events {
        data class NavigateTo(val destination: String, val replaceTop: Boolean = false) :
            PrayerDetailContract.Events
        data object NavigateBack : Events
    }
}
