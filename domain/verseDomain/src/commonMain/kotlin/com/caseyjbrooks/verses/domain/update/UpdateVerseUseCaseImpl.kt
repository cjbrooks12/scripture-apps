package com.caseyjbrooks.verses.domain.update

import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.repository.saved.SavedVersesRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

internal class UpdateVerseUseCaseImpl(
    private val savedVersesRepository: SavedVersesRepository,
    private val clock: Clock,
) : UpdateVerseUseCase {
    override suspend operator fun invoke(verse: SavedVerse): SavedVerse {
        val latestVerse = savedVersesRepository.getVerseById(verse.uuid).first()
            ?: error("Verse with ID ${verse.uuid} does not exist")

        return if (latestVerse == verse) {
            // no-op, there are no changes needed
            verse
        } else {
            val updatedVerse = verse.copy(
                updatedAt = clock.now(),
            )
            savedVersesRepository.updateVerse(updatedVerse)
            updatedVerse
        }
    }
}
