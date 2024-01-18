package com.caseyjbrooks.prayer.screens.list

import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.map

internal object PrayerListContract {
    internal data class State(
        val cachedPrayers: Cached<List<SavedPrayer>> = Cached.NotLoaded(),
        val archiveStatus: ArchiveStatus = ArchiveStatus.NotArchived,
        val prayerTypeFilter: Set<SavedPrayerType> = emptySet(),
        val tagFilter: Set<PrayerTag> = emptySet(),
    ) {
        val allTags: Cached<List<PrayerTag>> = cachedPrayers.map { prayers ->
            prayers
                .flatMap { it.tags }
                .distinct()
        }
    }

    internal sealed interface Inputs {
        data object ObservePrayerList : Inputs
        data class PrayersUpdated(val cachedPrayers: Cached<List<SavedPrayer>>) : Inputs

        data class SetArchiveStatus(val archiveStatus: ArchiveStatus) : Inputs
        data class AddTagFilter(val tag: PrayerTag) : Inputs
        data class RemoveTagFilter(val tag: PrayerTag) : Inputs

        data object CreateNewPrayer : Inputs
        data class ViewPrayerDetails(val prayer: SavedPrayer) : Inputs
        data class EditPrayer(val prayer: SavedPrayer) : Inputs
        data class PrayNow(val prayer: SavedPrayer) : Inputs

        data object GoBack : Inputs
    }

    internal sealed interface Events {
        data class NavigateTo(val destination: String, val replaceTop: Boolean = false) : Events
        data object NavigateBack : Events
    }
}
