package com.caseyjbrooks.verses.domain.create

import com.caseyjbrooks.verses.models.ArchiveStatus
import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.models.VerseUser
import com.caseyjbrooks.verses.repository.config.VerseConfig
import com.caseyjbrooks.verses.repository.saved.SavedVersesRepository
import com.caseyjbrooks.verses.repository.user.VerseUserRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

internal class CreateVerseUseCaseImpl(
    private val savedVersesRepository: SavedVersesRepository,
    private val verseConfig: VerseConfig,
    private val verseUserRepository: VerseUserRepository,
    private val clock: Clock,
) : CreateVerseUseCase {
    override suspend operator fun invoke(verse: SavedVerse): SavedVerse {
        val verseUser = verseUserRepository.getUserProfile()
            ?: error("User not logged in")

        if (verseUser.subscription == VerseUser.SubscriptionStatus.Free) {
            // on the free plan, check that the user has not exceeded the threshold

            if (savedVersesRepository
                    .getVerses(ArchiveStatus.FullCollection, emptySet())
                    .first().size >= verseConfig.maxVersesOnFreePlan
            ) {
                error("Exceeded free plan limit")
            }
        }

        val currentInstant = clock.now()
        val updatedVerse = verse.copy(
            createdAt = currentInstant,
            updatedAt = currentInstant,
        )
        savedVersesRepository.createVerse(updatedVerse)
        return updatedVerse
    }
}
