package com.caseyjbrooks.scripturenow.utils.models.votd

import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay

public interface VerseOfTheDayToMemoryVerseConverter {
    public fun convertVerseOfTheDayToMemoryVerse(
        verseOfTheDay: VerseOfTheDay,
    ): MemoryVerse
}
