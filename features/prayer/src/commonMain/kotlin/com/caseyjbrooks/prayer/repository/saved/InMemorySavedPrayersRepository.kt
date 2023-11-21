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
import kotlinx.datetime.Instant

public class InMemorySavedPrayersRepository(
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
        tags: Set<PrayerTag>,
    ): Flow<List<SavedPrayer>> {
        return _db.asStateFlow()
            .map { allPrayers ->
                val prayersFilteredByStatus = when (archiveStatus) {
                    ArchiveStatus.NotArchived -> allPrayers.filter { !it.archived }
                    ArchiveStatus.Archived -> allPrayers.filter { it.archived }
                    ArchiveStatus.FullCollection -> allPrayers
                }
                val prayersFilteredByStatusAndTag = if (tags.isNotEmpty()) {
                    prayersFilteredByStatus.filter { filteredPrayer ->
                        tags.all { tag ->
                            filteredPrayer.tags.contains(tag)
                        }
                    }
                } else {
                    prayersFilteredByStatus
                }

                prayersFilteredByStatusAndTag
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

    internal companion object {
        internal val INSTANCE = InMemorySavedPrayersRepository(
            initialPrayers = listOf(
                SavedPrayer(
                    PrayerId("1"),
                    "Prayer One",
                    SavedPrayerType.Persistent,
                    tags = emptyList(),
                    archived = false,
                    archivedAt = null,
                    createdAt = Instant.fromEpochMilliseconds(0L),
                    updatedAt = Instant.fromEpochMilliseconds(0L),
                ),
                SavedPrayer(
                    PrayerId("2"),
                    "Prayer Two",
                    SavedPrayerType.Persistent,
                    tags = emptyList(),
                    archived = false,
                    archivedAt = null,
                    createdAt = Instant.fromEpochMilliseconds(0L),
                    updatedAt = Instant.fromEpochMilliseconds(0L),
                ),
                SavedPrayer(
                    PrayerId("3"),
                    "Prayer Three",
                    SavedPrayerType.Persistent,
                    tags = emptyList(),
                    archived = false,
                    archivedAt = null,
                    createdAt = Instant.fromEpochMilliseconds(0L),
                    updatedAt = Instant.fromEpochMilliseconds(0L),
                ),
                SavedPrayer(
                    PrayerId("4"),
                    "Prayer Four",
                    SavedPrayerType.Persistent,
                    tags = emptyList(),
                    archived = false,
                    archivedAt = null,
                    createdAt = Instant.fromEpochMilliseconds(0L),
                    updatedAt = Instant.fromEpochMilliseconds(0L),
                ),
                SavedPrayer(
                    PrayerId("5"),
                    "Prayer Five",
                    SavedPrayerType.Persistent,
                    tags = emptyList(),
                    archived = false,
                    archivedAt = null,
                    createdAt = Instant.fromEpochMilliseconds(0L),
                    updatedAt = Instant.fromEpochMilliseconds(0L),
                ),
            ),
        )
    }
}
