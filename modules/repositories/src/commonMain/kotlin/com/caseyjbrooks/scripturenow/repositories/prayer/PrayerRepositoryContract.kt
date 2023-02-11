package com.caseyjbrooks.scripturenow.repositories.prayer

import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.copperleaf.ballast.repository.cache.Cached

public object PrayerRepositoryContract {
    public data class State(
        val initialized: Boolean = false,

        val prayerListInitialized: Boolean = false,
        val prayerList: Cached<List<Prayer>> = Cached.NotLoaded(),
    )

    public sealed class Inputs {
        public object ClearCaches : Inputs()
        public object Initialize : Inputs()
        public object RefreshAllCaches : Inputs()

        public data class RefreshPrayerList(val forceRefresh: Boolean) : Inputs()
        public data class PrayerListUpdated(val prayerList: Cached<List<Prayer>>) : Inputs()
        public data class CreateOrUpdatePrayer(val prayer: Prayer) : Inputs()
        public data class DeletePrayer(val prayer: Prayer) : Inputs()
    }

    public sealed class Events
}
