package com.caseyjbrooks.prayer.repository.saved

import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.PrayerNotification
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType

internal fun List<SavedPrayer>.filterByArchiveStatus(archiveStatus: ArchiveStatus): List<SavedPrayer> {
    return when (archiveStatus) {
        ArchiveStatus.NotArchived -> this.filter { !it.archived }
        ArchiveStatus.Archived -> this.filter { it.archived }
        ArchiveStatus.FullCollection -> this
    }
}

internal fun List<SavedPrayer>.filterByPrayerType(prayerTypes: Set<SavedPrayerType>): List<SavedPrayer> {
    return if (prayerTypes.isNotEmpty()) {
        this.filter { filteredPrayer ->
            prayerTypes.any { prayerType ->
                filteredPrayer.prayerType::class == prayerType::class
            }
        }
    } else {
        this
    }
}

internal fun List<SavedPrayer>.filterByTag(tags: Set<PrayerTag>): List<SavedPrayer> {
    return if (tags.isNotEmpty()) {
        this.filter { filteredPrayer ->
            tags.all { tag ->
                filteredPrayer.tags.contains(tag)
            }
        }
    } else {
        this
    }
}

internal fun List<SavedPrayer>.filterByNotifications(notifications: Boolean?): List<SavedPrayer> {
    return when (notifications) {
        true -> this.filter { it.notification != PrayerNotification.None }
        false -> this.filter { it.notification == PrayerNotification.None }
        null -> this
    }
}
