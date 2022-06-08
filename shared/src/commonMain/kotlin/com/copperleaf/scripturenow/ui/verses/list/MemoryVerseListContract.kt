package com.copperleaf.scripturenow.ui.verses.list

import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse

object MemoryVerseListContract {
    data class State(
        val verses: Cached<List<MemoryVerse>> = Cached.NotLoaded(),
    )

    sealed class Inputs {
        data class Initialize(val forceRefresh: Boolean) : Inputs()
        data class VersesUpdated(val verses: Cached<List<MemoryVerse>>) : Inputs()
        object CreateVerse : Inputs()
        data class ViewVerse(val verse: MemoryVerse) : Inputs()
        data class EditVerse(val verse: MemoryVerse) : Inputs()
        data class DeleteVerse(val verse: MemoryVerse) : Inputs()
    }

    sealed class Events {
        object NavigateBack : Events()
        data class NavigateTo(val destination: String) : Events()
    }
}
