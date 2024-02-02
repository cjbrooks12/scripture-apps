package com.caseyjbrooks.verses.repository.saved

import app.cash.sqldelight.SuspendingTransactionWithoutReturn
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import co.touchlab.kermit.Logger
import com.caseyjbrooks.database.ScriptureNowDatabase
import com.caseyjbrooks.database.Tag
import com.caseyjbrooks.database.UuidFactory
import com.caseyjbrooks.database.Verse
import com.caseyjbrooks.database.Verse_tag
import com.caseyjbrooks.verses.models.ArchiveStatus
import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.models.VerseId
import com.caseyjbrooks.verses.models.VerseTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class OfflineDatabaseSavedVersesRepository(
    private val database: ScriptureNowDatabase,
    private val uuidFactory: UuidFactory,
    private val logger: Logger,
) : SavedVersesRepository {
    override suspend fun createVerse(verse: SavedVerse) {
        database.transaction {
            // create the verse record
            database.verseQueries.createVerse(
                Verse(
                    uuid = verse.uuid.uuid,
                    reference = verse.reference,
                    text = verse.text,
                    archivedAt = verse.archivedAt,
                    status = null,
                    createdAt = verse.createdAt,
                    updatedAt = verse.updatedAt,
                )
            )

            syncVerseTags(verse)
        }
    }

    override suspend fun updateVerse(verse: SavedVerse) {
        database.transaction {
            database.verseQueries.updateVerse(
                uuid = verse.uuid.uuid,

                reference = verse.reference,
                text = verse.text,
                archivedAt = verse.archivedAt,
                status = null,
                updatedAt = verse.updatedAt,
            )

            syncVerseTags(verse)
        }
    }

    override suspend fun deleteVerse(verse: SavedVerse) {
        database.verseQueries.deleteVerse(verse.uuid.uuid)
    }

    override fun getVerses(
        archiveStatus: ArchiveStatus,
        tags: Set<VerseTag>
    ): Flow<List<SavedVerse>> {
        return database.verseQueries
            .getAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { verseRecordList ->
                verseRecordList
                    .map { verseRecord -> verseRecord.fromRecord() }
                    .filterByArchiveStatus(archiveStatus)
                    .filterByTag(tags)
            }
    }

    override fun getVerseById(uuid: VerseId): Flow<SavedVerse?> {
        logger.i("Fetching verse at ${uuid.uuid}")
        return database.verseQueries
            .getById(uuid.uuid)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { verseRecord ->
                logger.i("mapping verse at ${uuid.uuid}")
                if (verseRecord != null) {
                    verseRecord.fromRecord()
                } else {
                    logger.i("Verse at ${uuid.uuid} not found")
                    error("Verse at ${uuid.uuid} not found")
                }
            }
    }

// Helpers
// ---------------------------------------------------------------------------------------------------------------------

    private fun Verse.fromRecord(): SavedVerse {
        val verseRecord = this

        val tags = database.verse_tagQueries
            .getTagsForVerse(verseRecord.uuid)
            .executeAsList()
            .map { verseTag ->
                database.tagQueries
                    .getById(verseTag.tag_uuid)
                    .executeAsOne()
            }
            .map { tag ->
                VerseTag(tag.name)
            }
            .sortedBy { it.tag }

        return SavedVerse(
            uuid = VerseId(verseRecord.uuid),
            reference = verseRecord.reference,
            text = verseRecord.text,
            tags = tags,
            archived = verseRecord.archivedAt != null,
            status = null,
            archivedAt = verseRecord.archivedAt,
            createdAt = verseRecord.createdAt,
            updatedAt = verseRecord.updatedAt,
        )
    }

    private suspend fun SuspendingTransactionWithoutReturn.syncVerseTags(verse: SavedVerse) {
        verse.tags.forEach { tag ->
            database.tagQueries.createTag(
                Tag(uuid = uuidFactory.getNewUuid(), name = tag.tag)
            )
            val tagId = database.tagQueries.getByName(tag.tag).executeAsOne().uuid
            database.verse_tagQueries.createVerseTag(
                Verse_tag(
                    verse_uuid = verse.uuid.uuid,
                    tag_uuid = tagId,
                )
            )
        }
    }
}
