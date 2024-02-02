package com.caseyjbrooks.prayer.repository.saved

import app.cash.sqldelight.SuspendingTransactionWithoutReturn
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import co.touchlab.kermit.Logger
import com.caseyjbrooks.database.Prayer
import com.caseyjbrooks.database.Prayer_tag
import com.caseyjbrooks.database.ScriptureNowDatabase
import com.caseyjbrooks.database.Tag
import com.caseyjbrooks.database.UuidFactory
import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.PrayerNotification
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class OfflineDatabaseSavedPrayersRepository(
    private val database: ScriptureNowDatabase,
    private val uuidFactory: UuidFactory,
    private val logger: Logger,
) : SavedPrayersRepository {
    override suspend fun createPrayer(prayer: SavedPrayer) {
        database.transaction {
            // create the prayer record
            database.prayerQueries.createPrayer(
                Prayer(
                    uuid = prayer.uuid.uuid,
                    text = prayer.text,
                    autoArchiveAt = when (prayer.prayerType) {
                        is SavedPrayerType.Persistent -> null
                        is SavedPrayerType.ScheduledCompletable -> prayer.prayerType.completionDate
                    },
                    archivedAt = prayer.archivedAt,
                    notificationSchedule = null,
                    createdAt = prayer.createdAt,
                    updatedAt = prayer.updatedAt,
                )
            )

            syncPrayerTags(prayer)
        }
    }

    override suspend fun updatePrayer(prayer: SavedPrayer) {
        database.transaction {
            database.prayerQueries.updatePrayer(
                uuid = prayer.uuid.uuid,

                text = prayer.text,
                autoArchiveAt = when (prayer.prayerType) {
                    is SavedPrayerType.Persistent -> null
                    is SavedPrayerType.ScheduledCompletable -> prayer.prayerType.completionDate
                },
                archivedAt = prayer.archivedAt,
                notificationSchedule = null,
                updatedAt = prayer.updatedAt,
            )

            syncPrayerTags(prayer)
        }
    }

    override suspend fun deletePrayer(prayer: SavedPrayer) {
        database.prayerQueries.deletePrayer(prayer.uuid.uuid)
    }

    override fun getPrayers(
        archiveStatus: ArchiveStatus,
        prayerTypes: Set<SavedPrayerType>,
        tags: Set<PrayerTag>
    ): Flow<List<SavedPrayer>> {
        return database.prayerQueries
            .getAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { prayerRecordList ->
                prayerRecordList
                    .map { prayerRecord -> prayerRecord.fromRecord() }
                    .filterByArchiveStatus(archiveStatus)
                    .filterByPrayerType(prayerTypes)
                    .filterByTag(tags)
            }
    }

    override fun getPrayerById(uuid: PrayerId): Flow<SavedPrayer?> {
        logger.i("Fetching prayer at ${uuid.uuid}")
        return database.prayerQueries
            .getById(uuid.uuid)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { prayerRecord ->
                logger.i("mapping prayer at ${uuid.uuid}")
                if (prayerRecord != null) {
                    prayerRecord.fromRecord()
                } else {
                    logger.i("Prayer at ${uuid.uuid} not found")
                    error("Prayer at ${uuid.uuid} not found")
                }
            }
    }

    override fun getPrayerByText(prayerText: String): Flow<SavedPrayer?> {
        return database.prayerQueries
            .getByText(prayerText)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { prayerRecord ->
                prayerRecord?.fromRecord()
            }
    }

// Helpers
// ---------------------------------------------------------------------------------------------------------------------

    private fun Prayer.fromRecord(): SavedPrayer {
        val prayerRecord = this

        val tags = database.prayer_tagQueries
            .getTagsForPrayer(prayerRecord.uuid)
            .executeAsList()
            .map { prayerTag ->
                database.tagQueries
                    .getById(prayerTag.tag_uuid)
                    .executeAsOne()
            }
            .map { tag ->
                PrayerTag(tag.name)
            }
            .sortedBy { it.tag }

        return SavedPrayer(
            uuid = PrayerId(prayerRecord.uuid),
            text = prayerRecord.text,
            prayerType = prayerRecord.autoArchiveAt
                ?.let { SavedPrayerType.ScheduledCompletable(it) }
                ?: SavedPrayerType.Persistent,
            tags = tags,
            archived = prayerRecord.archivedAt != null,
            archivedAt = prayerRecord.archivedAt,
            notification = PrayerNotification.None,
            createdAt = prayerRecord.createdAt,
            updatedAt = prayerRecord.updatedAt,
        )
    }

    private suspend fun SuspendingTransactionWithoutReturn.syncPrayerTags(prayer: SavedPrayer) {
        prayer.tags.forEach { tag ->
            database.tagQueries.createTag(
                Tag(uuid = uuidFactory.getNewUuid(), name = tag.tag)
            )
            val tagId = database.tagQueries.getByName(tag.tag).executeAsOne().uuid
            database.prayer_tagQueries.createPrayerTag(
                Prayer_tag(
                    prayer_uuid = prayer.uuid.uuid,
                    tag_uuid = tagId,
                )
            )
        }
    }
}
