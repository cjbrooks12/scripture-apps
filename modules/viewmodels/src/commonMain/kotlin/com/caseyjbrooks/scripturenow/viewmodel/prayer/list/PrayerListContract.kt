package com.caseyjbrooks.scripturenow.viewmodel.prayer.list

import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.copperleaf.ballast.repository.cache.Cached

public object PrayerListContract {
    public data class State(
        val prayers: Cached<List<Prayer>> = Cached.NotLoaded(),
    )

    public sealed class Inputs {
        public data class Initialize(val forceRefresh: Boolean) : Inputs()
        public data class PrayersUpdated(val prayers: Cached<List<Prayer>>) : Inputs()
        public object CreatePrayer : Inputs()
        public data class ViewPrayer(val prayer: Prayer) : Inputs()
        public data class EditPrayer(val prayer: Prayer) : Inputs()
        public data class DeletePrayer(val prayer: Prayer) : Inputs()
    }

    public sealed class Events {
        public data class NavigateTo(val destination: String) : Events()
        public object NavigateBack : Events()
    }
}
