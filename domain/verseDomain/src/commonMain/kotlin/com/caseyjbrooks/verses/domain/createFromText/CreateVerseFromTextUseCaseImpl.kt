package com.caseyjbrooks.verses.domain.createFromText

import com.caseyjbrooks.database.UuidFactory
import com.caseyjbrooks.verses.domain.create.CreateVerseUseCase
import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.models.VerseId
import com.caseyjbrooks.verses.models.VerseTag
import kotlinx.datetime.Clock

internal class CreateVerseFromTextUseCaseImpl(
    private val createVerseUseCase: CreateVerseUseCase,
    private val uuidFactory: UuidFactory,
    private val clock: Clock,
) : CreateVerseFromTextUseCase {
    override suspend fun invoke(reference: String, text: String, tags: Set<String>): SavedVerse {
        val newVerse = SavedVerse(
            uuid = VerseId(uuidFactory.getNewUuid()),
            reference = reference,
            text = text,
            tags = tags.map { VerseTag(it) },
            archived = false,
            archivedAt = null,
            status = null,
            createdAt = clock.now(),
            updatedAt = clock.now(),
        )
        return createVerseUseCase(newVerse)
    }
}
