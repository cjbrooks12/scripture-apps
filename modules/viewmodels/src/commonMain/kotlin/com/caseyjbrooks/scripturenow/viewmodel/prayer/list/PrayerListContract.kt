package com.caseyjbrooks.scripturenow.viewmodel.prayer.list

import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.copperleaf.ballast.repository.cache.Cached

public object PrayerListContract {
    public data class State(
        val verses: Cached<List<Prayer>> = Cached.NotLoaded(),
    )

    public sealed class Inputs {
        public data class Initialize(val forceRefresh: Boolean) : Inputs()
        public data class VersesUpdated(val verses: Cached<List<Prayer>>) : Inputs()
        public object CreateVerse : Inputs()
        public data class ViewVerse(val verse: Prayer) : Inputs()
        public data class EditVerse(val verse: Prayer) : Inputs()
        public data class DeleteVerse(val verse: Prayer) : Inputs()
    }

    public sealed class Events {
        public object NavigateBack : Events()
        public data class NavigateTo(val destination: String) : Events()
    }
}
