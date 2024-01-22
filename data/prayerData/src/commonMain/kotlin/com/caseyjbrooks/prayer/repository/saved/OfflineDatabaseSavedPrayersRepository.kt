package com.caseyjbrooks.prayer.repository.saved

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import co.touchlab.kermit.Logger
import com.caseyjbrooks.database.Prayer
import com.caseyjbrooks.database.ScriptureNowDatabase
import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

internal class OfflineDatabaseSavedPrayersRepository(
    private val database: ScriptureNowDatabase,
    private val logger: Logger,
) : SavedPrayersRepository {
    override suspend fun createPrayer(prayer: SavedPrayer) {
        database.prayersQueries.createPrayer(
            Prayer(
                uuid = prayer.uuid.uuid,
                text = prayer.text,
                prayerType = "Persistent",
                archivedAt = prayer.archivedAt?.toEpochMilliseconds(),
                createdAt = prayer.createdAt.toEpochMilliseconds(),
                updatedAt = prayer.updatedAt.toEpochMilliseconds(),
            )
        )
    }

    override suspend fun updatePrayer(prayer: SavedPrayer) {
        database.prayersQueries.updatePrayer(
            uuid = prayer.uuid.uuid,

            text = prayer.text,
            prayerType = "Persistent",
            archivedAt = prayer.archivedAt?.toEpochMilliseconds(),
            createdAt = prayer.createdAt.toEpochMilliseconds(),
            updatedAt = prayer.updatedAt.toEpochMilliseconds(),
        )
    }

    override suspend fun deletePrayer(prayer: SavedPrayer) {
        database.prayersQueries.deletePrayer(prayer.uuid.toString())
    }

    override fun getPrayers(
        archiveStatus: ArchiveStatus,
        prayerTypes: Set<SavedPrayerType>,
        tags: Set<PrayerTag>
    ): Flow<List<SavedPrayer>> {
        return database.prayersQueries
            .getAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { prayerRecordList ->
                prayerRecordList.map { prayerRecord ->
                    SavedPrayer(
                        uuid = PrayerId(prayerRecord.uuid),
                        text = prayerRecord.text,
                        prayerType = SavedPrayerType.Persistent,
                        tags = emptyList(),
                        archived = prayerRecord.archivedAt != null,
                        archivedAt = prayerRecord.archivedAt?.let { Instant.fromEpochMilliseconds(it) },
                        createdAt = Instant.fromEpochMilliseconds(prayerRecord.createdAt),
                        updatedAt = Instant.fromEpochMilliseconds(prayerRecord.updatedAt),
                    )
                }
            }
    }

    override fun getPrayerById(uuid: PrayerId): Flow<SavedPrayer?> {
        logger.i("Fetching prayer at ${uuid.uuid}")
        return database.prayersQueries
            .getById(uuid.uuid)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { prayerRecord ->
                logger.i("mapping prayer at ${uuid.uuid}")

                if (prayerRecord != null) {
                    SavedPrayer(
                        uuid = PrayerId(prayerRecord.uuid),
                        text = prayerRecord.text,
                        prayerType = SavedPrayerType.Persistent,
                        tags = emptyList(),
                        archived = prayerRecord.archivedAt != null,
                        archivedAt = prayerRecord.archivedAt?.let { Instant.fromEpochMilliseconds(it) },
                        createdAt = Instant.fromEpochMilliseconds(prayerRecord.createdAt),
                        updatedAt = Instant.fromEpochMilliseconds(prayerRecord.updatedAt),
                    )
                } else {
                    logger.i("Prayer at ${uuid.uuid} not found")
                    error("Prayer at ${uuid.uuid} not found")
                }
            }
    }

    override fun getPrayerByText(prayerText: String): Flow<SavedPrayer?> {
        return database.prayersQueries
            .getByText(prayerText)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { prayerRecord ->
                if (prayerRecord != null) {
                    SavedPrayer(
                        uuid = PrayerId(prayerRecord.uuid),
                        text = prayerRecord.text,
                        prayerType = SavedPrayerType.Persistent,
                        tags = emptyList(),
                        archived = prayerRecord.archivedAt != null,
                        archivedAt = prayerRecord.archivedAt?.let { Instant.fromEpochMilliseconds(it) },
                        createdAt = Instant.fromEpochMilliseconds(prayerRecord.createdAt),
                        updatedAt = Instant.fromEpochMilliseconds(prayerRecord.updatedAt),
                    )
                } else {
                    null
                }
            }
    }
}
