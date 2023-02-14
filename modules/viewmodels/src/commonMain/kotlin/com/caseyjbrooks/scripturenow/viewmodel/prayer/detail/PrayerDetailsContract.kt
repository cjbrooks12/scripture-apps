package com.caseyjbrooks.scripturenow.viewmodel.prayer.detail

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.isLoading

public object PrayerDetailsContract {
    public data class State(
        val prayer: Cached<Prayer> = Cached.NotLoaded(),
    ) {
        val loading: Boolean = prayer.isLoading()
    }

    public sealed class Inputs {
        public data class Initialize(val prayerUuid: Uuid) : Inputs()
        public data class PrayerUpdated(val prayer: Cached<Prayer>) : Inputs()
        public object EditPrayer : Inputs()
        public object DeletePrayer : Inputs()
        public object GoBack : Inputs()
    }

    public sealed class Events {
        public data class NavigateTo(val destination: String) : Events()
        public object NavigateBack : Events()
    }
}
