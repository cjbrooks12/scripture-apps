package com.caseyjbrooks.prayer.screens.form

import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.screens.detail.PrayerDetailRoute
import com.caseyjbrooks.prayer.screens.list.PrayerListRoute
import com.copperleaf.ballast.repository.cache.Cached

internal object PrayerFormContract {
    internal data class State(
        val prayerId: PrayerId?,
        val cachedPrayer: Cached<SavedPrayer> = Cached.NotLoaded(),
    )

    internal sealed interface Inputs {
        data class ObservePrayer(val prayerId: PrayerId?) : Inputs
        data class PrayerUpdated(val cachedPrayers: Cached<SavedPrayer>) : Inputs
        data class UpdatePrayer(val text: String) : Inputs
        data class CreatePrayer(val text: String) : Inputs

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
