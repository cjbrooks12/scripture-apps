package com.copperleaf.scripturenow.repositories.verses

import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse

object MemoryVersesRepositoryContract {
    data class State(
        val initialized: Boolean = false,

        val dataListInitialized: Boolean = false,
        val dataList: Cached<List<MemoryVerse>> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        object ClearCaches : Inputs()
        object Initialize : Inputs()
        object RefreshAllCaches : Inputs()

        data class RefreshDataList(val forceRefresh: Boolean) : Inputs()
        data class DataListUpdated(val dataList: Cached<List<MemoryVerse>>) : Inputs()
    }
}
