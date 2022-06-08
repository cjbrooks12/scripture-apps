package com.copperleaf.scripturenow.repositories.verses

import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse

object MemoryVersesRepositoryContract {
    data class State(
        val initialized: Boolean = false,

        val memoryVerseListInitialized: Boolean = false,
        val memoryVerseList: Cached<List<MemoryVerse>> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        object ClearCaches : Inputs()
        object Initialize : Inputs()
        object RefreshAllCaches : Inputs()

        data class RefreshMemoryVerseList(val forceRefresh: Boolean) : Inputs()
        data class MemoryVerseListUpdated(val memoryVerseList: Cached<List<MemoryVerse>>) : Inputs()
        data class CreateOrUpdateMemoryVerse(val memoryVerse: MemoryVerse) : Inputs()
        data class DeleteMemoryVerse(val memoryVerse: MemoryVerse) : Inputs()
    }
}
