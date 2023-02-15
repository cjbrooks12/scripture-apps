package com.caseyjbrooks.scripturenow.repositories.memory

import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.map

public object MemoryVerseRepositoryContract {
    public data class State(
        val initialized: Boolean = false,

        val memoryVerseListInitialized: Boolean = false,
        val memoryVerseList: Cached<List<MemoryVerse>> = Cached.NotLoaded(),
    ) {
        val mainMemoryVerse: Cached<MemoryVerse> = memoryVerseList
            .map { verses ->
                verses.single { it.main }
            }
    }

    public sealed class Inputs {
        public object ClearCaches : Inputs()
        public object Initialize : Inputs()
        public object RefreshAllCaches : Inputs()

        public data class RefreshMemoryVerseList(val forceRefresh: Boolean) : Inputs()
        public data class MemoryVerseListUpdated(val memoryVerseList: Cached<List<MemoryVerse>>) : Inputs()
        public data class CreateOrUpdateMemoryVerse(val memoryVerse: MemoryVerse) : Inputs()
        public data class SaveAsMemoryVerse(val verseOfTheDay: VerseOfTheDay) : Inputs()
        public data class SetAsMainVerse(val memoryVerse: MemoryVerse) : Inputs()
        public object ClearMainVerse : Inputs()
        public data class DeleteMemoryVerse(val memoryVerse: MemoryVerse) : Inputs()
    }

    public sealed class Events
}
