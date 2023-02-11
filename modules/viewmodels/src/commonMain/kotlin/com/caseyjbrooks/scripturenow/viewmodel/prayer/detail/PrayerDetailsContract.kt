package com.caseyjbrooks.scripturenow.viewmodel.prayer.detail

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.copperleaf.ballast.repository.cache.Cached

public object PrayerDetailsContract {
    public data class State(
        val prayer: Cached<Prayer> = Cached.NotLoaded(),
    )

    public sealed class Inputs {
        public data class Initialize(val verseUuid: Uuid) : Inputs()
        public data class PrayerUpdated(val prayer: Cached<Prayer>) : Inputs()
        public object EditVerse : Inputs()
        public object DeleteVerse : Inputs()
        public object GoBack : Inputs()
    }

    public sealed class Events {
        public object NavigateBack : Events()
        public data class NavigateTo(val destination: String) : Events()
    }
}
