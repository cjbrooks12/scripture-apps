package com.caseyjbrooks.verses.repository.saved

import com.caseyjbrooks.verses.models.ArchiveStatus
import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.models.VerseId
import com.caseyjbrooks.verses.models.VerseTag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class FakeSavedVersesRepository(
    initialVerses: List<SavedVerse> = emptyList<SavedVerse>(),
) : SavedVersesRepository {
    private val _db = MutableStateFlow(initialVerses)

    override suspend fun createVerse(verse: SavedVerse) {
        _db.update { verses ->
            val verseById = verses.singleOrNull { it.uuid == verse.uuid }

            if (verseById != null) {
                error("Verse with id '${verse.uuid.uuid}' already exists!")
            }

            verses + verse
        }
    }

    override suspend fun updateVerse(verse: SavedVerse) {
        _db.update { verses ->
            val indexOfVerseById = verses.indexOfFirst { it.uuid == verse.uuid }

            if (indexOfVerseById == -1) {
                error("Verse with id '${verse.uuid.uuid}' not found!")
            }

            verses
                .toMutableList()
                .apply { this[indexOfVerseById] = verse }
                .toList()
        }
    }

    override suspend fun deleteVerse(verse: SavedVerse) {
        _db.update { verses ->
            val indexOfVerseById = verses.indexOfFirst { it.uuid == verse.uuid }

            if (indexOfVerseById == -1) {
                error("Verse with id '${verse.uuid.uuid}' not found!")
            }

            verses
                .toMutableList()
                .apply { this.removeAt(indexOfVerseById) }
                .toList()
        }
    }

    override fun getVerses(
        archiveStatus: ArchiveStatus,
        tags: Set<VerseTag>,
    ): Flow<List<SavedVerse>> {
        return _db.asStateFlow()
            .map { allVerses ->
                allVerses
                    .filterByArchiveStatus(archiveStatus)
                    .filterByTag(tags)
            }
    }

    override fun getVerseById(uuid: VerseId): Flow<SavedVerse?> {
        return _db
            .map { verses -> verses.singleOrNull { it.uuid == uuid } }
    }
}
