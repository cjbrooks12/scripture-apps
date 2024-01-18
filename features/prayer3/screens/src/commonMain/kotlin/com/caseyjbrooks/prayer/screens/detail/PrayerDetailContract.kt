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
        data class ObservePrayer(val prayerId: PrayerId) :
            PrayerDetailContract.Inputs
        data class PrayerUpdated(val cachedPrayers: Cached<SavedPrayer>) :
            PrayerDetailContract.Inputs

        /**
         * Navigate to the hierarchical parent of the [PrayerDetailRoute], which is [PrayerListRoute]
         */
        data object NavigateUp : com.caseyjbrooks.prayer.screens.detail.PrayerDetailContract.Inputs

        /**
         * Navigate to the previous entry in the router backstack
         */
        data object GoBack : com.caseyjbrooks.prayer.screens.detail.PrayerDetailContract.Inputs

        /**
         * Navigate to the previous entry in the router backstack
         */
        data object Edit : com.caseyjbrooks.prayer.screens.detail.PrayerDetailContract.Inputs

        /**
         * Navigate to the previous entry in the router backstack
         */
        data object PrayNow : com.caseyjbrooks.prayer.screens.detail.PrayerDetailContract.Inputs
    }

    internal sealed interface Events {
        data class NavigateTo(val destination: String, val replaceTop: Boolean = false) :
            PrayerDetailContract.Events
        data object NavigateBack : com.caseyjbrooks.prayer.screens.detail.PrayerDetailContract.Events
    }
}
