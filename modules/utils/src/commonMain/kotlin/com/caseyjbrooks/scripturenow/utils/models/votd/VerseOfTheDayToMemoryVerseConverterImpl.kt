package com.caseyjbrooks.scripturenow.utils.models.votd

import com.benasher44.uuid.uuid4
import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.utils.now
import kotlinx.datetime.LocalDateTime

public class VerseOfTheDayToMemoryVerseConverterImpl : VerseOfTheDayToMemoryVerseConverter {
    override fun convertVerseOfTheDayToMemoryVerse(
        verseOfTheDay: VerseOfTheDay,
    ): MemoryVerse {
        return MemoryVerse(
            uuid = uuid4(), // create a new UUID for this verse
            main = false,
            text = verseOfTheDay.text,
            reference = verseOfTheDay.reference,
            version = verseOfTheDay.version,
            verseUrl = verseOfTheDay.verseUrl,
            notice = verseOfTheDay.notice,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
    }
}
