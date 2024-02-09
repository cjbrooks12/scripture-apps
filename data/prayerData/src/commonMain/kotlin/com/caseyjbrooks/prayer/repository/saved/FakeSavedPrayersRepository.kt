package com.caseyjbrooks.prayer.repository.saved

import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class FakeSavedPrayersRepository(
    initialPrayers: List<SavedPrayer> = emptyList<SavedPrayer>(),
) : SavedPrayersRepository {
    private val _db = MutableStateFlow(initialPrayers)

    override suspend fun createPrayer(prayer: SavedPrayer) {
        _db.update { prayers ->
            val prayerById = prayers.singleOrNull { it.uuid == prayer.uuid }

            if (prayerById != null) {
                error("Prayer with id '${prayer.uuid.uuid}' already exists!")
            }

            prayers + prayer
        }
    }

    override suspend fun updatePrayer(prayer: SavedPrayer) {
        _db.update { prayers ->
            val indexOfPrayerById = prayers.indexOfFirst { it.uuid == prayer.uuid }

            if (indexOfPrayerById == -1) {
                error("Prayer with id '${prayer.uuid.uuid}' not found!")
            }

            prayers
                .toMutableList()
                .apply { this[indexOfPrayerById] = prayer }
                .toList()
        }
    }

    override suspend fun deletePrayer(prayer: SavedPrayer) {
        _db.update { prayers ->
            val indexOfPrayerById = prayers.indexOfFirst { it.uuid == prayer.uuid }

            if (indexOfPrayerById == -1) {
                error("Prayer with id '${prayer.uuid.uuid}' not found!")
            }

            prayers
                .toMutableList()
                .apply { this.removeAt(indexOfPrayerById) }
                .toList()
        }
    }

    override fun getPrayers(
        archiveStatus: ArchiveStatus,
        prayerTypes: Set<SavedPrayerType>,
        tags: Set<PrayerTag>,
        withNotifications: Boolean?
    ): Flow<List<SavedPrayer>> {
        return _db.asStateFlow()
            .map { allPrayers ->
                allPrayers
                    .filterByArchiveStatus(archiveStatus)
                    .filterByPrayerType(prayerTypes)
                    .filterByTag(tags)
                    .filterByNotifications(withNotifications)
            }
    }

    override fun getPrayerById(uuid: PrayerId): Flow<SavedPrayer?> {
        return _db
            .map { prayers -> prayers.singleOrNull { it.uuid == uuid } }
    }

    override fun getPrayerByText(prayerText: String): Flow<SavedPrayer?> {
        return _db
            .map { prayers -> prayers.singleOrNull { it.text == prayerText } }
    }
}
