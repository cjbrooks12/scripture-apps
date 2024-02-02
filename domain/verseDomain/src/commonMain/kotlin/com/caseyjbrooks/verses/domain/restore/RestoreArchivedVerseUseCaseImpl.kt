package com.caseyjbrooks.verses.domain.restore

import com.caseyjbrooks.verses.domain.update.UpdateVerseUseCase
import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.repository.saved.SavedVersesRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

internal class RestoreArchivedVerseUseCaseImpl(
    private val savedVersesRepository: SavedVersesRepository,
    private val updateVerseUseCase: UpdateVerseUseCase,
    private val clock: Clock,
) : RestoreArchivedVerseUseCase {
    override suspend operator fun invoke(verse: SavedVerse) {
        val latestVerse = savedVersesRepository.getVerseById(verse.uuid).first()
            ?: error("Verse with ID ${verse.uuid} does not exist")

        if (!latestVerse.archived) {
            // no-op, not in the archives
        } else {
            updateVerseUseCase(
                verse.copy(
                    archived = false,
                    archivedAt = null,
                ),
            )
        }
    }
}
